/*
 * BitWriter - a class for objects that write bits to a file
 *
 * Computer Science E-22, Harvard University
 * 
 * Note: You should not have to modify this file.
 */

import java.io.*;

public class BitWriter {
    // a bit buffer containing up to 8 bits to be written to the file
    private byte buffer;

    // number of bits currently in the buffer
    private int numBits;

    // the current output stream, assumed to be open
    private OutputStream out;

    // number of bytes written to the file
    private int numBytesWritten;
    
    public BitWriter(OutputStream outstream) {
        out = outstream;
        buffer = 0;
        numBits = 0;
        numBytesWritten = 0;
    }

    /**
     * putBit - writes the next bit to the file.  In reality, the bit
     * goes to the buffer and, once we have a full buffer, the buffer
     * gets written ("flushed") to the file.
     */      
    public void putBit(int bit) throws IOException {
        if (!(bit == 0 || bit == 1)) {
            throw new IllegalArgumentException("argument must be 0 or 1");
        }

        // If the buffer has eight bits, need to flush it first.
        if (numBits == 8) {
            flushBits();
        }

        // Add the new bit to the right of the existing buffer.
        buffer = (byte)((buffer << 1) | bit);
        
        numBits++;
    }

    /**
     * writeCode - writes a complete code (i.e., a complete sequence of
     * bits that represents a character) to the file.
     */      
    public void writeCode(Code code) throws IOException {
        // call putBit() on all bits, from left to right
        for (int i = code.length() - 1; i >= 0; i--) {
        	putBit(code.getBit(i));
        }
    }

    /**
     * flushBits - writes the contents of the buffer to the file.
     *
     * Note: Be sure to call this file when you are done writing, or
     * else some bits may not get written out!
     */      
    public void flushBits() throws IOException {
        //
        // If the buffer has fewer than 8 bits, need to shift the bits
        // left to align them with the left end of the byte before 
        // writing out.  
        // NOTE: when you read in the bits, there may be some 
        // extra 0 bits dangling at the end!
        //
        if (numBits < 8) {
            buffer = (byte)(buffer << (8 - numBits));
        }
    
        out.write(buffer);
        numBytesWritten++;

        // Clear the buffer.
        buffer = 0;
        numBits = 0;
    }

    public int getNumBytesWritten() {
        return numBytesWritten;
    }
}
