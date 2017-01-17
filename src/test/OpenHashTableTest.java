package test;

import ru.mail.polis.OpenHashTable;

import static org.junit.Assert.*;

public class OpenHashTableTest {

    static OpenHashTable<String> table;

    public static void main(String[] args) {
        System.out.println("testing openHash...");
        table=new OpenHashTable<>();
        printSize();
        for (Integer i = 0; i < 20; i++) {
            add("s"+i.toString());
        }

        for (Integer i = 0; i < 20; i++) {
            remove("s"+i.toString());
        }

    }

    static void printSize(){
        System.out.println("size: "+table.size());
    }
    static void add(String val){
        System.out.println("add "+val);
        table.add(val);
        printSize();
        print();
        System.out.println();
    }
    static void print(){
        System.out.println(table.toString());
    }
    static void remove(String val){
        System.out.println("remove: "+ val);
        table.remove(val);
        printSize();
        print();
        System.out.println();
    }
}