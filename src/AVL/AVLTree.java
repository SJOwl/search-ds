package AVL;

import ru.mail.polis.BTreePrinter;
import ru.mail.polis.ISortedSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

    private Comparator<E> comparator;
    private AVLNode<E> rootAbove;
    private int size;

    public AVLTree(Comparator<E> comparator) {
        this();
        this.comparator = comparator;
    }

    public AVLTree() {
        this.comparator = null;
        rootAbove = new AVLNode<E>();
    }

    @Override
    public E first() {
        AVLNode<E> node = rootAbove;
        if (node == null) {
            return null;
        }
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.getElement();
    }

    @Override
    public E last() {
        AVLNode<E> node = rootAbove.getLeft();
        if (node == null) {
            return null;
        }
        while (node.getRight() != null
                && node.getElement() != null) {
            node = node.getRight();
        }
        return node.getElement();
    }

    // todo
    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<E>();
        if (rootAbove != null) {
            inorderTraverseRL(rootAbove.getLeft(), list);
        }
        return list;
    }

    public AVLNode<E> getRootAbove() {
        return rootAbove;
    }

    /**
     * @param element: The element to insert into the Tree
     *                 <p>
     *                 This method invokes a private helper method to insert the element.
     *                 It passes the root as the place to start.
     */
    public void insert(E element) {
        insert(element, rootAbove.getLeft());
    }

    public void printTree() {
        if (rootAbove == null) {
            // System.out.println("rootAbove: null");
            return;
        }
        BTreePrinter.printNode(rootAbove.getLeft());
    }

    /**
     * @param rotateBase: The root of the subtree that is being rotated
     * @param rootAbove:  The AVLNode that points to rotateBase
     *                    <p>
     *                    This method rotates the subtree balancing it to within a margin of |1|.
     */
    public void rotate(AVLNode<E> rotateBase, AVLNode<E> rootAbove) {
        int balance = rotateBase.getBalance();
        //System.out.println("Balance is "+balance);
        if (Math.abs(balance) < 2) {
            //System.out.println("No rotate");
        }
        //gets the child on the side with the greater height
        AVLNode<E> child = (balance < 0) ? rotateBase.getLeft() : rotateBase.getRight();
        if (child == null) {
            return;
        }
        int childBalance = child.getBalance();
        //System.out.println("child balance: "+childBalance);
        AVLNode<E> grandChild = null;
        //both the child and grandchild are on the
        //left side, so rotate the child up to the root position
        if (balance < -1 && childBalance <= 0) {
            if (rootAbove != this.rootAbove && rootAbove.getRight() == rotateBase) {
                rootAbove.setRightNode(child);
            } else {
                rootAbove.setLeftNode(child);
            }
            grandChild = child.getRight();
            child.setRightNode(rotateBase);
            rotateBase.setLeftNode(grandChild);
            return;
        }
        //both the child and the grandchild are on the
        //right side, so rotate the child up to the root position
        else if (balance > 1 && childBalance >= 0) {
            if (rootAbove != this.rootAbove && rootAbove.getRight() == rotateBase) {
                rootAbove.setRightNode(child);
            } else {
                rootAbove.setLeftNode(child);
            }
            grandChild = child.getLeft();
            child.setLeftNode(rotateBase);
            rotateBase.setRightNode(grandChild);
            return;
        }
        //the child is on the left side, but the grandchild is on the
        //right side, so rotate the grandchild up to the child position
        //so the condition of the first if statement is satisfied,
        //then recurse to have the first if statement evaluated
        else if (balance < -1 && childBalance >= 0) {
            grandChild = child.getRight();
            rotateBase.setLeftNode(grandChild);
            child.setRightNode(grandChild.getLeft());
            grandChild.setLeftNode(child);
            rotate(rotateBase, rootAbove);
            return;
        }
        //the child is on the right side, but the grandchild is on the
        //left side, so rotate the grandchild up to the child position
        //so the condition of the second if statement is satisfied,
        //then recurse to have the second if statement evaluated
        else if (balance > 1 && childBalance <= 0) {
            grandChild = child.getLeft();
            rotateBase.setRightNode(grandChild);
            child.setLeftNode(grandChild.getRight());
            grandChild.setRightNode(child);
            rotate(rotateBase, rootAbove);
            return;
        }
    }

    // todo
    @Override
    public int size() {
        return this.size;
    }

    // todo
    @Override
    public boolean isEmpty() {
        if (size > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            System.out.println("null value in contains(value)!");
            return false;
        }
        if (rootAbove != null && rootAbove.getLeft() != null) {
            AVLNode<E> curr = rootAbove.getLeft();
            int cmp;
            while (curr != null) {
                cmp = compare(curr.getElement(), value);
                if (cmp == 0) {
                    return true;
                } else if (cmp < 0) {
                    curr = curr.getRight();
                } else {
                    curr = curr.getLeft();
                }
            }
        }
        return false;
    }

    // todo
    @Override
    public boolean add(E value) {
        if (insert(value, rootAbove.getLeft()) == true) {
            size++;
            // System.out.println("value " + value + " is added");
            return true;
        }
        // System.out.println("value " + value + " is already in the Tree");
        return false;
    }

    // todo
    @Override
    public boolean remove(E value) {
        if (rootAbove == null) {
            return false;
        }
        if (remove(value, rootAbove.getLeft()) == true) {
            size--;
            // System.out.println("value " + value + " is removed");
            return true;
        }
        // System.out.println("Tree doesn't contain " + value);
        return false;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    @Override
    public String toString() {
        List<E> list = inorderTraverse();
        return list.toString();
    }

    /**
     * @param value: The element to insert into the Tree
     * @param temp:  The AVLNode to evaluate for recursive insertion
     *               <p>
     *               This method recursively traverses the Tree, inserting the
     *               element at the appropriate spot and incrementing the balance
     *               factors for the subtrees as it evaluates. The Tree will then
     *               recursively rebalance as necessary.
     */
    private boolean insert(E value, AVLNode<E> temp) {
        if (value == null) {
            System.out.println("null in insert value!");
            return false;
        }
        if (this.rootAbove.getLeft() == null) {
            this.rootAbove.setLeftNode(new AVLNode<E>(value));
            return true;
        }
        //travel left or right based on the
        //comparison of element to temp.element
        //remember that left means that element <= temp.element
        //and right means element > temp.element
        int cmp =compare(value, temp.getElement());
        // only unique elements in the tree
        if (cmp == 0) {
            return false;
        }
        while (true) {
            cmp =compare(value, temp.getElement());
            if (cmp == 0) {
                return false;
            }
            if (cmp > 0) {
                if (temp.getRight() != null) {
                    temp = temp.getRight();
                } else {
                    temp.setRight(value);
                    reBalance(temp);
                    break;
                }
            } else if (cmp < 0) {
                if (temp.getLeft() != null) {
                    temp = temp.getLeft();
                } else {
                    temp.setLeft(value);
                    reBalance(temp);
                    break;
                }
            }
        }
        return true;
    } //end insert

    /**
     * @param element: The element to remove from the AVLTree
     * @param temp:    The root node of the subtree
     *                 <p>
     *                 This method recursively traverses the AVLTree based on
     *                 the ordering of the element with respect to the Tree's
     *                 elements. If the element is not found, then nothing happens.
     *                 Otherwise, the element is removed, and either the far-right
     *                 element on its left child or the far left element on its right
     *                 child replaces it.
     ***/
    private boolean remove(E element, AVLNode<E> temp) {
        if (element == null) {
            throw new NullPointerException("element is null");
        }
        if (rootAbove == null) {
            return false;
        }
        if (rootAbove.getLeft() == null) {
            return false;
        }
        AVLNode<E> parent = rootAbove.getLeft();
        AVLNode<E> curr = rootAbove.getLeft();
        int cmp;
        while ((cmp = compare(curr.getElement(), element)) != 0) {
            parent = curr;
            if (cmp > 0) {
                curr = curr.getLeft();
            } else {
                curr = curr.getRight();
            }
            if (curr == null) {
                return false; // ничего не нашли
            }
        }
        if (curr.getLeft() != null && curr.getRight() != null) {
            AVLNode<E> next = curr.getRight();
            AVLNode<E> pNext = curr;
            while (next.getLeft() != null) {
                pNext = next;
                next = next.getLeft();
            } //next = наименьший из больших
            curr.setElement(next.getElement());
            next.setElement(null);
            //у правого поддерева нет левых потомков
            if (pNext == curr) {
                curr.setRightNode(next.getRight());
            } else {
                pNext.setLeftNode(next.getRight());
            }
            next.setRightNode(null);
        } else {
            if (curr.getLeft() != null) {
                reLink(parent, curr, curr.getLeft());
                //reBalance(curr.getLeft());
            } else if (curr.getRight() != null) {
                reLink(parent, curr, curr.getRight());
                //reBalance(curr.getRight());
            } else {
                reLink(parent, curr, null);
            }
        }
        reBalance(parent);
        return true;
    }

    private void reLink(AVLNode<E> parent, AVLNode<E> curr, AVLNode<E> child) {
        if (parent == curr) {
            rootAbove.setLeftNode(child);
        } else if (parent.getLeft() == curr) {
            parent.setLeftNode(child);
        } else {
            parent.setRightNode(child);
        }
        curr.setElement(null);
    }

    private AVLNode<E> getNode(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (rootAbove.getLeft() != null) {
            AVLNode<E> curr = rootAbove.getLeft();
            while (curr != null) {
                int cmp = compare(curr.getElement(), value);
                if (cmp == 0) {
                    return curr;
                } else if (cmp < 0) {
                    curr = curr.getRight();
                } else {
                    curr = curr.getLeft();
                }
            }
        }
        return null;
    }

    private void inorderTraverseRL(AVLNode<E> node, List<E> list) {
        if (node != null) {
            inorderTraverseRL(node.getLeft(), list);
            list.add(node.getElement());
            inorderTraverseRL(node.getRight(), list);
        }
    }

    /**
     * @param parent:    The parent of the element to be removed
     * @param remove:    The element to remove from the Tree
     * @param direction: false if remove is to the left of parent, true otherwise
     *                   <p>
     *                   This method physically removes the AVLNode with the element from the
     *                   AVLTree, replacing it with the appropriate successor.
     ***/
    private void enactRemoval(AVLNode<E> parent, AVLNode<E> remove, boolean direction) {
        AVLNode<E> temp = null;
        AVLNode<E> left = remove.getLeft();
        AVLNode<E> right = remove.getRight();
        //if the Node to remove is not a leaf, find the appropriate successor
        if (left != null || right != null) {
            temp = findSuccessor(remove);
        }
        //if remove is the right child of parent, update parent's right node
        if (direction && (parent != rootAbove)) {
            parent.setRightNode(temp);
        }
        //otherwise, update its left node with the successor
        else {
            parent.setLeftNode(temp);
        }
        //and update temp to point to remove's children
        if (temp != null) {
            if (temp != left) {
                temp.setLeftNode(remove.getLeft());
            }
            if (temp != right) {
                temp.setRightNode(remove.getRight());
            }
        }
        //and finally, discard those references from remove
        //so that the removed Node is garbage collected sooner
        remove.setLeftNode(null);
        remove.setRightNode(null);
    }

    /**
     * @param root: The element for which to find a successor AVLNode
     * @return AVLNode<E>: The successor Node
     ***/
    private AVLNode<E> findSuccessor(AVLNode<E> root) {
        AVLNode<E> temp = root;
        AVLNode<E> parent = null;
        //if the balance favors the right, traverse right
        //otherwise, traverse left
        boolean direction = (temp.getBalance() > 0);
        parent = temp;
        temp = (direction) ? temp.getRight() : temp.getLeft();
        if (temp == null) {
            return temp;
        }
        //and find the farthest left-Node on the right side,
        //or the farthest right-Node on the left side
        while ((temp.getRight() != null && !direction) ||
                (temp.getLeft() != null && direction)) {
            parent = temp;
            temp = (direction) ? temp.getLeft() : temp.getRight();
        }
        //finally, update the successor's parent's references
        //to adjust for a left child on the right node, or a right
        //child on the left-node
        if (temp == parent.getLeft()) {
            parent.setLeftNode(temp.getRight());
            temp.setRightNode(null);
        } else {
            parent.setRightNode(temp.getLeft());
            temp.setLeftNode(null);
        }
        return temp;
    }

    private void reBalance(AVLNode<E> node) {
        if (node.getParent() == null) {
            return;
        }
        //        System.out.println("Before balancing: ");
        //        this.printTree();
        while (node != rootAbove) {
            //System.out.println("ROTATE " + node.getElement());
            rotate(node, node.getParent());
            node = node.getParent();
        }
        //        System.out.println("After balancing: ");
        //        this.printTree();
    }
}