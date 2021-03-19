package ecm1414.coursework.joshuaprout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Static methods for reading and writing encoded and non encoded files
 */
public class FileHandler {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);


        boolean validFile = false;
        String inputString = "";

        // Gets file to create a tree for
        while (validFile == false) {
            System.out.println("Enter the path of the file to create a tree for:");
            String inputFilePath = input.nextLine();
            try {
                inputString = readUncompressed(inputFilePath);
                validFile = true;
            } catch (FileNotFoundException F) {
                System.out.println("File not found");
                validFile = false;
            }
        }

        // Creates huffman tree object
        HuffmanTree tree = new HuffmanTree();

        // Creates Nodes
        tree.createNodes(inputString);

        //Creates huffman tree
        tree.makeTree();
        tree.makeCodes();

        //Encodes string using the tree
        String encodedString = tree.encodeString(inputString);
        //System.out.println(encodedString);


        // Writes to file
        //writeCompressed(encodedString);

        // Reads from compressed file
        //String readCompressedString = readCompressed();

        // Decompress
        //System.out.println(tree.decodeString(readCompressedString));

    }

    /**
     * Converts a binary string into an array of bytes
     * @param binaryString
     * @return
     */
    public static byte[] decodeBinary(String binaryString) {
        if (binaryString.length() % 8 != 0) throw new IllegalArgumentException(
                "Binary data length must be multiple of 8");
        byte[] data = new byte[binaryString.length() / 8];
        for (int i = 0; i < binaryString.length(); i++) {
            char c = binaryString.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid char in binary string");
            }
        }
        return data;
    }

    /**
     * Reads plaintext file to be compressed
     *
     * @param filename
     * @return
     */
    public static String readUncompressed(String filename) throws FileNotFoundException {
        // Loads input data
        String inputString = "";
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                inputString += myReader.nextLine() + "\n";
            }
            //System.out.println(inputString);
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found");
        }
        return inputString;
    }


    /**
     * Writes the huffman encoded binary string to file
     * <p>
     * The string is padded with 0s at the end, to make the string length divisible by 8 to be written to the file properly
     * The first 3 bits of the binary string store the number of bits added to the end of the string
     *
     * @param encodedString
     */
    public static void writeCompressed(String encodedString, String filePath) throws IOException{


        /*The string needs to be divisible by 8, to be written to the file. To achieve this, the string is padded
        with zeros at the end. To tell the decoder at what point the string ends and the padding begins, 3 bits are
        reserved at the front of the binary string, to signify how many padding bits have been added.
         */

        // Finds string length, with 3 added for the padding indicator
        int stringLength = encodedString.length() + 3;
        //System.out.println("String length " + stringLength);

        // Divides by 8, the modulus will be the number of bits to add at the end
        int zerosToAdd = 8 - (stringLength % 8);
        if (zerosToAdd != 8) {
            // Adds the padding zeros
            for (int i = 0; i < zerosToAdd; i++) {
                encodedString += "0";
            }
        }
        // Calculates the indication bits
        String paddingIndicator = Integer.toBinaryString(zerosToAdd);
        paddingIndicator = String.format("%1$" + "3" + "s", paddingIndicator).replace(' ', '0');
        paddingIndicator = paddingIndicator.substring(paddingIndicator.length() - 3, paddingIndicator.length());
        //System.out.println("Padding indicator: " + paddingIndicator);

        // Adds the indication bits to the encoded string
        encodedString = paddingIndicator + encodedString;

        //Splits string into chunks of length 8



        //System.out.println(encodedString);

        //
        byte[] data = decodeBinary(encodedString);

        File file = new File(filePath);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(data);
        } catch (IOException e) {
            throw new IOException("IO Exception");
        }
    }

    /**
     * Reads a compressed file
     * @return
     */
    public static String readCompressed(String filePath) throws IOException {

        // Reads binary string from output file
        File file = new File(filePath);
        StringBuilder inputString = new StringBuilder();
        try {
            byte[] inData = Files.readAllBytes(file.toPath());
            for (byte inB : inData) {
                inputString.append(String.format("%8s", Integer.toBinaryString(inB & 0xFF)).replace(' ', '0'));
            }

        } catch (IOException e) {
            throw new IOException("File could not be read");
        }

        String paddingIndicator = inputString.substring(0, 3);
        int padding = Integer.parseInt(paddingIndicator, 2);

        return inputString.substring(3, inputString.length() - padding);
    }

    /**
     * Writes a decompressed file
     * @param decompressed
     */
    public static void writeDecompressed(String decompressed, String filePath) throws IOException{

        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(decompressed);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new IOException("IO Exception");
        }
    }

    /**
     * Writes the frequency of each character string to a file
     * @param charFreqs
     */
    public static void writeCharacterFreqs(String charFreqs, String filePath) throws IOException{
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(charFreqs);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new IOException("IO Exception");
        }
    }

    public static String readPlainText(String filePath) throws IOException {
        // Loads input data
        String text = "";
        try {
            text = Files.readString(Paths.get(filePath));
        } catch (IOException i) {
            throw new IOException("File could not be read");
        }
        return text;
    }
}