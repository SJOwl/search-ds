package test;

import ru.mail.polis.RedBlackTree;

import static org.junit.Assert.assertTrue;

public class RBTreeTest {
    static RedBlackTree<Integer> tree;
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        tree = new RedBlackTree<>();
        System.out.println("tests");
        assertTrue("isEmpty", tree.isEmpty() == true);
        assertTrue("size!=0", tree.size() == 0);
        printTree();


        for (int i = 2; i < 10; i++){
            tree.add(i);
//            printTree();
//            System.out.println("adding "+tree.contains(i));
        }
        printTree();
        for (int i = 2; i < 10; i++){
            System.out.println("removing "+i+":");
            System.out.println(tree.remove(i));
            if(tree.contains(i)==true)
                System.out.println(i+" is not removed");
            printTree();
        }

//        System.out.println("first = " + tree.first());
//        System.out.println("last = " + tree.last());
//        tree.add(70);
//        printTree();
//        System.out.println("first = " + tree.first());
//        System.out.println("last = " + tree.last());
//        assertTrue("size!=1", tree.size() == 1);
//        tree.add(66);
//        printTree();
//        System.out.println("first = " + tree.first());
//        System.out.println("last = " + tree.last());
//        tree.add(57);
//        printTree();
//        tree.add(54);
//        printTree();
//        tree.add(51);
//        printTree();
//        tree.add(55);
//        printTree();
//        assertTrue("isEmpty", tree.isEmpty() == false);
//        assertTrue(tree.contains(55) == true);
//        // size
//        // isEmpty
//        // contains
//        // add
//        // remove
//        tree.remove(55);
//        printTree();
//        System.out.println(tree.size());
//        assertTrue("size!=5", tree.size() == 5);
//        System.out.println("first = " + tree.first());
//        System.out.println("last = " + tree.last());
//        // inorderTraverse
//        tree.add(65);
//        printTree();
//        tree.add(95);
//        printTree();
//        tree.add(152);
//        printTree();
//        tree.add(140);
//        printTree();
//        System.out.println("51: "+tree.contains(51));
//        System.out.println("null: "+tree.contains(null));


    }

    private static void printTree() {
        System.out.println(tree.toString());
    }
}
