package com.creditWise.Sakshi;

import java.util.Map;

public class RedBlackTree<T extends Comparable<T>> {
    private Node root;
    public final Node NIL = new Node(null);  // Keep NIL as an instance variable

    public RedBlackTree() {
        this.root = NIL;
    }

    // Inner class to represent a node in the Red-Black Tree
    public class Node {
        T value;
        Node left, right, parent;
        int color; // 0 for black, 1 for red

        public Node(T value) {
            this.value = value;
            this.color = 1; // New nodes are always red initially
            this.left = this.right = this.parent = NIL;
        }
    }

    // Insert method for the Red-Black Tree
    public void insert(T value) {
        Node newNode = new Node(value);
        Node temp = root;
        Node parent = NIL;

        // Standard BST insertion
        while (temp != NIL) {
            parent = temp;
            if (value.compareTo(temp.value) < 0) {
                temp = temp.left;
            } else {
                temp = temp.right;
            }
        }

        // Attach the new node to its parent
        newNode.parent = parent;
        if (parent == NIL) {
            root = newNode;
        } else if (value.compareTo(parent.value) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Balance the tree
        fixInsert(newNode);
    }

    private void fixInsert(Node node) {
        while (node.parent != NIL && node.parent.color == 1) {  // Check if parent is valid
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == 1) {
                    node.parent.color = 0;
                    uncle.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == 1) {
                    node.parent.color = 0;
                    uncle.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = 0;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Traverse and count frequencies of terms
    public void countTermFrequencies(Node node, Map<String, Integer> termFrequencyMap) {
        if (node != null && node != NIL) {  // Ensure NIL is not processed
            termFrequencyMap.put((String) node.value, termFrequencyMap.getOrDefault((String) node.value, 0) + 1);
            countTermFrequencies(node.left, termFrequencyMap);
            countTermFrequencies(node.right, termFrequencyMap);
        }
    }

    public Node getRoot() {
        return root;
    }
}
