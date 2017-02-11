/* 
 * Huff.java
 *
 * A program that compresses a file using Huffman encoding. Assumes
 * that the input file is ANSI encoded text
 *
 * Jon Janelle, jonjanelle1@gmail.com
 * 11/23/2016
 */ 

import java.util.*;



import java.io.*;

public class Huff {
	/**
	 * Build a list of character-frequency pairs from a text file 
	 */
	public static int[] buildFreqList(FileReader in)
	{
		int[] freqList = new int[255]; //Assume 8-bit character encoding
		int ch;
		try{
			do {
				ch = in.read(); //reads one char at a time
				if (ch!=-1) {
					freqList[ch]++;
				}
			}while(ch>=0); //-1 indicates end of input stream
		}
		catch(IOException e){
			System.out.println("Error reading from file. "+e.getMessage());
		}
		return freqList;
	}

	/**
	 * Write encoded text to file using a BitWriter
	 * 
	 */
	public static void writeEncoded(FileReader in, BitWriter writer, String[] huffTable)
	{
		int ch = 0;
		do {
			try{
				ch = in.read(); //reads one char at a time
			} catch(IOException e){
				System.out.println("Error reading from input file");
			}
			if (ch!=-1) {
				Code huffCode = new Code();
				String huffSeq = huffTable[ch];
				for (int i = 0; i < huffSeq.length();i++){
					huffCode.addBit(Integer.parseInt(huffSeq.substring(i, i+1)));
				}
				try{
					writer.writeCode(huffCode);
				} catch(IOException e){
					System.out.println("Error writing to output file");
				}
			}

		}while(ch>=0); //-1 indicates end of input stream
	}

	/** 
	 * main method for compression.  Takes command line arguments. 
	 * To use, type: java Huff input-file-name output-file-name 
	 * at the command-line prompt. 
	 */ 
	public static void main(String[] args) throws IOException {


		Scanner console = new Scanner(System.in);
		FileReader in = null;               // reads in the original file
		ObjectOutputStream out = null;      // writes out the compressed file

		// Get the file names from the command line (if any) or from the console.
		String infilename, outfilename;
		if (args.length >= 2) {
			infilename = args[0];
			outfilename = args[1];
		} else {
			System.out.print("Enter the name of the original file: ");
			infilename = console.nextLine();
			System.out.print("Enter the name to be used for the compressed file: ");
			outfilename = console.nextLine();
		}

		// Open the input file.
		try {
			in = new FileReader(infilename);
		} catch (FileNotFoundException e) {
			System.out.println("Can't open file " + infilename);
			System.exit(1);
		}

		// Open the output file.
		try {
			out = new ObjectOutputStream(new FileOutputStream(outfilename));
		} catch (FileNotFoundException e) {
			System.out.println("Can't open file " + outfilename);
			System.exit(1);
		}

		// Create a BitWriter that is able to write to the compressed file.
		BitWriter writer = new BitWriter(out);

		/****** Add your code below. ******/
		/* 
		 * Note: After you read through the input file once, you will need
		 * to reopen it in order to read through the file
		 * a second time.
		 */

		//Build character frequency list from data file
		int[] freqList = buildFreqList(in);

		//Count number of non-zero entries for header length
		int listLen = 0;
		for (int i: freqList){
			if (i>0) listLen++;
		}

		HuffEncodeTree htree = new HuffEncodeTree();
		//Build list of char:frequency nodes in sorted order
		//Also writes header to output file
		//First write the number of entries in the header
		out.writeInt(listLen);
		for (int i = 0; i < freqList.length; i++){
			if (freqList[i]>0) {//list only includes characters seen one or more times
				htree.addItem(freqList[i],(char)i);//Add node to list
				out.writeInt(i);			//Write character to header (4 bytes)
				out.writeInt(freqList[i]);  //Write character frequency to header (4 bytes)
			}
		}
		//Construct the Huffman tree 
		htree.constructHuffTree();

		String[] result = htree.buildCodeTable();//Indices are ascii codes, values Huffman codes

		//Reopen file handle so it is positioned at beginning
		in = new FileReader(infilename); 

		//Write compressed text to output file 
		writeEncoded(in, writer, result);


		/* Leave these lines at the end of the method. */
		in.close();
		out.close();
	}
}
