# Testausdokumentti

## Yksikkötestaus

Käyttöliittymää lukuun ottamatta ohjelmalle on toteutettu kattavat JUnit-yksikkötestit. Luokkien testaamisessa on käytetty osittain mock-olioita ja osittain luokkaan kiinteästi liittyvä toinen luokka on jo yksikkötestissä mukana.

Yksikkötestejä ei ole toteutettu niille tapahtumille, joita ohjelmassa ei ole mahdollista tulla vastaan (esim. pelilaudan lukemisessa tiedoston puuttumiselle, koska tiedosto on sisällytetty jar-pakettiin). Tekoälyä testattaessa ei juurikaan testata sitä, tekeekö teköäly aina tarkoituksenmukaisen siirron.

Testien rivikattavuus (käyttöliittymä pois luettuna) on tällä hetkellä 96%.

## Integraatiotestaus

StupidDetectiveTest -integraatiotestit suorittavat koko pelin läpi niin, että simuloitu käyttäjä tekee satunnaisia siirtoja.  

## Järjestelmätestaus

Sovellus on järjestelmätestattu manuaalisesti pelaamalla. Samalla on arvioitu tekoälyn haastavuutta ihmispelaajaa vastaan.

Tekoälyn haastavuutta on testattu myös seitsemänvuotiaalla testipelaajalla, joka onnistuu lähes aina voittamaan tietokoneen.

## Suorituskykytestaus

Ohjelmaan on integroitu yksinkertaiset suorituskykytestit etäisyyksien hakemiselle ja tekoälylle.

Testiparametri    |    Testattava asia   |  Tyypillinen tulos
----------------|----------------------|--------------------------------
test FW         | Floydin-Warshallin etäisyyksien haku |  0,02 s.
test TA         | Lippujen määrät huomioiva etäisyyksien haku | 1,2 s.
test SA         | Yksinkertainen tekoäly  | 0,001 s.
test HA         | Heuristinen tekoäly     | 1 ... 20 s.

**Tähän diagrammi heuristisen tekoälyn testin vaihtelusta**

Testi suoritetaan antamalla yllä oleva parametri, esim ```java -jar jokiniemi.jar test FW```.

Etäisyystesti hakee 20 000 kahden ruudun välistä etäisyyttä (eli noin puolet ruutujen välisistä etäisyyksistä). Ei liene yllättävää, että kaikki etäisyydet taulukkoon hakeva algoritmi on ylivoimainen verrattuna algoritmiin, jossa syvyyshakuun on yhdistetty joukko kolmeen eri lippulajiin liittyviä tarkastuksia.

Tekoälyn testi suorittaa koko 24 vuoroa kestävän pelin niin, että simuloitu käyttäjä tekee satunnaisia valintoja (vastaa ohjelman integraatiotestiä). Heuristisen tekoälyn testin suuri hajonta johtuu testiin liittyvästä satunnaisuudesta: pelin tekoäly hidastuu huomattavasti silloin, kun se joutuu käyttämään hitaampaa lippujen määrän huomioivaa algoritmia arvioidessaan etsivien uhkaa Mr X:lle ja etsivät sattuvat olemaan algoritmin kannalta äärimmäisissä sijanneissa. Ihmispelaajaa vasten pelattaessa 0,5 - 1 sekunnin siirtoviive on kuitenkin täysin hyväksyttävissä. Käytännössä tästä testistä oli suurta hyötyä määriteltäessä saarretuksen joutumisen vaaraa arvoivan algoritmin hakusyvyyttä niin, että peli ei hidastu liikaa.
