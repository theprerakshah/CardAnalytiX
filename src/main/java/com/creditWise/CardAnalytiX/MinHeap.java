package com.creditWise.CardAnalytiX;

public class MinHeap { // Member variables of this class
    private int[] Heap;
    private int size;
    private int maximumSize;

    // Initializing front as static with unity
    private static final int FRONT = 1;

    // Constructor of this class
    public MinHeap(int maxsize)
    {

        this.maximumSize = maxsize;
        this.size = 0;

        Heap = new int[this.maximumSize + 1];
        Heap[0] = Integer.MIN_VALUE;
    }

    // Method to get the position of a nodes parent
    private int parent(int position) { return position / 2; }

    // Method to get the position of a nodes left child
    private int leftChild(int position) { return (2 * position); }

    // Method to get the position of a nodes right child
    private int rightChild(int position)
    {
        return (2 * position) + 1;
    }

    // Method to check if its a leaf node
    private boolean isLeaf(int position)
    {

        if (position > (size / 2)) {
            return true;
        }

        return false;
    }

    // Method 5
    // To swap two nodes of the heap
    private void swap(int fpos, int spos)
    {

        int tmp;
        tmp = Heap[fpos];

        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }


    // To heapify the node at position
    private void minHeapify(int position)
    {
        if(!isLeaf(position)){
            int swapPos= position;
            // swap with the minimum of the two children
            // to check if right child exists. Otherwise default value will be '0'
            // and that will be swapped with parent node.
            if(rightChild(position)<=size)
                swapPos = Heap[leftChild(position)]<Heap[rightChild(position)]?leftChild(position):rightChild(position);
            else
                swapPos= leftChild(position);

            if(Heap[position]>Heap[leftChild(position)] || Heap[position]> Heap[rightChild(position)]){
                swap(position,swapPos);
                minHeapify(swapPos);
            }

        }
    }

    // Method 7
    // To insert a node into the heap
    public void insert(int element)
    {

        if (size >= maximumSize) {
            return;
        }

        Heap[++size] = element;
        int current = size;

        while (Heap[current] < Heap[parent(current)]) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Method 8
    // To print the contents of the heap
    public void print()
    {
        for (int i = 1; i <= size / 2; i++) {

            // Printing the parent and both childrens
            System.out.print(
                    " PARENT : " + Heap[i]
                            + " LEFT CHILD : " + Heap[2 * i]
                            + " RIGHT CHILD :" + Heap[2 * i + 1]);

            // By here new line is required
            System.out.println();
        }
    }

    // Method 9
    // To remove and return the minimum
    // element from the heap
    public int remove()
    {

        int popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);

        return popped;
    }
    public int size(){
        return size;
    }
}
