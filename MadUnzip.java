package madzip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * The MadUnzip class is an executable class that can be used to decompress files.
 *
 * @author Salman Khattak
 * @version 11/23/2022
 */
public class MadUnzip {
  /**
   * The main method which takes in the file name and decompresses it.
   *
   * @param args the file name and destination name.
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Invalid number of arguments.");
      return;
    }

    String compressedFile = args[0];
    String destinationFile = args[1];

    if (checkEmptyFile(compressedFile)) {
      createEmptyFile(destinationFile);
    } else {
      decompressFile(compressedFile, destinationFile);
    }
  }
  /**
   * Creates an empty file.
   *
   * @param destinationFile the file name.
   */
  private static void createEmptyFile(String destinationFile) {
    try (FileOutputStream fileOut = new FileOutputStream(destinationFile)) {
    } catch (IOException e) {
      System.err.println("Error creating file.");
    }
  }
  /**
   * Decompresses the file.
   *
   * @param compressedFile   the file name.
   * @param destinationFile the destination file name.
   */
  private static void decompressFile(String compressedFile, String destinationFile) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(compressedFile))) {
      HuffmanSave save = (HuffmanSave) in.readObject();
      BitSequence bits = save.getEncoding();
      HashMap<Byte, Integer> frequencies = save.getFrequencies();
      MadZip.Node root = createTree(frequencies);
      ArrayList<Byte> decoded = decode(bits, root);

      writeDecodedBytes(destinationFile, decoded);
    } catch (IOException e) {
      System.err.println("Compressed file does not exist.");
    } catch (ClassNotFoundException e) {
      System.err.println("HuffmanSave class not found.");
    }
  }
  /**
   * This method writes the decoded bytes to the destination file.
   *
   * @param  destinationFile the destination file name.
   * @param decoded the decoded bytes.
   */
  private static void writeDecodedBytes(String destinationFile, ArrayList<Byte> decoded) {
    try (FileOutputStream out = new FileOutputStream(destinationFile)) {
      for (Byte b : decoded) {
        out.write(b);
      }
    } catch (IOException e) {
      System.err.println("Error writing to destination file.");
    }
  }

  /**
   * Checks if the huffmanSave Object is empty.
   *
   * @param compressedFile the file name.
   * @return true if the file is empty.
   */
  private static boolean checkEmptyFile(String compressedFile) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(compressedFile))) {
      HuffmanSave save = (HuffmanSave) in.readObject();
      return save.getEncoding().length() == 0;
    } catch (IOException e) {
      System.err.println("Compressed file does not exist.");
    } catch (ClassNotFoundException e) {
      System.err.println("HuffmanSave class not found.");
    }
    return false;
  }
  /**
   * Decodes the bits.
   *
   * @param bits the bits.
   * @param root the root node.
   * @return the decoded bytes.
   */
  private static ArrayList<Byte> decode(BitSequence bits, MadZip.Node root) {
    if (root.isLeaf()) {
      ArrayList<Byte> decoded = new ArrayList<>();
      for (int i = 0; i < bits.length(); i++) {
        decoded.add(root.getByte());
      }
      return decoded;
    }
    ArrayList<Byte> decoded = new ArrayList<>();
    MadZip.Node current = root;
    for (int i = 0; i < bits.length(); i++) {
      if (bits.getBit(i) == 0) {
        current = current.left;
      } else {
        current = current.right;
      }
      if (current.left == null && current.right == null) {
        decoded.add(current.data);
        current = root;
      }
    }
    return decoded;
  }
  /**
   * Creates the tree.
   *
   * @param frequencies the frequencies.
   * @return the root node.
   */
  private static MadZip.Node createTree(HashMap<Byte, Integer> frequencies) {
    PriorityQueue<MadZip.Node> pq = new PriorityQueue<>();
    frequencies.forEach((b, f) -> pq.add(new MadZip.Node(b, f)));

    while (pq.size() > 1) {
      pq.add(new MadZip.Node(pq.remove(), pq.remove()));
    }
    return pq.remove();
  }
}
