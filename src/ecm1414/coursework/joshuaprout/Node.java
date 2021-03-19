package ecm1414.coursework.joshuaprout;

/**
 * The huffman tree is made up of Nodes. These nodes can either be leaf nodes or internal nodes.
 * <p>
 * When the tree is complete, a leaf node contains:
 *      a character that it represents in the encoding,
 *      the freq that the char appears in the text,
 *      a parent node,
 *      a binary flag, 0 if it is the left child of a node or 1 if it is the right, these are used when constructing the code
 *      an encoding string, the binary string representation of the char
 * <p>
 * When the tree is complete, an internal, non root node contains:
 *      a left and right child node
 *      a parent node
 *      a freq, which is the sum of all of it's descendant nodes
 *      a binary flag, 0 if it is the left child of a node or 1 if it is the right, these are used when constructing the code
 */
public class Node {

    private char aChar;

    private Node leftNode;

    private Node rightNode;

    private Node parentNode;

    private int freq;

    private char binaryFlag;

    private String encodingString;

    private boolean isLeaf;

    /**
     * Constructor for a leaf node, a node with a char and no children
     * @param aChar character
     */
    public Node(char aChar) {
        this.aChar = aChar;
        this.freq = 1;
        this.isLeaf = true;
    }

    /**
     * Constructor for a leaf node, when the frequency is known on creation
     * <p>
     * @param aChar the character the node represents
     * @param freq the frequency the character appears
     */
    public Node(char aChar, int freq) {
        this.aChar = aChar;
        this.freq = freq;
        this.isLeaf = true;
    }

    /**
     * Constructor for a non-leaf node, with no char and two children
     * @return
     */
    public Node(Node leftNode, Node rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.freq = leftNode.getFreq() + rightNode.getFreq();
        this.isLeaf = false;
    }

    /**
     * Gets the character assigned to this node
     * @return character
     */
    public char getaChar() {
        return aChar;
    }

    /**
     * Increments the frequency of the node by one
     */
    public void incFreq() {
        freq++;
    }

    /**
     * Gets the frequency of the node
     * @return
     */
    public int getFreq() {
        return freq;
    }

    /**
     * Gets the left child node
     * @return left child node
     */
    public Node getLeftNode() {
        return leftNode;
    }

    /**
     * Gets the right child node
     * @return right child node
     */
    public Node getRightNode() {
        return rightNode;
    }

    /**
     * Sets the binary flag, 0 for a left node, 1 for a right node, or 2 for the root node
     * @param binaryFlag binary flag char
     */
    public void setBinaryFlag(char binaryFlag) {
        this.binaryFlag = binaryFlag;
    }

    /**
     * Gets the binary flag
     * @return binary flag character
     */
    public char getBinaryFlag() {
        return binaryFlag;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public String getEncodingString() {
        return encodingString;
    }

    public void setEncodingString(String encodingString) {
        this.encodingString = encodingString;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

}
