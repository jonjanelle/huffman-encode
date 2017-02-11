/*
 * BitWriter - a class for objects that read bits from a file
 *
 * Computer Science E-22, Harvard University
 * 
 * NOTE: You should not have to modify this file.
 */

import java.io.*;

public class BitReader {
    // a bit buffer containing up to 8 bits from the file
    private byte buffer;

    // number of bits currently in the buffer
    private int numBits;

    // the current input stream, assumed to be open
    private InputStream in;

    public BitReader(InputStream instream) {
        in = instream;
        buffer  = 0;
        numBits = 0;
    }

    /*
     * getBit - returns the next bit from the file (0 or 1) as an int,
     * or -1 if the end of the file has been reached.
     */      
    public int getBit() throws IOException {
        // If the buffer is empty, read more bits from the file.
        if (numBits == 0) {
            int returnVal = in.read();
            if (returnVal == -1) {
                return -1;                    // eof has been reached 
            } else {
                buffer = (byte)returnVal;
                numBits = 8;                  // we just read 8 bits
            }
        }

        // Now we need to get the highest bit that is valid:
        // i.e., the bit in position numBits - 1 in the buffer.
        int bit = (buffer & (1 << (numBits - 1))) >> (numBits - 1);
        numBits--;

        return bit;
    }
}
