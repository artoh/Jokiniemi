# Määrittelydokumentti

## Algoritmit ja tietorakenteet ratkaistavien ongelmien mukaisesti

### Pelilaudan tallentaminen

Pelilauta muodostuu 199 ruudusta, joiden välillä voi liikkua eri kulkuneuvoilla. Kyse on siis verkosta.

Pelilauta on tallennettu teksitiedostoon (sisällytetään jar-pakettiin)

Tehtäessä päätöstä herra X:n siirrosta tarvitaan tieto, minne kustakin ruudusta pääsee. Samoin tietoa tarvitaan kun etsiville esitetään kulloinkin valittavissa olevat vaihtoehdot.

Tietorakenne: Taulukkoon toteutettu vieruslista

Aikavaativuudet: Tiedon lisääminen ja hakeminen O(1)

### Pelitilanteen ja -historian tallentaminen

Pelihistoriaan kuuluu tieto herra X:n ja etsivien liikkeistä (ruudut ja käytetyt kulkuneuvot). Jos totetetaan matkalippujen / tuplauskorttien / mustien lippujen käyttö, on myös niiden lukumäärästä pidettävä kirjaa.

Tietorakenne: Pelitilanne tallennetaan taulukkoihin

Aikavaativuus: Tiedon lisääminen ja haku O(1)

### Ruutujen välisten etäisyyksien määrittäminen (pelivuoroissa)

Arvioitaessa herra X:n siirtoa on tarpeen tietää, kuinka nopeasti etsivät saavuttaisivat kyseisen ruudun. Koska etäisyysarvioita etsivien sijainneista vaihtoehtoisille X:n etenemissuunnille tehdään pelin kuluessa paljon, lienee järkevää laskea ruutujen väliset etäisyydet etukäteen.

Kun pelilauta on ladattu, lasketaan kaikkien ruutujen välisistä etäisyyksistä etäisyysmatriisi Floydin ja Warshallin algoritmilla. Koska kaikki etäisyydet ovat symmetrisiä, muokataan algoritmia niin että etäisyydet lasketaan ja tallennetaan vain yhteen kertaan.

Tietorakenne: Taulukko, tilavaativuus O(n<sup>2</sup>)
Aikavaativuudet: Taulkon muodostaminen O(n<sup>3</sup>), tiedon hakeminen O(1)

*Vaihtoehto 1:* Koska kaikkien ruutujen välisiä etäisyyksiä tuskin tarvitaan, voitaisiin etäisyykisä laskea sopivalla algoritmilla vasta tarvittaessa (tai siten, että laskettu etäisyys tallennetaan välimuistiin jolloin jo laskettuja etäisyyksiä ei tarvitse laskea uudestaan).

*Vaihtoehto 2:* Ruutujen väliset etäisyydet generoidaan valmiiksi tiedostoon eräajona (sopivalla algoritmilla) ja luetaan sieltä. Tilavaativuus tiedostolle O(n<sup>2</sup>), nykyisillä tietokoneiden nopeuksilla vs. leveymuistin nopeus ei liene järkevä vaihtoehto.

### Satunnaislukujen tuottaminen

Pelin alussa poliisien ja herra X:n alkusijainint arvotaan (tietyistä vaihtoehdoista).

Algoritmi: Sopiva satunnaislukugeneraattori

Tilavaatuvuus: O(1)

Aikavaativuus: O(1)

### Herra X:n mahdollisten ruutujen seuraaminen

Koska Herra X paljastaa sijaintinsa ajoittain sekä joutuu ilmaisemaan, mitä kulkunevoja on käyttänyt, pystyvät etsivät päättelemään, mitkä ovat Herra X:n mahdolliset ruudut. Herra X:n on edullisinta tehdä sellaisia siirtoja, joiden avulla etsivät eivät pysty kovin helposti päättelemään hänen sijaintiaan. Siksi on tarkoituksenmukaista pitää yllä listaa niistä ruuduista, joissa herra X voisi etsivien päättelyn mukaan olla.

Tietorakenne: taulukko


### Siirtovaihtoehtojen heuristiikka

Herra X:n mahdolliset siirrot pisteytetään. Pisteytyksessä otetaan huomioon
- ruudun etäisyys etsiviin
- liikkumismahdollisuudet ruudusta
- ruudun vaikutus "mahdollisiin ruutuihin"
- huomioitava tuplasiirrot ja mustan kortin käyttö
- painotettava liikkumismahdollisuuksia niillä vuoroilla, joilla herra X joutuu paljastamaan sijaintinsa

Tavoitteena on toteuttaa ohjelman tietorakenteet siten, että itse heuristiikkafunktio voidaan toteuttaa nopeassa vakioajassa.


### Herra X:n siirron valitseminen

Koska etsivät eivät tiedä herra X:n sijaintia, ei peli ole deterministinen. Vastaavasti on myös vältettävä sitä, että herra X:n liikkeet olisivat pelaajille liian helposti arvattavissa.

Algoritmia olisi ehkä suunniteltava [Expectiminimax](https://en.wikipedia.org/wiki/Expectiminimax) -algoritmin pohjalta niin, että tietokone ei väistämättä valitse sitä vaihtoehtoa, joka on pisteytetty parhaaksi, vaan esim. käyttää heuristisia arvioita painotuksina arvonnassa (ja näin tuottaa hankalammin arvattavissa olevia tuloksia). Jotta laskennassa päästään riittävään syvyyteen (ehkä 3..8 siirtoa) tarvittaneen tähän alpha-beta-pruning -tyyppistä vaihtoehtojen karsimista.

Aikavaativuus pahimmillaan: O(n<sup>m</sup>)

Tilavaativuus: O(m)


## Alustava arkkitehtuuri
**Tämä osio siirretään myöhemmin toteutusdokumenttiin**

- jokiniemi.peli
  - pelilauta
  - aloitussijaintien arvonta
  - pelitianne (nappuloiden sijainnit)
  - pelitila (kenen vuoro, voitto, häviö)
- jokiniemi.ai
  - herra X:n siirtojen päättäminen
    - siirtovaihtoehtojen heuristiikka
    - valinnan tekeminen eri vaihtoehdoista
  - parametrien avulla "älykkyys" säädettävissä
- jokiniemi.ui
  - käyttöliittymä
    - pelivalinnat, pelin käynnistäminen
    - pelitilanteen esittäminen käyttäjälle
    - etsivien siirtojen tekeminen
