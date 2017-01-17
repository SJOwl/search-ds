/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr&iacute;guez
 *
 * Programming Assignment #4
 *
 * Implement a red black tree
 *
 * Satshabad Khalsa
 */

package proj4;

/**
 * This class holds a node and it's current depth
 * 
 * @author Satshabad
 *
 * @param <K> the type of key held by the node
 * @param <V> the type of value to be held by the node
 */
public class DepthAndNode<E extends Comparable<E>> {

    /**
     * the depth of the held node
     */
    private int depth;
    
    /**
     * a node 
     */
    private Node<E> node;
    
    /**
     * creates a node and depth object 
     * 
     * @param dep the depth of the node
     * @param n the node
     */
    public DepthAndNode(int dep, Node<E> n) {
        node = n;
        depth = dep;
    }
    
    /**
     * gets the depth
     * @pre true
     * @post the depth is returned
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * gets the node
     * @pre true
     * @post the node is returned
     * @return the node
     */
    public Node<E> getNode() {
        return node;
    }

    /**
     * Sets the depth of the current node.
     * @pre true
     * @post the depth is set
     * 
     * @param depth
     *            the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    

    public String toString() {
        if (node != null) {
            return node.toString();
        } else {
            return "DaN null";
        }

    }
}
