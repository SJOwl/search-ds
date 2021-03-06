package AVL;

public class AVLNode<E extends Comparable<? super E>> {

    private AVLNode<E> left;
    private AVLNode<E> right;
    private AVLNode<E> parent;
    private E element;

    //the default constructor used to initialize
    //this AVLNode with a null element
    public AVLNode() {
        this(null);
    }
    /**
     * @param element: The element this AVLNode is encapsulating
     *                 <p>
     *                 The left and right children are initialized as empty.
     ***/
    public AVLNode(E element) {
        this.element = element;
        left = right = null;
    }

    public int getBalance() {
        int leftHeight = (left == null) ? 0 : left.height();
        int rightHeight = (right == null) ? 0 : right.height();
        return rightHeight - leftHeight;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public AVLNode<E> getLeft() {
        return left;
    }

    //the setLeft() and setRight() functions
    //simply set the elements for the left and right
    //AVLNodes. the actual ordering is handled by the
    //AVLTree class
    public void setLeft(E element) {
        if (left == null) {
            this.left = new AVLNode<E>(element);
        } else {
            this.left.setElement(element);
        }
        this.left.parent = this;
    }

    public AVLNode<E> getParent() {
        return parent;
    }

    public void setParent(AVLNode<E> parent) {
        this.parent = parent;
    }

    public AVLNode<E> getRight() {
        return right;
    }

    public void setRight(E element) {
        if (right == null) {
            this.right = new AVLNode<E>(element);
        } else {
            this.right.setElement(element);
        }
        this.right.parent = this;
    }

    public void setLeftNode(AVLNode<E> temp) {
        this.left = temp;
        if (temp != null) {
            this.left.parent = this;
        }
    }

    public void setRightNode(AVLNode<E> temp) {
        this.right = temp;
        if (temp != null) {
            this.right.parent = this;
        }
    }

    public String toString() {
        return assemble(this, 0);
    }

    private int height() {
        int leftHeight = (left == null) ? 0 : left.height();
        int rightHeight = (right == null) ? 0 : right.height();
        return 1 + Math.max(leftHeight, rightHeight);
    }

    private String assemble(AVLNode<E> temp, int offset) {
        String ret = "";
        for (int i = 0; i < offset; i++) {
            ret += "\t";
        }
        ret += temp.getElement() + "\n";
        if (temp.getLeft() != null) {
            ret += "Left: " + assemble(temp.getLeft(), offset + 1);
        }
        if (temp.getRight() != null) {
            ret += "Right: " + assemble(temp.getRight(), offset + 1);
        }
        return ret;
    }
}