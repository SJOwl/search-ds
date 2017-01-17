package proj4;

import ru.mail.polis.ISortedSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    /**
     * The constant boolean for the color red
     */
    private final boolean RED = true;
    /**
     * The constant boolean for the color black
     */
    private final boolean BLACK = false;
    private final Comparator<E> comparator;
    /**
     * This is the length of the longest string representation of the element
     */
    private int longestString;
    /**
     * The one node that represents all of the nil leaves at once.
     */
    private Node<E> theNilLeaf;
    private int size;
    /**
     * A special node called the root
     */
    public Node<E> root;

    /**
     * Initializes a Red Black Tree
     *
     * @pre true
     * @post a RBT is made
     */
    public RedBlackTree() {
        root = null;
        theNilLeaf = new Node<E>(BLACK, null, null, null, null);
        longestString = 0;
        comparator = null;
        size=0;
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
        root = null;
        theNilLeaf = new Node<E>(BLACK, null, null, null, null);
        longestString = 0;
        size=0;
    }

    /**
     * This method deletes the first instance of the element value mapping indicated
     * by the element.
     *
     * @param element the element to value to be deleted
     * @return the value mapped to the element which was deleted
     * @post the mapping to be deleted is valid
     * @post the first instance of the mapping is removes
     */
    public boolean delete(E element) {
        // the tree is empty
        if (root == null) {
            return false;
        }
        // find node to be deleted
        Node<E> toBeDeleted = lookupHelper(element, root);
        // if this is null the node we want to delete doesn't exists
        if (toBeDeleted == null) {
            return false;
        }
        // if the root is the only node
        if (toBeDeleted == root && root.getRightChild() == theNilLeaf
                && root.getLeftChild() == theNilLeaf) {
            root = null;
            return false;
        }
        // this will tell if using the successor or predecessor
        boolean successor = false;
        Node<E> replaceNode;
        if (toBeDeleted.getRightChild() == theNilLeaf
                && toBeDeleted.getLeftChild() == theNilLeaf) {
            replaceNode = toBeDeleted;
        } else if (toBeDeleted.getRightChild() != theNilLeaf) {
            successor = true;
            replaceNode = findInOrderSuccessor(toBeDeleted.getRightChild());
        } else {
            replaceNode = findInOrderPredecessor(toBeDeleted.getLeftChild());
        }
        // this should never happen because we know the node to be deleted
        // exists
        assert (replaceNode != null);
        Node<E> nodeNeedingBalance;
        if (replaceNode.isRed()) {
            // if the node to be deleted is red than both of it's children
            // must be leaves because at least one of it's children is a leaf
            // otherwise it wouldn't be a successor or predecessor. Both child
            // must leaves or it it would violate the black rule.
            assert (replaceNode.getRightChild() == theNilLeaf && replaceNode
                    .getLeftChild() == theNilLeaf);
            // replace the node to with a nil leaf
            if (replaceNode.getParent().getRightChild() == replaceNode) {
                replaceNode.getParent().setRightChild(theNilLeaf);
            } else {
                replaceNode.getParent().setLeftChild(theNilLeaf);
            }
            return true;
        } else {
            if (successor) {
                // if we have the successor to delete then the left child is
                // definitely going to be a leaf
                assert (replaceNode.getLeftChild() == theNilLeaf);
                // in some cases the in order successor will be a right child
                // and in some it will be the left
                if (replaceNode.getParent().getRightChild() == replaceNode) {
                    replaceNode.getParent().setRightChild(
                            replaceNode.getRightChild());
                } else {
                    replaceNode.getParent().setLeftChild(
                            replaceNode.getRightChild());
                }
                replaceNode.getRightChild().setParent(replaceNode.getParent());
                nodeNeedingBalance = replaceNode.getRightChild();
                replaceNode.setRightChild(null);
                replaceNode.setLeftChild(null);
                replaceNode.setParent(null);
            } else {
                // if we have the predecessor to delete then the right child is
                // definitely going to be a leaf
                assert (replaceNode.getRightChild() == theNilLeaf);
                // in some cases the in order pred will be a right child
                // and in some it will be the left
                if (replaceNode.getParent().getRightChild() == replaceNode) {
                    replaceNode.getParent().setRightChild(
                            replaceNode.getLeftChild());
                } else {
                    replaceNode.getParent().setLeftChild(
                            replaceNode.getLeftChild());
                }
                replaceNode.getLeftChild().setParent(replaceNode.getParent());
                nodeNeedingBalance = replaceNode.getLeftChild();
                System.out.println(" n's parent's parent: "
                                           + nodeNeedingBalance.getParent().getParent());
                System.out.println(" n's parent: "
                                           + nodeNeedingBalance.getParent());
                System.out.println(" n:" + nodeNeedingBalance);
                replaceNode.setRightChild(null);
                replaceNode.setLeftChild(null);
                replaceNode.setParent(null);
            }
            if (nodeNeedingBalance.isRed()) {
                nodeNeedingBalance.setColor(BLACK);
                System.out
                        .println("case 0: child of replacer was red, replacer was black, repainted child black");
                System.out.println(" n's parent's parent: "
                                           + nodeNeedingBalance.getParent().getParent());
                System.out.println(" n's parent: "
                                           + nodeNeedingBalance.getParent());
                System.out.println(" n:" + nodeNeedingBalance);
            } else {
                deleteBalance(nodeNeedingBalance);
            }
            System.out.println("BEFORE REBALANCE-------------------------");
            prettyPrint();
        }
        return true;
    }

    @Override
    public E first() {
        Node<E> node = root;
        if(root==null) return null;
        while (node.getLeftChild() != theNilLeaf) {
            node = node.getLeftChild();
        }
        return node.getValue();
    }

    @Override
    public String toString() {
        List<E> list = inorderTraverse();
        return list.toString();
    }

    @Override
    public E last() {
        Node<E> node = root;
        if (node == null) {
            return null;
        }
        while (node.getRightChild() != theNilLeaf
                && node.getValue() != theNilLeaf) {
            node = node.getRightChild();
        }
        return node.getValue();
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<E>();
        inorderTraverseRL(root, list);
        return list;
    }

    /**
     * This method looks up the value associated with the element.
     *
     * @param element the element of the value
     * @return the value indexed by element
     * @pre the mapping must be valid
     * @post the value mapped to the element is returned
     */
    public Node<E> lookup(E element) {
        return lookupHelper(element, root);
    }

    /**
     * This prints the tree in a readable format, regardless of element string
     * length
     *
     * @pre true
     * @post the tree is printed
     */
    public void prettyPrint() {
        ArrayList<DepthAndNode<E>> queue = new ArrayList<>();
        int depth = 0;
        int treeDepth = getTreeDepth(root);
        int beginningSpaces = longestString + 2;
        for (int i = 0; i < treeDepth - 2; i++) {
            beginningSpaces = (beginningSpaces * 2) + longestString + 2;
        }
        int inBetweenSpaces = beginningSpaces;
        queue.add(0, new DepthAndNode<E>(depth, root));
        DepthAndNode<E> m = null;
        for (int i = 0; i < beginningSpaces; i++) {
            System.out.print(" ");
        }
        while (depth < treeDepth) {
            m = queue.remove(0);
            if (m.getDepth() > depth) {
                depth = m.getDepth();
                if (!(depth < treeDepth)) {
                    break;
                }
                System.out.println();
                inBetweenSpaces = beginningSpaces;
                beginningSpaces = ((beginningSpaces - (longestString + 2)) / 2);
                for (int i = 0; i < beginningSpaces; i++) {
                    System.out.print(" ");
                }
            }
            Node<E> right;
            Node<E> left;
            if (m.getNode() == null) {
                right = null;
                left = null;
            } else {
                right = m.getNode().getRightChild();
                left = m.getNode().getLeftChild();
            }
            queue.add(queue.size(), new DepthAndNode<E>(depth + 1, left));
            queue.add(queue.size(), new DepthAndNode<E>(depth + 1, right));
            if (m.getNode() == null) {
                for (int j = 0; j < longestString + 2; j++) {
                    System.out.print(" ");
                }
            } else if (m.getNode() == theNilLeaf) {
                for (int j = 0; j < longestString; j++) {
                    System.out.print("N");
                }
                System.out.print(":B");
            } else {
                System.out.print(m.getNode().getValue());
                for (int j = m.getNode().getValue().toString().length(); j < longestString; j++) {
                    System.out.print("E");
                }
                if (m.getNode().isRed()) {
                    System.out.print(":R");
                } else {
                    System.out.print(":B");
                }
            }
            for (int k = 0; k < inBetweenSpaces; k++) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * This method helped me visualize the tree until I got pretty print working
     *
     * @pre true
     * @post the tree is printed sideways
     * @deprecated
     */
    public void printTree() {
        internalInOrderPrint(root, 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(E element) {
        if (lookup(element) != null) {
            return true;
        }
        return false;
    }

    /**
     * This method adds the mapping to the tree.
     *
     * @param element the object to be stored
     * @return whether the mapping was made or not
     * @pre true
     * @post the element added matches the value added with it. If there was a
     * previous mapping of the same element results are not guaranteed
     */
    public boolean add(E element) {
        // don't add null mapping, it could interfere with other methods.
        if (element == null) {
            return false;
        }
        if (element.toString().length() > longestString) {
            longestString = element.toString().length();
        }
        // special case when there is no root.
        if (root == null) {
            root = new Node<E>(BLACK, theNilLeaf, theNilLeaf, null, element);
            root.getLeftChild().setParent(root);
            root.getRightChild().setParent(root);
            size++;
            return true;
        }
        // finds the node on which to add the new node
        Node<E> placeToAdd = traverseForAdd(element, root);
        // the method traverseToAdd should never return null.
        assert (placeToAdd != null);
        // add as if it were a binary tree, greater than to the right, less or
        // equal to the left. Except now after adding we have to make sure that
        // the tree is still following it's conditions
        if (placeToAdd.getValue().compareTo(element) < 0) {
            placeToAdd.setRightChild(new Node<E>(RED, theNilLeaf,
                                                 theNilLeaf, placeToAdd, element));
            addBalance(placeToAdd.getRightChild());
        } else {
            placeToAdd.setLeftChild(new Node<E>(RED, theNilLeaf, theNilLeaf,
                                                placeToAdd, element));
            addBalance(placeToAdd.getLeftChild());
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(E value) {
        boolean res=delete(value);
        if(res){
            size--;
            return true;
        }
        return false;
    }

    /**
     * This is a method I was working on to help verify the tree, I got it to
     * catch red violations, but not black. Often it will throw a black
     * violation even though there is none
     *
     * @return the number of black nodes in any given path
     * @pre true
     * @post false
     */
    public int verify() {
        return verifyHelper(root, 1, false, -1);
    }

    private void inorderTraverseRL(Node<E> node, List<E> list) {
        if (node != theNilLeaf && node!=null) {
            inorderTraverseRL(node.getLeftChild(), list);
            list.add(node.getValue());
            inorderTraverseRL(node.getRightChild(), list);
        }
    }

    /**
     * This internal method will check the 5 cases that might occur when a node
     * is added and it breaks the 4 RBT rules.
     *
     * @param currentNode the node to start checking on
     * @pre the only rules that are broken are from the latest add proc.
     * @post all 4 of the RBT rules are not violated
     */
    private void addBalance(Node<E> currentNode) {
        // first case is when the root is not black. so color it black.
        if (currentNode == root) {
            currentNode.setColor(BLACK);
            // System.out.println("case 1");
            return;
        }
        Node<E> parent = currentNode.getParent();
        // if the node is not the root then it should never have a null parent.
        assert (parent != null);
        // if parent is black no problem.
        if (!parent.isRed()) {
            // System.out.println("case 2");
            return;
        }
        // The parent should always be red here
        assert (parent.isRed());
        Node<E> grandparent = parent.getParent();
        Node<E> uncle = getUncle(currentNode);
        // if the uncle and parent are red, re-color both to black and re-color
        // gramp to red and call on gramp.
        if (uncle.isRed()) {
            // System.out.println("case 3");
            uncle.setColor(BLACK);
            parent.setColor(BLACK);
            grandparent.setColor(RED);
            // printTree();
            // System.out.println("%%%%");
            addBalance(currentNode.getParent().getParent());
            // cases 4 and 5 involve rotations.
        } else if (!uncle.isRed()) {
            // case 4a and 4b simply set up for case 5a and 5b.
            if (parent.getRightChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                // System.out.println("case 4a");
                rotateLeft(parent);
                // reset all the helpful variables
                currentNode = currentNode.getLeftChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                // printTree();
                // System.out.println("%%%%");
            } else if (parent.getLeftChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                // System.out.println("case 4b");
                rotateRight(parent);
                // reset all the helpful variables
                currentNode = currentNode.getRightChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                // printTree();
                // System.out.println("%%%%");
            }
            // if either of these cases occur then we are done
            if (parent.getLeftChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                // System.out.println("case 5a");
                rotateRight(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);
            } else if (parent.getRightChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                // System.out.println("case 5b");
                rotateLeft(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);
            }
        }
    }

    /**
     * This internal method will check the 5 cases that might occur when a node
     * is deleted and it breaks the 4 RBT rules.
     *
     * @param node the node to start checking on
     * @pre the only rules that are broken are from the latest delete proc.
     * @post all 4 of the RBT rules are not violated
     */
    private void deleteBalance(Node<E> node) {
        if (node == root) {
            return;
        }
        Node<E> parent = node.getParent();
        Node<E> sibling = getSibling(node);
        if (sibling == theNilLeaf) {
            return;
        }
        System.out.println("we's left child ="
                                   + node.getParent().getLeftChild());
        // Case 2 the sibling is red
        if (sibling.isRed()) {
            System.out
                    .println("Case 2: the sibling is RED, set sibling to BLACK "
                                     + "and parent to BLACK, rotate left if left child "
                                     + "rotate right if right child, n = "
                                     + node.getValue());
            prettyPrint();
            System.out
                    .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            sibling.setColor(BLACK);
            parent.setColor(BLACK);
            if (node == parent.getLeftChild()) {
                rotateLeft(parent);
            } else {
                rotateRight(parent);
            }
            sibling = getSibling(node);
            parent = node.getParent();
        }
        // Case 3 4 5 and 6
        // the sibling is black
        if (!sibling.isRed()) {
            // Case 3
            if (!sibling.getRightChild().isRed()
                    && !sibling.getLeftChild().isRed()) {
                if (!parent.isRed()) {
                    System.out
                            .println("Case 3: sibling is BLACK, sibling's chilren are BLACK, "
                                             + "parent is BLACK. set sibling to RED and call again on parent, n = "
                                             + node.getValue());
                    sibling.setColor(RED);
                    prettyPrint();
                    System.out
                            .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    deleteBalance(parent);
                }
                // Case 3
                else {
                    System.out
                            .println("Case 4: sibling is BLACK, sibling's chilren are BLACK, "
                                             + "parent is RED. set sibling to RED set parent to BLACK, n = "
                                             + node.getValue());
                    sibling.setColor(RED);
                    parent.setColor(BLACK);
                    prettyPrint();
                    System.out
                            .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    return;
                }
            }
            // Sibling is still black but it's children aren't both black
            // Case 5
            if (node == parent.getLeftChild()
                    && !sibling.getRightChild().isRed()
                    && sibling.getLeftChild().isRed()) {
                System.out
                        .println("Case 5: sibling is BLACK, sib's right child is BLACK, "
                                         + "sib's left child is RED. set the sibling to RED, sib's left child to BLACK. rotate right in sib. n = "
                                         + node.getValue());
                sibling.setColor(RED);
                sibling.getLeftChild().setColor(BLACK);
                rotateRight(sibling);
                prettyPrint();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            } else if (node == parent.getRightChild()
                    && !sibling.getLeftChild().isRed()
                    && sibling.getRightChild().isRed()) {
                System.out
                        .println("Case 5: sibling is BLACK, sib's right child is RED, "
                                         + "sib's left child is BLACK. set the sibling to RED, sib's right child to BLACK. rotate left on sib. n = "
                                         + node.getValue());
                sibling.setColor(RED);
                sibling.getRightChild().setColor(BLACK);
                rotateLeft(sibling);
                prettyPrint();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
            if (node == theNilLeaf) {
                node.setParent(parent);
            }
            sibling = getSibling(node);
            if (sibling == theNilLeaf) {
                return;
            }
            // Case 6
            if (node == parent.getLeftChild()
                    && sibling.getRightChild().isRed()) {
                boolean temp = parent.isRed();
                parent.setColor(sibling.isRed());
                sibling.setColor(temp);
                System.out.println("Case 6a: n = " + node.getValue());
                sibling.getRightChild().setColor(BLACK);
                rotateLeft(parent);
                prettyPrint();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            } else if (node == parent.getRightChild()
                    && sibling.getLeftChild().isRed()) {
                boolean temp = parent.isRed();
                parent.setColor(sibling.isRed());
                sibling.setColor(temp);
                sibling.getLeftChild().setColor(BLACK);
                rotateRight(parent);
                System.out.println("Case 6b: n = " + node.getValue());
                prettyPrint();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
        }
    }

    /**
     * This method returns the sibling of the node
     *
     * @param node the node of who's sibling is to be returned
     * @return the sibling of node
     * @pre the node is not the root, the node has a sibling
     * @post the sibling is returned
     */
    private Node<E> getSibling(Node<E> node) {
        if (node.getParent().getRightChild() == node) {
            return node.getParent().getLeftChild();
        } else {
            return node.getParent().getRightChild();
        }
    }

    /**
     * Performs a right rotation on the node
     *
     * @param node the node to rotate on
     * @pre the node called on must not be a node with less than 2 levels of
     * children below it.
     * @post a left rotation is performed
     */
    private void rotateRight(Node<E> node) {
        if (node != root) {
            if (node.getParent().getLeftChild() == node) {
                node.getLeftChild().setParent(node.getParent());
                node.getParent().setLeftChild(node.getLeftChild());
                Node<E> nodesOldLeftChild = node.getLeftChild();
                node.setLeftChild(node.getLeftChild().getRightChild());
                node.getLeftChild().setParent(node);
                nodesOldLeftChild.setRightChild(node);
                node.setParent(nodesOldLeftChild);
                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldLeftChild);
            } else if (node.getParent().getRightChild() == node) {
                node.getLeftChild().setParent(node.getParent());
                node.getParent().setRightChild(node.getLeftChild());
                Node<E> nodesOldLeftChild = node.getLeftChild();
                node.setLeftChild(node.getLeftChild().getRightChild());
                node.getLeftChild().setParent(node);
                nodesOldLeftChild.setRightChild(node);
                node.setParent(nodesOldLeftChild);
                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldLeftChild);
            }
        } else {
            node.getLeftChild().setParent(null);
            root = node.getLeftChild();
            node.setLeftChild(node.getLeftChild().getRightChild());
            node.getLeftChild().setParent(node);
            root.setRightChild(node);
            node.setParent(root);
            // after rotation node should be child of root
            assert (node.getParent() == root);
        }
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    /**
     * Performs a left rotation on the node
     *
     * @param node the node to rotate on
     * @pre the node called on must not be a node with less than 2 levels of
     * children below it.
     * @post a left rotation is performed
     */
    private void rotateLeft(Node<E> node) {
        if (node != root) {
            if (node.getParent().getLeftChild() == node) {
                node.getParent().setLeftChild(node.getRightChild());
                node.getRightChild().setParent(node.getParent());
                Node<E> nodesOldRightChild = node.getRightChild();
                node.setRightChild(node.getRightChild().getLeftChild());
                node.getRightChild().setParent(node);
                nodesOldRightChild.setLeftChild(node);
                node.setParent(nodesOldRightChild);
                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldRightChild);
            } else if (node.getParent().getRightChild() == node) {
                node.getParent().setRightChild(node.getRightChild());
                node.getRightChild().setParent(node.getParent());
                Node<E> nodesOldRightChild = node.getRightChild();
                node.setRightChild(node.getRightChild().getLeftChild());
                node.getRightChild().setParent(node);
                nodesOldRightChild.setLeftChild(node);
                node.setParent(nodesOldRightChild);
                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldRightChild);
            }
        } else {
            node.getRightChild().setParent(null);
            root = node.getRightChild();
            node.setRightChild(node.getRightChild().getLeftChild());
            node.getRightChild().setParent(node);
            root.setLeftChild(node);
            node.setParent(root);
            // after rotation node should be child of root
            assert (node.getParent() == root);
        }
    }

    /**
     * Finds the uncle of the given node
     *
     * @param currentNode the node to find the uncle of.
     * @return the uncle of currentNode
     * @pre the passed node is not the root
     * @post the uncle of the node is returned
     */
    private Node<E> getUncle(Node<E> currentNode) {
        // this method should never be called on a node without a parent. i. e.
        // the root
        assert (currentNode.getParent() != null);
        Node<E> parent = currentNode.getParent();
        // just making sure that the root is black
        if (parent.getParent() == null) {
            assert (!parent.isRed());
        }
        if (parent.getParent().getLeftChild() == parent) {
            return parent.getParent().getRightChild();
        } else if (parent.getParent().getRightChild() == parent) {
            return parent.getParent().getLeftChild();
        }
        // we must have returned by now.
        assert (false);
        return null;
    }

    /**
     * The method find the node onto which the new node will be added
     *
     * @param element   the element of the node to be added
     * @param startNode the node to currently look at for adding
     * @return the node to add onto.
     * @pre true
     * @post the node that should be added onto is returned. it is up to caller
     * to determine which side to add on to.
     */
    private Node<E> traverseForAdd(E element, Node<E> startNode) {
        /* There are four cases of adding, I enumerated all of them one by one. */
        Node<E> left = startNode.getLeftChild();
        Node<E> right = startNode.getRightChild();
        // if both leave are null this is the place to add
        if (left == theNilLeaf && right == theNilLeaf) {
            return startNode;
        }
        // if the left leaf is null we may add here or keep going on the right
        // path
        if (left == theNilLeaf && right != theNilLeaf) {
            if (startNode.getValue().compareTo(element) > -1) {
                return startNode;
            } else {
                return traverseForAdd(element, right);
            }
        }
        // if the right leaf is null we may add here or keep going on the left
        // path
        if (right == theNilLeaf && left != theNilLeaf) {
            if (startNode.getValue().compareTo(element) < 0) {
                return startNode;
            } else {
                return traverseForAdd(element, left);
            }
        }
        // if neither leaves are null then we check to see which way we should
        // go.
        if (left != theNilLeaf && right != theNilLeaf) {
            if (startNode.getValue().compareTo(element) < 0) {
                return traverseForAdd(element, right);
            } else {
                return traverseForAdd(element, left);
            }
        }
        // this should never happen.
        assert (false);
        return null;
    }

    /**
     * This method helps look up a node by element.
     *
     * @param element the element of the node being looked for
     * @param node    the node being looked at
     * @return the node with the corresponding element if it exists, otherwise null
     * @pre the element is a valid mapping
     * @post the node with the matching element is returned
     */
    private Node<E> lookupHelper(E element, Node<E> node) {
        if (node.getValue().compareTo(element) == 0) {
            return node;
        }
        if (node.getValue().compareTo(element) < 0
                && node.getRightChild() != theNilLeaf) {
            return lookupHelper(element, node.getRightChild());
        }
        if (node.getValue().compareTo(element) > -1
                && node.getLeftChild() != theNilLeaf) {
            return lookupHelper(element, node.getLeftChild());
        }
        return null;
    }

    /**
     * This method finds the in order successor of the subtree with startNode at
     * the root
     *
     * @param startNode the root of the subtree
     * @return the node that is the in order sucessor of startNode
     */
    private Node<E> findInOrderSuccessor(Node<E> startNode) {
        if (startNode.getLeftChild() == theNilLeaf) {
            return startNode;
        }
        if (startNode.getLeftChild() != theNilLeaf) {
            return findInOrderSuccessor(startNode.getLeftChild());
        }
        if (startNode.getRightChild() != theNilLeaf) {
            return findInOrderSuccessor(startNode.getRightChild());
        }
        // we should never reach here
        assert (false);
        return null;
    }

    /**
     * This method finds the in order predecessor of the subtree with startNode
     * at the root
     *
     * @param startNode the root of the subtree
     * @return the node that is the in order predecessor of startNode
     */

    private Node<E> findInOrderPredecessor(Node<E> startNode) {
        if (startNode.getRightChild() == theNilLeaf) {
            return startNode;
        }
        if (startNode.getRightChild() != theNilLeaf) {
            return findInOrderPredecessor(startNode.getRightChild());
        }
        if (startNode.getLeftChild() != theNilLeaf) {
            return findInOrderPredecessor(startNode.getLeftChild());
        }
        // we should never reach here
        assert (false);
        return null;
    }

    private void internalInOrderPrint(Node<E> start, int depth) {
        if (start == null) {
            return;
        }
        internalInOrderPrint(start.getRightChild(), depth + 1);
        for (int i = 0; i < depth; i++) {
            System.out.print(" ");
        }
        if (start.isRed()) {
            System.out.println(start.getValue() + " Red");
        } else {
            System.out.println(start.getValue());
        }
        internalInOrderPrint(start.getLeftChild(), depth + 1);
    }

    /**
     * This method finds the depth of the tree starting at start
     *
     * @param start the node to start the depth at
     * @return the depth of the tree
     * @pre true
     * @post the tree is traversed and the depth is returned
     */
    private int getTreeDepth(Node<E> start) {
        if (start == null) {
            return 0;
        }
        return Math.max(getTreeDepth(start.getRightChild()) + 1,
                        getTreeDepth(start.getLeftChild()) + 1);
    }

    /**
     * This method does most of the logic of the verify method. see
     * {@link #verify()}
     *
     * @param node           the current node
     * @param numofBlack     the number of black so far
     * @param wasRed         if the previous node was red
     * @param firstPathBlack the length of the black path that is reached first
     * @return the number of black nodes in any given path
     */
    private int verifyHelper(Node<E> node, int numofBlack, boolean wasRed,
                             int firstPathBlack) {
        if (node == null) {
            if (firstPathBlack == -1) {
                firstPathBlack = numofBlack;
            } else {
                assert (numofBlack == firstPathBlack);
            }
            return firstPathBlack;
        }
        if (!node.isRed()) {
            numofBlack++;
        }
        if (node.isRed()) {
            assert (!wasRed);
        }
        verifyHelper(node.getRightChild(), numofBlack, node.isRed(),
                     firstPathBlack);
        verifyHelper(node.getLeftChild(), numofBlack, node.isRed(),
                     firstPathBlack);
        return numofBlack;
    }
}
