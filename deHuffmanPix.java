// Name:   
// Date:   
import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
public class deHuffmanPix
{
   public static void main(String[] args) throws IOException
   {
      Scanner keyboard = new Scanner(System.in);
      System.out.print("\nWhat binary picture file (middle part) ? ");
      String middlePart = keyboard.next();
      Scanner sc = new Scanner(new File("pix."+middlePart+".txt")); 
      String binaryCode = sc.next();
      Scanner huffScheme = new Scanner(new File("schemePix."+middlePart+".txt")); 
      int width = huffScheme.nextInt();   //  read the size of the image
      int height = huffScheme.nextInt();    
      
      TreeNode root = huffmanTree(huffScheme);
      BufferedImage bufImg = dehuff(binaryCode, root,height, width);
   	
      JFrame f = new JFrame("HuffmanPix");
      f.setSize(50,50);    // width, height
      f.setLocation(100,50);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setContentPane(new DisplayPixx(bufImg));
      f.setVisible(true);
      
      sc.close();
      huffScheme.close();
      keyboard.nextLine();  //press 'enter'
      keyboard.nextLine(); 
    // System.exit(0);
   }
   
   public static TreeNode huffmanTree(Scanner huffScheme) {
	    // Skip the first line
	    huffScheme.nextLine();

	    // Create a priority queue to hold the tree nodes
	    PriorityQueue<TreeNode> pq = new PriorityQueue<>((a, b) -> a.freq - b.freq);

	    // Process each line of the huffScheme data
	    while (huffScheme.hasNextLine()) {
	        String line = huffScheme.nextLine();
	        System.out.println(line);
	        String[] parts = line.split(" ");
	        int value = Integer.parseInt(parts[0]);
	        String code = parts[1];

	        // Create a new tree node with the value and code
	        TreeNode node = new TreeNode((char) value, code.length());
	        pq.add(node);
	    }

	    // Build the Huffman tree
	    while (pq.size() > 1) {
	        TreeNode left = pq.poll();
	        TreeNode right = pq.poll();
	        int sumFreq = left.freq + right.freq;
	        TreeNode parent = new TreeNode('\0', sumFreq);
	        parent.left = left;
	        parent.right = right;
	        pq.add(parent);
	    }

	    // Return the root of the Huffman tree
	    return pq.poll();
	}


   public static BufferedImage dehuff(String text, TreeNode root, int height, int width) {
	    // Create a BufferedImage with the specified dimensions
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	    // Create a graphics object to manipulate the image
	    Graphics2D graphics = image.createGraphics();

	    // Traverse the Huffman tree to decode the text and set the pixel values
	    TreeNode currentNode = root;
	    int pixelIndex = 0;
	    for (int i = 0; i < text.length(); i++) {
	        char bit = text.charAt(i);

	        if (bit == '0') {
	            currentNode = currentNode.left;
	        } else if (bit == '1') {
	            currentNode = currentNode.right;
	        }

	        // Check if the current node is a leaf node
	        if (currentNode.isLeaf()) {
	            // Extract the pixel value from the leaf node
	            int pixelValue = currentNode.symbol;

	            // Calculate the x and y coordinates of the pixel in the image
	            int x = pixelIndex % width;
	            int y = pixelIndex / width;

	            // Set the pixel value in the image
	            image.setRGB(x, y, pixelValue);

	            // Move to the next pixel
	            pixelIndex++;

	            // Reset the current node to the root
	            currentNode = root;
	        }
	    }

	    // Dispose the graphics object
	    graphics.dispose();

	    // Return the decoded image
	    return image;
	}

   
}

  /*
  * Minimum code necessary to show a BufferedImage.   
  * 
  */ 
class DisplayPixx extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private BufferedImage img;
    private Graphics g;

    public DisplayPixx(BufferedImage bufImg, ImageIcon i)   //for Huffman Coding
    {
        int w = bufImg.getWidth();
        int h = bufImg.getHeight();
        img = bufImg;
        g = bufImg.getGraphics();
        g.drawImage(i.getImage(), 0, 0, w, h, null);
    }

    public DisplayPixx(BufferedImage bufImg)              //for deHuffman
    {
        img = bufImg;
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }
}

class TreeNode {
    char symbol;
    int freq;
    TreeNode left;
    TreeNode right;

    public TreeNode(char symbol, int freq) {
        this.symbol = symbol;
        this.freq = freq;
        this.left = null;
        this.right = null;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}