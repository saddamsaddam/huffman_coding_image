// Name:      
// Date:
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class HuffmanPix 
{
   public static int WIDTH=50 ;   // 500 x 500 is too big
   public static int HEIGHT=50;

   public static void main(String[] args) throws IOException
   {
      Scanner keyboard = new Scanner(System.in);
      System.out.print("Encoding using Huffman codes");
      System.out.print("\nWhat image (including extension)? ");
      String pixName = keyboard.nextLine();
      ImageIcon i = new ImageIcon(pixName);
      BufferedImage bufImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

      JFrame f = new JFrame("HuffmanPix");
      f.setSize(50,50);    // width, height
      f.setLocation(100,50);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setContentPane(new DisplayPix(bufImg, i));
      f.setVisible(true);
    
   
      System.out.print("\nEnter middle part of filename:  ");
      String middlePart = keyboard.nextLine();
   
      huffmanize( bufImg, middlePart );
      
     // System.exit(0);
   }

   
 
   public static void huffmanize(BufferedImage bufImg, String middlePart) throws IOException {
	    // Perform Huffman encoding

	    // Step 1: Calculate symbol frequencies
	   Map<Integer, Integer> symbolFrequencies = new HashMap<>();
       for (int i = 0; i < bufImg.getWidth(); i++) {
           for (int j = 0; j <bufImg.getHeight(); j++) {
               int color = bufImg.getRGB(i, j);
               symbolFrequencies.put(color,symbolFrequencies.getOrDefault(color, 0) + 1);
           }
       }

	    // Step 2: Build Huffman tree
	    Node root = buildHuffmanTree(symbolFrequencies);

	    // Step 3: Generate Huffman codes
	    Map<Integer, String> codes =generateHuffmanCodes(root);


	    // Step 4: Encode the image using Huffman codes
	    StringBuilder compressedData = new StringBuilder();
        for (int i = 0; i < bufImg.getWidth(); i++) {
            for (int j = 0; j < bufImg.getHeight(); j++) {
                int color = bufImg.getRGB(i, j);
                compressedData.append(codes.get(color));
            }
        }

	    // Step 5: Save the encoded image to a file
	    String binaryFileName = "pix." + middlePart + ".txt";
	    FileOutputStream compressedOutput = new FileOutputStream(binaryFileName);
        compressedOutput.write(compressedData.toString().getBytes());
        compressedOutput.close();
	    System.out.println("Pix done");


	    // Step 6: Save the Huffman scheme to a file
	
	    String schemeFile = "schemePix." + middlePart + ".txt";

	    FileWriter codeTableWriter = new FileWriter(schemeFile);
	    codeTableWriter.write(WIDTH +"\n");
	    codeTableWriter.write(HEIGHT+"\n");
        for (Map.Entry<Integer, String> entry : codes.entrySet()) {
            codeTableWriter.write(entry.getKey() + " " + entry.getValue() + "\n");
        }
        codeTableWriter.close();
	    System.out.println("Scheme done");

	}
   public static Map<Integer, String> generateHuffmanCodes(Node root) {
	    Map<Integer, String> codes = new HashMap<>();
	    generateHuffmanCodesRecursive(root, "", codes);
	    return codes;
	}

	private static void generateHuffmanCodesRecursive(Node node, String code, Map<Integer, String> codes) {
		 if (node.left == null && node.right == null) {
			 codes.put(node.color, code);
	        } else {
	        	generateHuffmanCodesRecursive(node.left, code + "0", codes);
	        	generateHuffmanCodesRecursive(node.right, code + "1", codes);
	        }
	}

   public static Node buildHuffmanTree(  Map<Integer, Integer> symbolFrequencies) {
	   PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
       for (Map.Entry<Integer, Integer> entry : symbolFrequencies.entrySet()) {
           priorityQueue.add(new Node(entry.getKey(), entry.getValue(), null, null));
       }

       while (priorityQueue.size() > 1) {
           Node left = priorityQueue.poll();
           Node right = priorityQueue.poll();
           Node parent = new Node(-1, left.frequency + right.frequency, left, right);
           priorityQueue.add(parent);
       }

       Node root = priorityQueue.poll();
       // Return the root of the Huffman tree
       return root;
	}

   
   /*  several Huffman methods go here  */
   
   
}


  /*
  * This node stores two values.  
  * The compareTo method must ensure that the lowest frequency has the highest priority.
  */   
class Node implements Comparable<Node> {
    final int color;
    final int frequency;
    final Node left;
    final Node right;

    public Node(int color, int frequency, Node left, Node right) {
        this.color = color;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(frequency, o.frequency);
    }
}

  /*
  * Minimum code necessary to display a BufferedImage.    
  */ 
class DisplayPix extends JPanel
{
   private BufferedImage img;
   private Graphics g;
   public DisplayPix(BufferedImage bufImg, ImageIcon i)   //for Huffman
   {
      int w = bufImg.getWidth();
      int h = bufImg.getHeight();
      img = bufImg;
      g = bufImg.getGraphics();
      g.drawImage( i.getImage() , 0 , 0 , w , h, null );
   }
   public void paintComponent(Graphics g)
   {
      g.drawImage( img , 0 , 0 , getWidth() , getHeight() , null );
   }
}