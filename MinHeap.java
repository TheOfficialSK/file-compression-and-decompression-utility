package madzip;

import java.util.NoSuchElementException;

/**
 * The MinHeap class is a generic implementation of a min-heap data structure.
 *
 * @param <T> The type of the elements in the heap.
 * @author Salman Khattak
 * @version 12/6/2022
 */
public class MinHeap<T extends Comparable<T>> {
  private int size;
  public final T[] items;

  /**
   * Creates a new MinHeap object.
   *
   * @param capacity the capacity of the heap.
   */
  public MinHeap(int capacity) {
    items = (T[]) new Comparable[capacity];
  }

  /**
   * Returns the size of the heap.
   *
   * @return the size of the heap.
   */
  public int size() {
    return size;
  }

  /**
   * Returns the capacity of the heap.
   *
   * @return the capacity of the heap.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the capacity of the heap.
   *
   * @param element the index of the element.
   */
  public void add(T element) {
    if (size == items.length) {
      throw new IllegalStateException("Heap is full");
    }
    items[size] = element;
    heapifyUp(size++);
  }

  /**
   * Returns the capacity of the heap.
   *
   * @return the capacity of the heap.
   */

  public T remove() {
    if (isEmpty()) {
      throw new NoSuchElementException("Heap is empty");
    }
    T smallest = items[0];
    items[0] = items[--size];
    heapifyDown(0);
    return smallest;
  }
  /**
   * Heapifies up the element at the given index.
   *
   * @param index the index of the element to heapify up.
   */
  private void heapifyUp(int index) {
    int parentIndex;
    while (index > 0 && (parentIndex = (index - 1) / 2) >= 0) {
      if (items[index].compareTo(items[parentIndex]) < 0) {
        swap(index, parentIndex);
      } else {
        break;
      }
      index = parentIndex;
    }
  }
  /**
   * Heapifies down the element at the given index.
   *
   * @param index the index of the element to heapify down.
   */
  private void heapifyDown(int index) {
    int smallerChildIndex;
    while ((smallerChildIndex = 2 * index + 1) < size) {
      int rightChildIndex = smallerChildIndex + 1;
      if (rightChildIndex < size && items[rightChildIndex].compareTo(items[smallerChildIndex]) < 0) {
        smallerChildIndex = rightChildIndex;
      }
      if (items[index].compareTo(items[smallerChildIndex]) > 0) {
        swap(index, smallerChildIndex);
      } else {
        break;
      }
      index = smallerChildIndex;
    }
  }
  /**
   * Swaps the elements at the given indices.
   *
   * @param index1 the index of the first element.
   * @param index2 the index of the second element.
   */
  private void swap(int index1, int index2) {
    T temp = items[index1];
    items[index1] = items[index2];
    items[index2] = temp;
  }

}
