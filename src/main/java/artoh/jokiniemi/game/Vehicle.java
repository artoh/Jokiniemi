package artoh.jokiniemi.game;

/**
 * Pelissä käytetyt ajoneuvot ja lipputyypit
 * 
 * @author ahyvatti
 */
public enum Vehicle {
    START_SQUARE,
    TAXI,
    BUS,
    UNDERGROUD,
    FERRY,
    BLACK_CARD,
    /*
    Kun Mr X käyttä tuplauskorttien, merkitään pelaajien lokiin DOUBLED sen
    siirron kohdalle, joka etsiviltä jää tuplauksen takia tekemättä.
    */
    DOUBLED  
}
