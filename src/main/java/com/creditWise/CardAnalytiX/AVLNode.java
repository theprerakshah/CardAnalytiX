package com.creditWise.CardAnalytiX;


// Node class for AVL Tree
class AVLNode {
    String word;
    int frequency;
    AVLNode left, right;
    int height;

    AVLNode(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
        this.height = 1;
    }
}

// AVL Tree class
class AVLTree {
    public AVLNode root;

    // Function to get height of the node
    private int height(AVLNode node) {
        if(node==null){
            return 0;
        }
        else{
            return node.height;
        }
    }

    // Function to update height of the node
    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Function to calculate balance factor
    private int subTreeHeightDifference(AVLNode node) {
        if(node==null){
            return 0;
        }
        else{
            return height(node.left) - height(node.right);
        }
    }

    // Right rotate utility
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Left rotate utility
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Insert word into the AVL tree
    public AVLNode insert(AVLNode node, String word) {
        if (node == null) return new AVLNode(word, 1);

        if (word.compareTo(node.word) < 0) {
            node.left = insert(node.left, word);
        } else if (word.compareTo(node.word) > 0) {
            node.right = insert(node.right, word);
        } else {
            node.frequency++;
            return node;
        }

        updateHeight(node);
        int balance = subTreeHeightDifference(node);

        // Balancing the AVL tree
        if (balance > 1 && word.compareTo(node.left.word) < 0)
            return rightRotate(node);

        if (balance < -1 && word.compareTo(node.right.word) > 0)
            return leftRotate(node);

        if (balance > 1 && word.compareTo(node.left.word) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && word.compareTo(node.right.word) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Insert word public method
    public void insert(String word) {
        root = insert(root, word);
    }

}