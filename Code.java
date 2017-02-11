/*
 * Code - a class for an encoding of a character, which consists of
 * the bits themselves and the number of bits in the code.  Note that
 * the maximum length of a character encoding is 32 bits.
 *
 * 
 * You are encouraged to use this class wherever you need to 
 * store a character encoding.
 *
 * Note: You should not need to modify this file, although you are
 * welcome to do so.
 */

public class Code {
    private int bits;       // an int that stores the bits in the code
    private int length;     // the number of bits in the code
    
    /*
     * Creates an "empty" Code object. After using this constructor to
     * create a Code object, you can use the addBit() and removeBit()
     * methods to build up the desired binary encoding.
     */
    public Code() {
        bits = 0;
        length = 0;
    }

    /*
     * Creates a new Code object that is a copy of the other Code object
     * passed in as a parameter. This allows you to "save" the encoding
     * specified in other before other is modified further. It also
     * allows you to create a new Code that is based on an existing Code.
     */
    public Code(Code other) {
        this.bits = other.bits;
        this.length = other.length;
    }

    /*
     * getBits - returns an integer representation of the code --
     * i.e., the decimal equivalent of the binary number formed
     * by the bits in the code.  For example, if the code was
     * the 3 bits 101, this method would return 5.
     */
    public int getBits() {
        return bits;
    }

    /*
     * getBit - returns the nth bit in the code as an integer --
     * either 0 or 1.  The bits are numbered from right to left,
     * so bit 0 is the rightmost bit in the code.
     */
    public int getBit(int n) {
        if (n < 0 || n > length - 1) {
            throw new IllegalArgumentException(
              "n must be between 0 and " + (length - 1));
        }

        return ((bits & (1 << n)) >> n);
    }

    /*
     * length - returns the number of bits in the code
     */
    public int length() {
        return length;
    }

    /*
     * addBit - adds the specified bit to the right-hand side of the bits
     * that currently make up the code.  For example, if the code
     * were the bits 10, addBit(1) would result in the code being 101.
     */
    public void addBit(int theBit) {
        if (!(theBit == 0 || theBit == 1)) {
            throw new IllegalArgumentException("argument must be 0 or 1");
        }

        if (length == 32) {
            throw new IllegalStateException("cannot fit additional bits");
        }

        // Add the new bit to the right of the existing bits, shifting
        // the existing bits one place to the left.
        bits = (bits << 1) | theBit;

        length++;
    }

    /*
     * removeBit - removes the rightmost bit from the code
     */
    public void removeBit() {
        if (length == 0) {
            throw new IllegalStateException("no bits to remove");
        }

        // Shift the bits one place to the right, losing the rightmost bit.
        bits = bits >> 1;

        length--;
    }

    /*
     * toString - returns the code in string form.  For example,
     * the code 101 would return the string "101".  This method
     * is provided for debugging purposes.
     */
    public String toString() {
        if (length == 0) {
            return "code has no bits yet";
        }
        
        String str = "";
        for (int i = length - 1; i >= 0; i--) {
            str += getBit(i);
        }
        return str;
    }
}
