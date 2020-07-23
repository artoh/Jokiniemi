# Määrittelydokumentti

## Algoritmit ja tietorakenteet ratkaistavien ongelmien mukaisesti

### Pelilaudan tallentaminen

Pelilauta muodostuu 199 ruudusta, joiden välillä voi liikkua eri kulkuneuvoilla. Ruutujen välillä olevien yhteyksien määrä vaihtelee.

Pelilauta on tallennettu tekstitiedostoon (sisällytetään jar-pakettiin)

Tehtäessä päätöstä herra X:n siirrosta tarvitaan tieto, minne kustakin ruudusta pääsee. Samoin tietoa tarvitaan kun etsiville esitetään kulloinkin valittavissa olevat vaihtoehdot.

Tietorakenne: Vieruslistat toteutetaan yhteen suuntaan linkitettyinä listoina, joiden ensimmäisten alkioiden osoittimet sijoitetaan taulukkoon

*Vaihtoehtoisesti* käytetään vieruslistoihin isoa taulukkoa (tällöin kuluu hieman ylimääräistä tilaa, mutta toteutus on todennäköisesti nopeampi). Tuloksena olisi ehkä kuitenkin selkeämpi rajapinta.

Aikavaativuudet: Tiedon lisääminen ja tietyn ruudun vieruslistan hakeminen O(1)

### Pelitilanteen ja -historian tallentaminen

Pelihistoriaan kuuluu tieto herra X:n ja etsivien liikkeistä (ruudut ja käytetyt kulkuneuvot). Jos totetetaan matkalippujen / tuplauskorttien / mustien lippujen käyttö, on myös niiden lukumäärästä pidettävä kirjaa.

Tietorakenne: Pelitilanne tallennetaan pelaajakohtaisiin taulukkoihin

Aikavaativuus: Tiedon lisääminen ja haku O(1)

### Ruutujen välisten etäisyyksien määrittäminen (pelivuoroissa)

Arvioitaessa herra X:n siirtoa on tarpeen tietää, kuinka nopeasti etsivät saavuttaisivat kyseisen ruudun. Koska etäisyysarvioita etsivien sijainneista vaihtoehtoisille X:n etenemissuunnille tehdään pelin kuluessa paljon, lienee järkevää laskea ruutujen väliset etäisyydet etukäteen.

Kun pelilauta on ladattu, lasketaan kaikkien ruutujen välisistä etäisyyksistä etäisyysmatriisi *Vaihtoehto 1* Floydin ja Warshallin algoritmilla. Koska kaikki etäisyydet ovat symmetrisiä, muokataan algoritmia niin että etäisyydet lasketaan ja tallennetaan vain yhteen kertaan.

Tietorakenne: Taulukko, tilavaativuus O(n<sup>2</sup>)

Aikavaativuudet: Taulukon muodostaminen O(n<sup>3</sup>), tiedon hakeminen O(1)

*Vaihtoehto 2:* Koska kaikkien ruutujen välisiä etäisyyksiä tuskin tarvitaan, voitaisiin etäisyykisä laskea sopivalla algoritmilla vasta tarvittaessa (tai siten, että laskettu etäisyys tallennetaan välimuistiin jolloin jo laskettuja etäisyyksiä ei tarvitse laskea uudestaan).

*Vaihtoehto 3:* Ruutujen väliset etäisyydet generoidaan valmiiksi tiedostoon eräajona (sopivalla algoritmilla) ja luetaan sieltä. Tilavaativuus tiedostolle O(n<sup>2</sup>), nykyisillä tietokoneiden nopeuksilla vs. leveymuistin nopeus ei liene järkevä vaihtoehto.

*Vaihtoehto 4:* Käytetään muita algoritmeja etäisyyksien laskemiseen.

Koska kartan lataaminen ja etäisyyksien laskeminen tehdään ohjelman alussa, voisi tässä toteuttaa vertailun eri metodien välille.

### Satunnaislukujen tuottaminen

Pelin alussa poliisien ja herra X:n alkusijainint arvotaan (tietyistä vaihtoehdoista).

Algoritmi: Sopiva satunnaislukugeneraattori, esimerkiksi Linear Congruential Generator. (Jos ohjelma on muuten jäämässä algoritmien osalta liian yksinkertaiseksi, voisi tässä harkita monimutkaisempaa toteutusta)

Tilavaatuvuus: O(1)

Aikavaativuus: O(1)


### Herra X:n siirron valitseminen

Koska etsivät eivät tiedä herra X:n sijaintia, ei peli ole deterministinen. Kartassa kahden ruudun välinen etäisyys on enintään 10 vuoroa.

Siirron valitsemisessa lienee järkevintä käyttää heuristista lähetymistapaa, jossa eri vaihtoehdot pisteytetään (kriteereinä siirron turvallisuus eli mahdollisuus jäädä kiinni sekä etsivien hämääminen eli kuinka vaikea herra X:n sijainti on päätellä hänen käyttämiensä matkalippujen perusteella). Loppuvaiheessa pystytään min-max -periaatteella ehkä pääsemään paikkaan, joka on täysin turvallinen, mutta muuten herra X valitsee jonkin tarjolla olevista hyvistä vaihtoehdoista.

Algoritminen vaativuus projektissa perustuisi kuitenkin enemmän verkon käsittelyyn ja satunnaisuuden toteuttamiseen.

Aikavaativuus pahimmillaan: O(n<sup>m</sup>)

Tilavaativuus: O(m)


## Alustava arkkitehtuuri
**Tämä osio siirretään myöhemmin toteutusdokumenttiin**

- jokiniemi.game
  - pelilauta
  - aloitussijaintien arvonta
  - pelitilanne (nappuloiden sijainnit)
  - pelitila (kenen vuoro, voitto, häviö)
- jokiniemi.algorithm
  - verkon etäisyyksien laskenta
  - satunnaislukugeneraattori
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

Palikat liitetään toisiinsa riippuvuuksien injektointia käyttäen.
