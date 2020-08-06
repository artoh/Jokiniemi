package artoh.jokiniemi.struct;

/**
 * Dynaamisen kokoinen kokonaislukutaulukko
 * 
 * Tässä on toteutettuina vain ohjelmassa tarvittavat metodit.
 * Näin ollen taulukkoon voi ainoastaan lisätä alkioita, ei poistaa
 * eikä muokata.
 * 
 * @author arto
 */
public class IntegerArray {
    
    /**
     * Muodostaja
     * 
     * @param initialCapacity Alkukapasiteetti (pitää olla vähintään yksi)
     */
    public IntegerArray(int initialCapacity) {
        this.capacity = initialCapacity;
        this.data = new int[initialCapacity];
    }
        
    /**
     * Lisää kokonaisluvun taulukon loppuun
     * 
     * @param value Kokonaisluvun indeksi
     */
    public void push(int value) {
        if (capacity == counter) {
            capacity = capacity * 2 + 1;
            int newArray[] = new int[capacity];
            for (int i = 0; i < counter; i++) {
                newArray[i] = data[i];
            }
            data = newArray;
        }
        data[counter] = value;
        counter++;
    }
    
    /**
     * Taulukon koko
     * @return Taulukkoon tallennettujen alkioiden lukumäärä
     */
    public int count() {
        return this.counter;
    }
    
    /**
     * Taulukon alkio
     * @param index Indeksi (alkaen nollasta)
     * @return Taulukkoon kyseiselllä indeksillä tallennettu kokonaisluku
     */
    public int at(int index) {
        return this.data[index];
    }        
    
    private int[] data;
    private int counter;
    private int capacity;
    
}
