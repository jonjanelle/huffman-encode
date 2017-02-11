import java.util.ArrayList;

/**
 * Class to represent a binary Huffman encoding tree.
 * 
 * The nodes, defined by the HuffNode inner class, consist
 * of a character, an integer frequency count for the character,
 * and references to the left and right children
 * 
 * Author: Jon Janelle
 * jonjanelle1@gmail.com
 * 11/23/2016
 *
 */
public class HuffEncodeTree {
	/**
	 * Private inner class to model the nodes of a Huffman tree.
	 * Nodes serve as a pieces in a singly linked list and as
	 * nodes in a binary tree.
	 *
	 */
	private class HuffNode {
		private HuffNode left;
		private HuffNode right;
		private HuffNode next; //reference to the next node in list, null after node added to tree
		private int freq;
		private Character ch;

		//Constructor used in constructor of nodes for Huffman tree
		private HuffNode(int freq, Character ch, HuffNode left, HuffNode right,HuffNode next){
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.next = next;
			this.right = right;
		}
		//Constructor used in the constructor of the initial character/frequency list
		private HuffNode(int freq, Character ch) {
			this(freq, ch, null, null,null);
		}	

	}

	//Fields of the tree
	private HuffNode root;  //root first serves as head of list, then as root of tree
	private int listLength; //Used to track length of freq/char list, used to determine 
							//when Huffman tree construction is complete
	
	
	//Constructor creates an empty tree
	public HuffEncodeTree(){
		root = null;
		listLength = 0;
	}


	/** 
	 *  addItem - add HuffNode to list in sorted order.
	 *  Currently additions are O(n).
	 */
	public void addItem(HuffNode newNode) {
		this.listLength++;
		if (root == null) { //List empty, make new root node
			root = newNode;
			return;
		}

		else{
			HuffNode trav = root;
			HuffNode prev = null;
			while (trav != null){
				if (trav.freq >= newNode.freq){
					newNode.next = trav; //put newNode before trav
					if (prev == null){
						root = newNode;
					}
					else {
						prev.next = newNode; //and after prev
					}
					return;
				}
				prev = trav;
				trav = trav.next;
			}
			prev.next = newNode; //If here, then add to end of list
		}
	}
	
	/**
	 * 	addItem - add character with specified
	 * frequency to the list in sorted order.
	*/
	public void addItem(int freq, Character ch)
	{
		addItem(new HuffNode(freq, ch));
	}

	/**
	 * Print the contents of the list to the console. 
	 * Used for testing and debugging
	 */
	public void printList(){
		HuffNode trav = root;
		while (trav != null){
			System.out.println(trav.ch+": "+trav.freq);
			trav = trav.next;
		}
	}

	
	/**
	 * isEmpty returns true if the list/tree
	 * is empty, false otherwise
	 */
	public boolean isEmpty(){
		return root == null;
	}
	

	/**Remove the smallest node. Assumes that the list is 
	 * sorted in ascending order by frequency field
	 * @return The removed smallest node
	 */
	private HuffNode findRemoveSmallest(){
		if (isEmpty()){
			return null;
		}
		HuffNode smallest = root;
		root = root.next;
		listLength--;
		return smallest;
	}
	
	
	/**
	 * Find and merge the two smallest nodes in the frequency list into one node.
	 * The two smallest nodes in the list are removed, and the newly created node
	 * in added to the list.
	 * 
	 * If only two nodes remain, then the resulting merged node
	 * is the root of the Huffman tree.
	 */
	private void mergeSmallest()
	{
		if (listLength < 2){
			return; //tree already built.
		}
		HuffNode s1 = findRemoveSmallest();
		HuffNode s2 = findRemoveSmallest();
		//Add the merged node to the list in the correct sorted order
		addItem(new HuffNode(s1.freq+s2.freq,null,s1,s2,null));
	
	}
	
	/**
	 * Construct Huffman tree by merging pairs of nodes
	 * from list until only 1 item left in list
	 */
	public void constructHuffTree()
	{
		while (listLength != 1){
			mergeSmallest();
		}
	}
	
	
	/**
	 * Build a table of characters and their
	 * associated Huffman codes.
	 * 
	 * Each item in the returned array is 
	 * a String of the form <char>,<huffcode>
	 */
	public String[] buildCodeTable(){
		String[] result = new String[255];
		buildCodeTable(root,new String(), result);
		return result;
	}
	
	/**
	 * Build encoding table keeping track of paths to 
	 * leaves via a pre-order traversal. Builds up
	 * the resulting an array of <char>,<huffcode>
	 * Strings during the process
	 */
	private void buildCodeTable(HuffNode root, String seq, String[] result){
		if (root.ch != null){
			result[root.ch]=seq;
			//System.out.println(root.ch+":\t"+seq);
		}
		
		if (root.left!=null){
			seq += "0";
			buildCodeTable(root.left,seq, result);
			seq = seq.substring(0,seq.length()-1);
		}
		
		if (root.right!=null){
			seq += "1";
			buildCodeTable(root.right,seq,result);
		}
	}
	
	/**
	 * Decode an bit sequence into plain-text characters
	 * using the Huffman tree
	 */
	public String decodeBitSequence(ArrayList<Integer> bits)
	{
		HuffNode trav = root;
		String original = "";
		for (int i = 0; i < bits.size(); i++){
			if (bits.get(i)==0 && trav.left!=null){
				trav = trav.left;
			} 
			else if (trav.right!=null){
				trav = trav.right;
			}
			else {
				throw new IllegalArgumentException("Bit sequence invalid");
			}
			if (trav.ch!=null){ 	//found a leaf
				original+=trav.ch; //add decoded character to result
				trav = root; 	   //and start a new traversal
			}
		}
		return original;
	}


}
