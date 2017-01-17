package proj4;

public class Node<E extends Comparable<E>> {

    /**
     * The right child of this node
     */
    private Node<E> rightChild;
    /**
     * The left child of this node
     */
    private Node<E> leftChild;
    /**
     * The parent of this node
     */
    private Node<E> parent;
    /**
     * The color value of this node, if true, red, if false, black
     */
    private boolean red;
    /**
     * The value held by this node
     */
    private E value;

    /**
     * Initializes this node with the given attributes
     * 
     * @pre true
     * @post the node is created
     * 
     * 
     * @param color
     *            The color value of this node, if true, red, if false, black
     * @param leftChild
     *            The left child of this node
     * @param rightChild
     *            The right child of this node
     * @param parent
     *            The parent of this node
     * @param value
     *            The value held by this node
     * @param key
     *            The key associated with the value held by this node
     */
    public Node(boolean color, Node<E> leftChild, Node<E> rightChild,
            Node<E> parent, E value) {
        red = color;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        this.value = value;
    }

    /**
     * Gets The right child of this node
     * 
     * @pre true
     * @post the right child of this node is returned
     * 
     * @return the rightChild
     */
    public Node<E> getRightChild() {
        return rightChild;
    }

    /**
     * Sets The right child of this node
     * 
     * @pre true
     * @post the right child of this node is one that was passed in
     * 
     * @param rightChild
     *            the rightChild of this node to set
     */
    public void setRightChild(Node<E> rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Gets The left child of this node
     * 
     * @pre true
     * @post the left child of this node is returned
     * @return the leftChild
     */
    public Node<E> getLeftChild() {
        return leftChild;
    }

    /**
     * Sets The left child of this node
     * 
     * @pre true
     * @post the left child of this node is one that was passed in
     * 
     * @param leftChild
     *            the leftChild of this node to set
     */
    public void setLeftChild(Node<E> leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Gets the parent of this node
     * 
     * @pre true
     * @post the parent of this node is returned
     * 
     * @return the parent
     */
    public Node<E> getParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node
     * 
     * @pre true
     * @post the parent of this node is now the node that was passed in
     * 
     * @param parent
     *            the parent of this node to set
     */
    public void setParent(Node<E> parent) {
        this.parent = parent;
    }

    /**
     * Checks what the current color of this node is, true for red, false for
     * black.
     * 
     * @pre true
     * @post the color is returned
     * 
     * @return the color of the node
     */
    public boolean isRed() {
        return red;
    }

    /**
     * Sets the color of this node, true for red, false for black
     * 
     * @pre true
     * @post The color of this node is set to the boolean specified
     * 
     * @param red
     *            the color (true for red) to set
     */
    public void setColor(boolean red) {
        this.red = red;
    }

    /**
     * Gets the value held by this node
     * 
     * @pre true
     * @post the value is returned
     * 
     * @return the value
     */
    public E getValue() {
        return value;
    }
}
