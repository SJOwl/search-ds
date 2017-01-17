package RBTree;


import java.net.Socket;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    static RedBlackTree<Integer> tree;

    public static void main(String[] args) {

        for (int j = 0; j < 4; j++) {
            tree=new RedBlackTree<>();
            for (int i = 0; i < 4; i++) {
                add(i);
            }
            remove(j);
            System.out.println();
            System.out.println();
        }
    }
    static void add(Integer x){
        System.out.println("adding "+x+": ");
        tree.add(x);
        print();
    }
    static void print(){
        System.out.println(tree.toString());
    }
    static void remove(Integer x){
        System.out.println("removing "+x+"...");
        tree.remove(x);
        print();
    }
}