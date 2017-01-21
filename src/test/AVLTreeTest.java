package test;

import AVL.AVLTree;

public class AVLTreeTest {
    static AVLTree<Integer> tree;

    static void checkAdd(Integer val) {
        contain(val);
        size();
        add(val);
        print();
        size();
        contain(val);
        add(val);
        print();
        size();
        contain(val);
        System.out.println();
    }

    static void contain(Integer val) {
        System.out.println("contains " + val + ": " + tree.contains(val));
    }

    static void size() {
        System.out.println("Size: " + tree.size());
    }

    static void add(Integer val) {
        System.out.println("add: " + val + " " + tree.add(val));
    }

    private static void print() {
        System.out.println(tree.toString());
    }

    static void checkRemove(Integer val) {
        contain(val);
        size();
        remove(val);
        print();
        size();
        contain(val);
        remove(val);
        print();
        size();
        remove(val);
        System.out.println();
    }

    static void remove(Integer x) {
        System.out.println("remove " + x + " " + tree.remove(x));
    }

    public static void main(String[] args) {
        tree = new AVLTree<>();
        System.out.println("testing of AVLTree");
        for (int i = 0; i < 20; i++) {
            Integer x1=i;
            Integer x2=i-1;
            System.out.println("compare "+x1 +" & "+ x2+" = " +x1.compareTo(x2));
        }
    }

    static void check(int value) {
        for (int i = 0; i <= value; i++) {
            contain(i);
        }
        if (!tree.isEmpty()) {
            first();
            last();
        }
        size();
        for (int i = 0; i <= value; i++) {
            contain(i);
        }
        add(value);
        size();
        for (int i = 0; i <= value; i++) {
            contain(i);
        }
        add(value);
        size();
        for (int i = 0; i <= value; i++) {
            contain(i);
        }
        if (!tree.isEmpty()) {
            first();
            last();
        }
    }

    static void first() {
        System.out.println("first: " + tree.first());
    }

    static void last() {
        System.out.println("last: " + tree.last());
    }
}