/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.algorithm;

/**
 * Array of integers
 * 
 * @author arto
 */
public class IntegerArray {
    
    /**
     * Constructor
     * 
     * @param initialCapacity Initial capacity
     */
    public IntegerArray(int initialCapacity) {
        this.capacity = initialCapacity;
        this.data = new int[initialCapacity];
    }
        
    /**
     * Add a integer at the end
     * 
     * @param value Integer to append
     */
    public void push(int value) {
        if( capacity == counter) {
            capacity = capacity * 2;
            int newArray[] = new int[capacity];
            for(int i=0; i < counter; i++) {
                newArray[i]=data[i];
            }
            data = newArray;
        }
        data[counter] = value;
        counter++;
    }
    
    /**
     * Size of array
     * @return size
     */
    public int count() {
        return this.counter;
    }
    
    /**
     * Element at position
     * @param index Index, beginning at 0
     * @return integer at position index
     */
    public int at(int index) {
        return this.data[index];
    }        
    
    private int[] data;
    private int counter;
    private int capacity;
    
}
