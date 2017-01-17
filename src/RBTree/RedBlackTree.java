package RBTree;

import ru.mail.polis.ISortedSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

enum Color {
    BLACK, RED
}

public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    public RBNode<E> root, nullNode;
    private final Comparator<E> comparator;
    int size=0;

    public RedBlackTree() {
        nullNode = new RBNode();
        nullNode.setLeft(nullNode);
        nullNode.setRight(nullNode);
        root = nullNode;
        comparator=null;
    }

    public void delete(RBNode z) {
        RBNode y = z, x;
        Color yOrigin = y.getColor();
        if (z.left() == nullNode) {
            x = z.right();
            transplant(z, z.right());
        } else if (z.right() == nullNode) {
            x = z.left();
            transplant(z, z.left());
        } else {
            y = minChild(z.right());
            yOrigin = y.getColor();
            x = y.right();
            if (y.parent() == z) {
                x.setParent(y);
            } else {
                transplant(y, y.right());
                y.setRight(z.right());
                y.right().setParent(y);
            }
            transplant(z, y);
            y.setLeft(z.left());
            y.left().setParent(y);
            y.setColor(z.getColor());
        }
        if (yOrigin == Color.BLACK) {
            deleteFixup(x);
        }
        size--;
    }

    void transplant(RBNode u, RBNode v) {
        if (u.parent() == nullNode) {
            setRoot(v);
        } else if (u == u.parent().left()) {
            u.parent().setLeft(v);
        } else {
            u.parent().setRight(v);
        }
        v.setParent(u.parent());
    }

    RBNode minChild(RBNode x) {
        while (x.left() != nullNode) {
            x = x.left();
        }
        return x;
    }

    void deleteFixup(RBNode x) {
        while (x != root && x.isBlack()) {
            RBNode w;
            if (x.isLeft()) {
                w = x.parent().right();
                if (w.isRed()) {
                    w.setBlack();
                    x.parent().setRed();
                    leftRotate(x.parent());
                    w = x.parent().right();
                }
                if (w.left().isBlack() && w.right().isBlack()) {
                    w.setRed();
                    x = x.parent();
                } else {
                    if (w.right().isBlack()) {
                        w.left().setBlack();
                        w.setRed();
                        rightRotate(w);
                        w = x.parent().right();
                    }
                    w.setColor(x.parent().getColor());
                    x.parent().setBlack();
                    w.right().setBlack();
                    leftRotate(x.parent());
                    x = getRoot();
                }
            } else {
                w = x.parent().left();
                if (w.isRed()) {
                    w.setBlack();
                    x.parent().setRed();
                    leftRotate(x.parent());
                    w = x.parent().left();
                }
                if (w.right().isBlack() && w.left().isBlack()) {
                    w.setRed();
                    x = x.parent();
                } else {
                    if (w.left().isBlack()) {
                        w.left().setBlack();
                        w.setRed();
                        leftRotate(w);
                        w = x.parent().left();
                    }
                    w.setColor(x.parent().getColor());
                    x.parent().setBlack();
                    w.left().setBlack();
                    rightRotate(x.parent());
                    x = getRoot();
                }
            }
        }
        x.setBlack();
    }

    void leftRotate(RBNode x) {
        RBNode y = x.right();
        x.setRight(y.left());//Turn y's left subtree into x's subtree
        if (y.left() != nullNode) {
            y.left().setParent(x);
        }
        y.setParent(x.parent());
        if (x.parent() == nullNode) {
            setRoot(y);
        } else if (x == x.parent().left()) {
            x.parent().setLeft(y);
        } else {
            x.parent().setRight(y);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    void rightRotate(RBNode x) {
        RBNode y = x.left();
        x.setLeft(y.right());//Turn y's left subtree into x's subtree
        if (y.right() != nullNode) {
            y.right().setParent(x);
        }
        y.setParent(x.parent());
        if (x.parent() == nullNode) {
            setRoot(y);
        } else if (x == x.parent().left()) {
            x.parent().setLeft(y);
        } else {
            x.parent().setRight(y);
        }
        y.setRight(x);
        x.setParent(y);
    }

    public RBNode getRoot() {
        return root;
    }

    void setRoot(RBNode root) {
        this.root = root;
    }

    public void inorder(RBNode x) {
        if (x != nullNode) {
            inorder(x.left());
            System.out.print(x.getKey() + " " + x.isRed() + "\t");
            inorder(x.right());
        }
    }

    public void insert(RBNode z) {
        RBNode y = nullNode;
        RBNode x = getRoot();
        while (x != nullNode) {
            y = x;

            if (z.getKey().compareTo(x.getKey())<0) {
                x = x.left();
            } else {
                x = x.right();
            }
        }
        z.setParent(y);
        if (y == nullNode) {
            setRoot(z);
        } else if (z.getKey().compareTo(y.getKey())<0) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }
        z.setLeft(nullNode);
        z.setRight(nullNode);
        z.setRed();
        fixup(z);
        size++;
    }

    private void fixup(RBNode z) {
        while (z.parent().isRed()) {
            if (z.uncle() != nullNode && z.uncle().isRed()) {
                z.parent().setBlack();
                z.uncle().setBlack();
                z.grandParent().setRed();
                z = z.grandParent();
            } else {
                if (z.isRight() && z.parent().isLeft()) {
                    leftRotate(z.parent());
                    z = z.left();
                } else if (z.isLeft() && z.parent().isRight()) {
                    rightRotate(z.parent());
                    z = z.right();
                }
                z.parent().setBlack();
                z.grandParent().setRed();
                if (z.isLeft()) {
                    rightRotate(z.grandParent());
                } else {
                    leftRotate(z.grandParent());
                }
            }
        }
        getRoot().setBlack();
    }

    RBNode successor(RBNode x) {
        if (x.right() != nullNode) {
            x = x.right();
            while (x.left() != nullNode) {
                x = x.left();
            }
            return x;
        } else {
            RBNode y = x.parent();
            while (y != nullNode && x == y.right()) {
                x = y;
                y = y.parent();
            }
            return y;
        }
    }

    RBNode predecessor(RBNode x) {
        RBNode predc;
        if (x.left() != nullNode) {
            predc = x.left();
            while (predc.right() != nullNode) {
                predc = predc.right();
            }
            return predc;
        } else {
            predc = x.parent();
            while (predc != nullNode && x == predc.left()) {
                x = predc;
                predc = predc.parent();
            }
        }
        return predc;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private void inorderTraverseRL(RBNode<E> node, List<E> list) {
        if (node != nullNode && node != null) {
            inorderTraverseRL(node.left(), list);
            list.add(node.getKey());
            inorderTraverseRL(node.right(), list);
        }
    }
    @Override
    public String toString() {
        List<E> list = inorderTraverse();
        return list.toString();
    }

    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<E>();
        inorderTraverseRL(root, list);
        return list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if(size==0) return true;
        return false;
    }

    @Override
    public boolean contains(E value) {
        if(lookupHelper(value, root)!=null) return true;
        return false;
    }

    private RBNode<E> lookupHelper(E element, RBNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getKey().compareTo(element) == 0) {
            return node;
        }
        if (node.getKey().compareTo(element) < 0
                && node.right() != nullNode) {
            return lookupHelper(element, node.right());
        }
        if (node.getKey().compareTo(element) > -1
                && node.left() != nullNode) {
            return lookupHelper(element, node.left());
        }
        return null;
    }

    @Override
    public boolean add(E value) {
        insert(new RBNode(value));
        size++;
        return true;
    }

    @Override
    public boolean remove(E value) {
        RBNode<E> node=lookupHelper(value, root);
        if(node!=null){
            delete(node);
            size--;
            return true;
        }

        return false;
    }
}

class RBNode<E extends Comparable<E>>{
    private RBNode parent;
    private RBNode right;
    private RBNode left;

    public E getKey() {
        return key;
    }

    public void setKey(E key) {
        this.key = key;
    }

    public RBNode() {
        parent=null;
        right=null;
        left=null;
        key=null;
        color=Color.BLACK;
    }

    public RBNode(E key) {
        this.key = key;
    }

    E key;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setParent(RBNode parent) {
        this.parent = parent;
    }

    public void setRight(RBNode right) {
        this.right = right;
    }

    void setBlack() {
        color = Color.BLACK;
    }

    void setRed() {
        color = Color.RED;
    }

    boolean isRed() {
        if (color == Color.RED) {
            return true;
        }
        return false;
    }

    boolean isBlack() {
        if (color == Color.BLACK) {
            return true;
        }
        return false;
    }

    RBNode uncle() {
        if (this.parent() == null || this.parent().parent() == null) {
            return null;
        }
        if (this.parent().parent().left() == this.parent()) {
            return this.parent().parent().right();
        } else {
            return this.parent().parent().left();
        }
    }

    RBNode parent() {
        return parent;
    }

    RBNode left() {
        return left;
    }

    RBNode right() {
        return right;
    }

    RBNode grandParent() {
        return this.parent().parent();
    }

    boolean isLeft() {
        if (this.parent().left() == this) {
            return true;
        } else {
            return false;
        }
    }
    boolean isRight() {
        return !isLeft();
    }

    public void setLeft(RBNode left) {
        this.left = left;
    }
}