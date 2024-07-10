package com.mkippe;

public class MinIntHeap {
    private int capacity = 10;
    private int size = 0;
    private int[] items = new int[capacity];

    // Helper methods to get parent and child indices
    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    // Helper methods to check if indices are within the bounds
    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    // Helper methods to get the values of parent or children
    private int leftChild(int index) {
        return items[getLeftChildIndex(index)];
    }

    private int rightChild(int index) {
        return items[getRightChildIndex(index)];
    }

    private int parent(int index) {
        return items[getParentIndex(index)];
    }

    // Helper method to swap two elements
    private void swap(int indexOne, int indexTwo) {
        int temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    // Ensure the capacity of the heap
    private void ensureExtraCapacity() {
        if (size == capacity) {
            capacity *= 2;
            int[] newItems = new int[capacity];
            System.arraycopy(items, 0, newItems, 0, size);
            items = newItems;
        }
    }

    // Peek at the root element (minimum element)
    public int peek() {
        if (size == 0) throw new IllegalStateException();
        return items[0];
    }

    // Remove the root element (minimum element)
    public int poll() {
        if (size == 0) throw new IllegalStateException();
        int item = items[0];
        items[0] = items[size - 1];
        size--;
        heapifyDown();
        return item;
    }

    // Add an element to the heap
    public void add(int item) {
        ensureExtraCapacity();
        items[size] = item;
        size++;
        heapifyUp();
    }

    // Maintain heap property after adding an element
    private void heapifyUp() {
        int index = size - 1;
        while (hasParent(index) && parent(index) > items[index]) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    // Maintain heap property after removing the root element
    private void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index) < leftChild(index)) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (items[index] < items[smallerChildIndex]) {
                break;
            } else {
                swap(index, smallerChildIndex);
            }
            index = smallerChildIndex;
        }
    }

    // Method to get the size of the heap
    public int getSize() {
        return size;
    }

    // Method to check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        MinIntHeap minHeap = new MinIntHeap();
        minHeap.add(10);
        minHeap.add(15);
        minHeap.add(20);
        minHeap.add(17);
        minHeap.add(8);

        System.out.println(minHeap.peek()); // should print 8
        System.out.println(minHeap.poll()); // should print 8
        System.out.println(minHeap.peek()); // should print 10
    }
}
