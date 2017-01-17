package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//TODO: write code here
public class OpenHashTable<E extends Comparable<E>> implements ISet<E> {

    private Comparator<E> comparator;
    private int TABLE_SIZE = 8;
    private int size;
    private double loadFactor;
    HashEntry<E>[] table;

    public OpenHashTable() {
        table = new HashEntry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = null;
        }
    }

    public OpenHashTable(int tableSize) {
        table = new HashEntry[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = null;
        }
    }

    public OpenHashTable(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (table[i] != null) {
                list.add(table[i].getValue().toString());
            } else {
                list.add("null");
            }
        }
        return list.toString();
    }    public int getPos(E value) {
        int hash = hashCode(value);
        while (table[hash] != null && !table[hash].getValue().equals(value)) {
            hash = (hash + 1) % TABLE_SIZE;
        }
        if (table[hash] == null) {
            return -1;
        } else {
            return hash;
        }
    }

    public void put(E value) {
        int hash = hashCode(value);
        while (table[hash] != null) {
            hash = (hash + 1) % TABLE_SIZE;
        }
        table[hash] = new HashEntry(value);
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            return false;
        }
        int hash = hashCode(value);
        while (table[hash] != null && !table[hash].getValue().equals(value)) {
            hash = (hash + 1) % TABLE_SIZE;
        }
        if (table[hash] == null) {
            return false;
        } else {
            return true;
        }
    }

    public double getLoadFactor() {
        loadFactor = 1.0 * size / TABLE_SIZE;
        return loadFactor;
    }

    private void reHash() {
        // create new table
        int tableSizeNew = TABLE_SIZE * 2;
        HashEntry<E>[] tableNew = new HashEntry[tableSizeNew];
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (table[i] != null) {
                int hash = hashCode(table[i].getValue());
                while (tableNew[hash] != null) {
                    hash = (hash + 1) % tableSizeNew;
                }
                tableNew[hash] = new HashEntry(table[i].getValue());
            }
        }
        TABLE_SIZE = tableSizeNew;
        table = tableNew;
    }

    @Override
    public boolean add(E value) {
        if (getLoadFactor() > 0.5) {
            reHash();
        }
        put(value);
        return true;
    }

    @Override
    public boolean remove(E value) {
        if (contains(value)) {
            table[getPos(value)] = null;
            size--;
        }
        return false;
    }



    private int hashCode(E value) {
        return Math.abs(value.toString().hashCode()) % TABLE_SIZE;
    }
}

class HashEntry<E> {
    private E value;

    HashEntry(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }
}
