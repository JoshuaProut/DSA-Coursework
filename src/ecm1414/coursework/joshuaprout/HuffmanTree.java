package ecm1414.coursework.joshuaprout;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a huffman tree for a given text file.
 *
 * Consists of a binary tree of Node objects, with each node pointing to their subnodes.
 */
public class HuffmanTree {

    private Node rootNode;

    private ArrayList<Node> leafNodes;


    /**
     * Constructor, initializes the arraylists
     */
    public HuffmanTree() {
        leafNodes = new ArrayList<>();
    }

    /**
     * Creates the initial leaf nodes for the chars present
     *
     * @param string The string read from the file
     */
    public void createNodes(String string) {

        //Iterates through each char in the string
        char[] chars = string.toCharArray();
        for (char thechar: chars) {
            boolean nodeFound = false;

            // Searches the node list to see if the char already has a node
            for (Node node : leafNodes) {
                // If char does have a node, increase node's frequency by 1
                if (node.getaChar() == thechar) {
                    node.incFreq();
                    nodeFound = true;
                    break;
                }
            }
            // If node was not found, create one
            if (!nodeFound) {
                Node newNode = new Node(thechar);

                // Adds to the nodes and leafNodes list
                leafNodes.add(newNode);
            }
        }
    }

    /**
     * Creates nodes from a character frequency string read from file
     *
     * @param freqString Character frequency string read from file in format: the character followed by frequency
     *                   in the file, then each character frequency pair is seperated by unicode char uE000
     */
    public void createNodesFromFreqString(String freqString) {

        // Splits string into char and freq pairs
        String[] charFreqs = freqString.split("\uE000");

        for (String charFreq: charFreqs) {
            char aChar = charFreq.charAt(0);
            int freq = Integer.parseInt(charFreq.substring(1));
            Node newNode = new Node(aChar, freq);
            leafNodes.add(newNode);
        }
    }


    /**
     * Creates the tree structure from the list of leaf nodes.
     * Sets the HuffmanTree rootnode, as the root node of the tree.
     */
    public void makeTree() {

        // Adds all leaf nodes to queue
        ArrayList<Node> queue = new ArrayList(leafNodes);

        // While there is more than one node in the queue
        while (queue.size() > 1) {
            // Sorts queue so that nodes with the lowest priority are at the front
            queue.sort(Comparator.comparing(Node::getFreq));

            // Removes the two nodes with the lowest frequency from the array
            Node firstNode = queue.get(0);
            firstNode.setBinaryFlag('0');
            queue.remove(0);

            Node secondNode = queue.get(0);
            secondNode.setBinaryFlag('1');
            queue.remove(0);

            // Creates internal node and adds to queue
            Node newNode = new Node(firstNode, secondNode);
            queue.add(newNode);

            // Sets the parent for the two nodes
            firstNode.setParentNode(newNode);
            secondNode.setParentNode(newNode);

        }

        // The remaining node in the queue is the root node
        rootNode = queue.get(0);
        // A binary flag of 2 signifies the root node when creating codes
        rootNode.setBinaryFlag('2');
    }

    /**
     * For each of the leaf nodes, uses the tree to find their encoding string and set it to the respective leaf node.
     */
    public void makeCodes() {
        ArrayList<Character> encodingChars = new ArrayList<>();

        // For each leafnode, therefore each character, moves upwards through the tree to find their encoding code
        for (Node node : leafNodes) {
            Node currentNode = node;

            //The node with a Binary Flag of 2 is the root node
            while (currentNode.getBinaryFlag() != '2') {
                encodingChars.add(currentNode.getBinaryFlag());
                currentNode = currentNode.getParentNode();
            }

            // Reverses the string, as the tree was read backwards
            Collections.reverse(encodingChars);
            StringBuilder encodingString = new StringBuilder();
            for (Character character : encodingChars) {
                encodingString.append(character);
            }
            node.setEncodingString(encodingString.toString());
            encodingChars.clear();
            System.out.println(node.getaChar() + " | " + node.getEncodingString());
        }
    }

    /**
     * Encodes the plaintext string, using the huffman tree
     *
     * @param string the original string to be encoded
     * @return an encoded binary string
     */
    public String encodeString(String string) {

        //Adds each node to a hashmap
        Map<Character, String> map = new HashMap<>();
        for (Node node : leafNodes) {
            map.put(node.getaChar(), node.getEncodingString());
        }
        System.out.println("Mapped");

        // Maps each char to its encoding string
        char[] chars = string.toCharArray();
        StringBuilder encodedString = new StringBuilder();
        for (char thechar : chars) {
            String code = map.get(thechar);

            // If the string to encode contains a character not present in the map, replace with the code for a comma
            if (code == null) {
                encodedString.append(map.get(','));
            } else {
                encodedString.append(map.get(thechar));
            }
        }
        System.out.println("encoded");
        return encodedString.toString();
    }

    /**
     * Decodes a binary string to plaintext.
     * <p>
     * Must use the same tree that was used for encoding.
     *
     * @param binString A binary string encoded with the tree
     * @return A decoded plaintext string
     */
    public String decodeString(String binString) {
        StringBuilder decodedString = new StringBuilder();
        Node currentNode = rootNode;
        char[] chars = binString.toCharArray();
        // Iterates through each char in the binary string
        for (char c : chars) {
            // If a leaf node has been reached
            if (currentNode.getIsLeaf()) {
                decodedString.append(currentNode.getaChar());
                currentNode = rootNode;
            }
            // If the char is 0, move to the left child
            else if (c == '0') {
                currentNode = currentNode.getLeftNode();
                if (currentNode.getIsLeaf()) {
                    decodedString.append(currentNode.getaChar());
                    currentNode = rootNode;
                }
            }
            // If the char is 1, move to the right child
            else if (c == '1') {
                currentNode = currentNode.getRightNode();
                if (currentNode.getIsLeaf()) {
                    decodedString.append(currentNode.getaChar());
                    currentNode = rootNode;
                }
            }
        }
        return decodedString.toString();
    }

    /**
     * Creates a string of each character and it's frequency.
     *
     * The string of characters and their frequencies can be used to reconstruct the huffman tree from a saved file,
     * and can be used for decompressing. The string is in the structure of the character followed by the number of times
     * it appears in the file, each character and frequency pair.
     *
     * @return frequency of characters string
     */
    public String getCharsFreq() {
        StringBuilder freqString = new StringBuilder();
        for (Node node : leafNodes) {
            freqString.append(Character.toString(node.getaChar()) + Integer.toString(node.getFreq()) + "\uE000");
        }
    return freqString.toString();
    }

}








