package madzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * The MadZip class is an executable class that can be used to compress files.
 *
 * @author Salman Khattak
 * @version 11/23/2022
 */
public class MadZip {
  /**
   * The Node class which allows for the creation of a Huffman tree.
   */
  public static class Node implements Comparable<Node> {
    final byte data;
    private final int frequency;
    Node left;
    Node right;

    /**
     * Creates a new Node object.
     *
     * @param data      the byte value of the Node.
     * @param frequency the frequency of the byte value.
     */
    public Node(byte data, int frequency) {
      this.data = data;
      this.frequency = frequency;
      this.left = null;
      this.right = null;
    }

    /**
     * Creates a new Node object with existing nodes.
     *
     * @param left  the left child of the Node.
     * @param right the right child of the Node.
     */
    public Node(Node left, Node right) {
      this.data = 0;
      this.frequency = left.frequency + right.frequency;
      this.left = left;
      this.right = right;
    }

    @Override
    public int compareTo(Node o) {
      return this.frequency - o.frequency;
    }

    /**
     * Returns the frequency of the Node.
     *
     * @return the frequency of the Node.
     */
    public Byte getByte() {
      return data;
    }

    /**
     * Returns the frequency of the Node.
     *
     * @return the frequency of the Node.
     */

    public boolean isLeaf() {
      return left == null && right == null;
    }
  }

  /**
   * The main method of the YourZippersUp class.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Invalid number of arguments.");
      return;
    }
    String sourceFile = args[0], destinationFile = args[1];
    if (destinationFile.charAt(0) == '/') {
      System.err.println("File does not exist.");
      return;
    }
    File file = new File(destinationFile);
    try (FileInputStream fis = new FileInputStream(sourceFile)) {
      HashMap<Byte, Integer> frequencies = new HashMap<>();
      int read;
      while ((read = fis.read()) != -1) {
        byte b = (byte) read;
        frequencies.put(b, frequencies.getOrDefault(b, 0) + 1);
      }
      if (frequencies.isEmpty()) {
        save(file, new HuffmanSave(new BitSequence(), frequencies));
        return;
      }
      if (frequencies.size() == 1) {
        BitSequence bs = new BitSequence();
        bs.appendBit(1);
        save(file, new HuffmanSave(bs, frequencies));
        return;
      }
      MinHeap<Node> minHeap = new MinHeap<>(frequencies.size());
      for (Byte b : frequencies.keySet()) {
        minHeap.add(new Node(b, frequencies.get(b)));
      }
      Node root = buildTree(minHeap);
      HashMap<Byte, String> binaryTree = new HashMap<>();
      buildBinaryTree(root, binaryTree, "");
      BitSequence bitSequence = new BitSequence();
      try (FileInputStream fis2 = new FileInputStream(sourceFile)) {
        while ((read = fis2.read()) != -1) {
          for (char c : binaryTree.get((byte) read).toCharArray()) {
            bitSequence.appendBit(c == '1' ? 1 : 0);
          }
        }
      }
      save(file, new HuffmanSave(bitSequence, frequencies));
    } catch (IOException e) {
      System.err.println("File error.");
    }
  }

  /**
   * Builds the frequency table.
   *
   * @param root       the root of the tree.
   * @param binaryTree hashmap representation of the tree.
   * @param s          The string of bytes
   */
  private static void buildBinaryTree(Node root, HashMap<Byte, String> binaryTree, String s) {
    if (root.left == null && root.right == null) {
      binaryTree.put(root.data, s);
      return;
    }
    assert root.left != null;
    buildBinaryTree(root.left, binaryTree, s + "0");
    buildBinaryTree(root.right, binaryTree, s + "1");
  }

  /**
   * Builds the binary tree.
   *
   * @param minHeap the MinHeap to build the tree from.
   * @return the root of the tree.
   */
  private static Node buildTree(MinHeap<Node> minHeap) {
    while (minHeap.size() > 1) {
      Node left = minHeap.remove(), right = minHeap.remove();
      minHeap.add(new Node(left, right));
    }
    return minHeap.remove();
  }

  /**
   * This method saves the compressed file.
   *
   * @param file       the file to be saved.
   * @param compressed the compressed file.
   */
  public static void save(File file, HuffmanSave compressed) {
    ObjectOutputStream in;
    try {
      in = new ObjectOutputStream(new FileOutputStream(file));
      in.writeObject(compressed);
      in.close();
    } catch (IOException e) {
      System.err.println("File not found.");
      throw new RuntimeException();
    }
  }

}
