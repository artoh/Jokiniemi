/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import artoh.jokiniemi.struct.IntegerArray;
import artoh.jokiniemi.struct.JumArray;
import artoh.jokiniemi.struct.PossibleLocationsSet;
import artoh.jokiniemi.struct.ScoredAlternativesArray;

/**
 *
 * @author arto
 */
public class SimpleHeuristicAI implements AIInterface {

    public SimpleHeuristicAI(BoardDistanceInterface boardDistances, RandomizeInterface randomizer) {
        this.boardDistances = boardDistances;        
        this.randomizer = randomizer;
    }
    
    @Override
    public void startGame(Game game) {
        this.game = game;        
        this.possibles = new PossibleLocationsSet(game);
        this.possibles.fill();
        this.inRoute = new boolean[game.gameBoard().squareCount() + 1];
        this.jumArray = new JumArray(game.gameBoard());
        this.firstVisiblePlayed = false;
    }

    
    public int blackCardScale(int score, Vehicle vehicle) {
        if (game.log().isVisibleTurn(game.log().currentTurn() + 1)) {
            return score / 10;
        }
        else if (game.blackCardsLeft() + 1 >= game.log().turnsLeft()) {
            return score * 2;   
        } else if (game.log().currentTurn() < 4) {
            return vehicle == Vehicle.UNDERGROUD ? score * 2 / 3 : score / 3;
        } else {        
           if (1.0 * game.log().currentTurn() / game.log().turnsTotal() >
               1.0 * (Game.BLACK_CARDS_TOTAL - game.blackCardsLeft()) / Game.BLACK_CARDS_TOTAL) {
                if (possibles.ferryPossible()) {
                    return vehicle == Vehicle.UNDERGROUD ? score * 3 / 2  : score;
                }
                return vehicle == Vehicle.UNDERGROUD ? score  : score * 7 / 8;
           } else {
                return score / 2;
           }
        }
    }
    
   public int doubleCardScale(int score) {
       if (game.doubleCardsLeft() + 1 >= game.log().turnsLeft() * 2) {
           return score * 2;
       } else if (game.log().currentTurn() < 4) {
           return score / 5;
       } else {
           return score / 3;
       }
   }
       
    
    @Override
    public void doAITurn() {
    
        int currentPosition = game.log().currentPosition(0);
        int currentTurn = game.log().currentTurn();
        
        if (game.log().isVisibleTurn(currentTurn)) {
            possibles.init(currentPosition);
        } else if(currentTurn > 0) {
            possibles = possibles.nextSet(game.log().vehicle(0, currentTurn), true);
        } else {
            possibles.removeDetectiveLocations();
        }        
        
        int connections = game.gameBoard().connectionsCount(currentPosition);
        boolean onlyTaxi = possibles.onlyTaxiPossible();
        
        ScoredAlternativesArray alternatives = new ScoredAlternativesArray();        
        
        System.out.println("Turn " + currentTurn + " " + game.log().turnsLeft() + " left " + (onlyTaxi ? "TAXI" : " ") + currentPosition);
        inRoute[currentPosition] = true;
        
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(currentPosition);
        boolean inTrouble = analyseJum(2, jumSquare, 1) == 0;
        
        for (int i = 0; i < connections; i++) {
            int squareTo = game.gameBoard().connectionTo(currentPosition, i);
            Vehicle vehicle = game.gameBoard().connectionVehicle(currentPosition, i);                        
            
            if (nearestDetectiveDistance(squareTo) == 0) {
                if (vehicle != Vehicle.FERRY) {
                    alternatives.add(vehicle, squareTo, Vehicle.START_SQUARE, 0, 0);
                }
                continue;
            }
            if (vehicle == Vehicle.FERRY) {
                if (game.blackCardsLeft() > 0) {                    
                    alternatives.add(Vehicle.BLACK_CARD, squareTo, Vehicle.START_SQUARE, 0, evaluateMove(Vehicle.BLACK_CARD, squareTo, currentTurn, possibles));
                }
            } else {
                alternatives.add(vehicle, squareTo, Vehicle.START_SQUARE, 0, evaluateMove(vehicle, squareTo, currentTurn, possibles));
                if ( game.blackCardsLeft() > 0 && !onlyTaxi) {                    
                    alternatives.add(Vehicle.BLACK_CARD, squareTo, Vehicle.START_SQUARE, 0, blackCardScale(evaluateMove(Vehicle.BLACK_CARD, squareTo, currentTurn, possibles), vehicle));
                }
            }
            
            
            if (game.doubleCardsLeft() > 0 && 
                game.log().turnsLeft() > 1 &&
                inTrouble) {
                // Doubled alternativies
                for (int j = 0; j < game.gameBoard().connectionsCount(squareTo); j++) {
                    int secondTarget = game.gameBoard().connectionTo(squareTo, j);
                    Vehicle secondVehicle = game.gameBoard().connectionVehicle(squareTo, j);
                    
                    if (secondTarget == currentPosition) {
                        continue;
                    }
                    
                    if (vehicle == Vehicle.FERRY && secondVehicle == Vehicle.FERRY || 
                       ((vehicle == Vehicle.FERRY || secondVehicle == Vehicle.FERRY) && game.blackCardsLeft() == 0)) {
                        continue;
                    }
                    
                    PossibleLocationsSet secondSet = possibles.nextSet(vehicle, true);
                    int doubledMoveScore = doubleCardScale(evaluateMove(secondVehicle, secondTarget, currentTurn+1, secondSet));
                    
                    if (vehicle == Vehicle.FERRY && game.doubleCardsLeft() > 0) {
                        alternatives.add(Vehicle.BLACK_CARD, squareTo, secondVehicle, secondTarget, doubledMoveScore);                        
                    } else if (secondVehicle == Vehicle.FERRY && game.doubleCardsLeft() > 0) {
                        alternatives.add(vehicle, squareTo, Vehicle.BLACK_CARD, secondTarget, evaluateMove(Vehicle.BLACK_CARD, secondTarget, currentTurn+1, secondSet));                        
                        
                    } else if (game.doubleCardsLeft() > 0){
                        alternatives.add(vehicle, squareTo, secondVehicle, secondTarget, doubledMoveScore);   
                        if ( !game.log().isVisibleTurn(currentTurn +1) && !game.log().isVisibleTurn(currentTurn +2)) {
                            alternatives.add(vehicle, squareTo, Vehicle.BLACK_CARD, secondTarget, blackCardScale(evaluateMove(Vehicle.BLACK_CARD, secondTarget, currentTurn+1, secondSet), secondVehicle));                        
                        }
                        if ( !game.log().isVisibleTurn(currentTurn +1) && !onlyTaxi) {
                            alternatives.add(Vehicle.BLACK_CARD, squareTo, secondVehicle, secondTarget, blackCardScale(doubledMoveScore, vehicle));                        
                        }
                    } else {
                        alternatives.add(vehicle, squareTo, secondVehicle, secondTarget, doubledMoveScore);
                    }
                }
            }
        }               
        inRoute[currentPosition] = false;
        
        if (possibles.count() < 5) {
            alternatives.doMove(game, possibles, randomizer);
        } else {
            alternatives.doBestMove(game, possibles);
        }
    }
    
    
    public boolean isSafe(int square, int turn, int deep) {
        if (deep >= nearestDetectiveDistance(square)) {
            return false;
        } else if (turn == game.log().turnsTotal()) {
            System.out.println("*** Safe end ***");
            return true;
        }
        
        for (int c = 0; c < game.gameBoard().connectionsCount(square); c++) {
            if (inRoute[square] == false) {
                inRoute[square] = true;
                boolean result = isSafe(game.gameBoard().connectionTo(square, c), turn + 1, deep + 1);
                inRoute[square] = false;
                if (result) {
                    return true;
                }
            }
        }
        return false;
    } 
    
    
    public int analyseJum(int distance, JumArray.JumSquare jumSquare, int level) {
        
        int onHuntDetectives[] = new int[game.detectives()];
        int huntSquaresCount = 0;
        int jumSquares = jumSquare.count(level);
        
        for (int si = 0; si < jumSquares; si++) {
            boolean hunted = false;
            
            int square = jumSquare.square(level, si);
            for (int di = 0; di < game.detectives(); di++) {
                int detectivePosition = game.log().currentPosition(di + 1);
                if (boardDistances.distance(detectivePosition, square) <= distance) {            
                    onHuntDetectives[di]++;
                    hunted = true;
                }
            }
            if (hunted) {
                huntSquaresCount++;
            }
        }

        int huntDetectivesCount = 0;
        
        for (int i = 0; i < game.detectives(); i++) {
            if (onHuntDetectives[i] > 0) {
                huntDetectivesCount++;
            }
        }
        
        if (huntSquaresCount == jumSquares &&
            huntDetectivesCount >= jumSquares) {
            return 0;   // JUMMED !!!
        }
        
        return 200 - 150 * huntDetectivesCount / jumSquares;
        
    }
    
    public int jumPoints(int square) {        
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(square);
        int jummedOneScores = analyseJum(1, jumSquare, 1);
        int jummedNearScores = analyseJum(2, jumSquare, 1);
        int jummedTwoScores = analyseJum(2, jumSquare, 2);
        
        if (jummedOneScores < 50 || jummedTwoScores < 50) {
            return 0;
        } else {
            return jummedNearScores  + jummedTwoScores ;
        }              
    }
    
    public int connectionPoints(int square, int deep, int turn) {
        inRoute[square] = true;
        
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(square);
        int squares = jumSquare.count(2);
        int points = 0;
        
        if (deep < 4 ) {
            points = (squares > 10 ? 10 : squares) * 2 + nearestDetectiveDistance(square) * 2;
        
        
            for (int c = 0; c < game.gameBoard().connectionsCount(square); c++) {
                if (game.gameBoard().connectionVehicle(square, c) == Vehicle.FERRY) {
                    points += 10;
                } else if (game.gameBoard().connectionVehicle(square, c) == Vehicle.UNDERGROUD) {
                    points += 5;
                } else if (game.gameBoard().connectionVehicle(square, c) == Vehicle.BUS) {
                    points += 2;
                } else if (game.gameBoard().connectionVehicle(square, c) == Vehicle.TAXI) {
                    points += 1;
                }
            }

            if (game.log().turnsTotal() - turn > 3 && game.log().isVisibleTurn(turn + 1)) {
                points = points * 4;
                if (!firstVisiblePlayed) {
                    points = points * 2;
                }
            } else if( deep == 1) {
                points = points * 2;
            }


            int nearest = nearestDetectiveDistance(square);
            if (nearest == 0) {
                inRoute[square] = false;
                return 0;
            } else if( nearest == 1) {
                points = points / 2;
            }             

        } else {
            if (analyseJum(4, jumSquare, 2) > 150) {
                points = 25;
            }
        }
        
        if (deep < 6 && turn + 1 < game.log().turnsTotal()) {
            int best = 0;
            for (int i = 0; i < game.gameBoard().connectionsCount(square); i++) {
                int nextSquare = game.gameBoard().connectionTo(square, i);
                if (inRoute[nextSquare] == false) {
                    int nextPoints = connectionPoints(nextSquare, deep + 1, turn +1);
                    best = nextPoints > best ? nextPoints : best;
                }
            }            
            points += best;
        }        
        
        inRoute[square] = false;
        return points;
    }
    
    public int evaluateMove(Vehicle vehicle, int square, int turn, PossibleLocationsSet set) {
        
        if (game.log().turnsLeft() < 5 && isSafe(square, turn, 1)) {
            return 1000;    // Safe end win !
        }
        
        int jumPoints = jumPoints(square);
        
        PossibleLocationsSet newSet = set.nextSet(vehicle, true);
        int newPossibilities = game.log().isVisibleTurn(turn + 1) ? 0 : newSet.count();
        
        int hiddenScores = (newPossibilities > set.count() ? 5 : 0) +
                (newPossibilities == 1 ? 0 : (newPossibilities > 10 ? 10 : newPossibilities) * 5);
        int connectionPoints = connectionPoints(square, 1, turn);
        
        if (turn > 2 && square == game.log().position(0, turn-1)) {
            connectionPoints = connectionPoints / 8;
        }
        if (turn > 3 && square == game.log().position(0, turn-2)) {
            connectionPoints = connectionPoints / 6;
        }

        
        int nearest = nearestDetectiveDistance(square);
        int nearPoints = 0;
        if (nearest > 2) {
            nearPoints = nearest * 5 + 120;
            if (game.log().isVisibleTurn(turn + 1)) {
               nearPoints += nearest * 30;
            }
        }
            
        if (nearest > 1 && vehicle == Vehicle.UNDERGROUD) {
            nearPoints += 80;
        } 
               
        int total = jumPoints + hiddenScores + nearPoints + connectionPoints;
        if ((nearest < 2 && game.log().isVisibleTurn(turn+1)) || nearest == 0) {
            total = 0;
        } else if (nearest == 1) {
            total = total / 8;
        } else if (jumPoints < 100) {
            total = total / 3;
        }               
        
        System.out.println(square + " \t" + jumPoints + " J\t" + hiddenScores + " H (" + newPossibilities + ")\t" + nearPoints + " N\t " + connectionPoints + " C\t = " + total);
        
        return total;
    }
         
    public int nearestDetectiveDistance(int square) {      
        int nearest = Integer.MAX_VALUE;
        
        for (int i = 1; i <= game.detectives(); i++) {
            int distance = boardDistances.distance(square, game.log().currentPosition(i));
            if (distance < nearest) {
                nearest = distance;
            }
        }
        return nearest;        
    }
    
    public int detectiveSumDistance(int square) {
        
        int sum = 0;
        for (int i = 1; i <= game.detectives(); i++) {
                sum  += boardDistances.distance(square, game.log().currentPosition(i));
            }               
        return sum;
    }
    
    private BoardDistanceInterface boardDistances;
    private Game game;
    private PossibleLocationsSet possibles;
    private RandomizeInterface randomizer;
    private boolean[] inRoute;
    private boolean firstVisiblePlayed = false;
    
    private JumArray jumArray;
    
}
