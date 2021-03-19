package ecm1414.coursework.joshuaprout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String choice = "";

        while (!choice.equals("-1")) {
            //System.out.println("Press 1 to create a compression tree");
            System.out.println("Press 1 to compress file");
            System.out.println("Press 2 to decompress file");
            System.out.println("Press -1 to quit");
            choice = input.nextLine();

            switch (choice) {
                case "1":
                    compressFile();
                    break;
                case "2":
                    decompressFile();
                    break;
            }
        }
    }

    public static void compressFile() {
        Scanner input = new Scanner(System.in);
        boolean validFile = false;
        String inputString = "";

        // Gets file to create a tree for
        while (!validFile) {
            System.out.println("Enter the path of the file to compress:");
            String inputFilePath = input.nextLine();
            try {
                inputString = FileHandler.readUncompressed(inputFilePath);
                validFile = true;
            } catch (FileNotFoundException F) {
                System.out.println("File not found");
                validFile = false;
            }
        }

        // Creates huffman tree object
        HuffmanTree tree = new HuffmanTree();

        System.out.println("Press 1 to create a new encoding tree, press 2 to load an existing tree");
        String choice = input.nextLine();
        switch (choice) {
            case "1":

                // Creates Nodes for each character
                tree.createNodes(inputString);

                // Creates file for storing character frequencies
                validFile = false;
                while (!validFile) {
                    System.out.println("Enter the path and filename of the character frequencies to save:");
                    try {
                        String outputFilePath = input.nextLine();
                        FileHandler.writeCharacterFreqs(tree.getCharsFreq(), outputFilePath);
                        validFile = true;
                    } catch (IOException I) {
                        System.out.println("File could not be created");
                        validFile = false;
                    }
                }


                break;

            case "2":
                // Read char frequency file
                validFile = false;
                String charFreqs = "";

                while (!validFile) {
                    System.out.println("Enter the path and filename of the character frequencies file to use");
                    try {
                        String filePath = input.nextLine();
                        charFreqs = FileHandler.readPlainText(filePath);
                        validFile = true;
                    } catch (IOException i) {
                        System.out.println("File could not be read");
                        validFile = false;
                    }
                }

                tree.createNodesFromFreqString(charFreqs);
                break;
        }


        //Organises nodes into tree
        tree.makeTree();

        // Finds huffman codes for each node
        tree.makeCodes();

        //Encodes string using the tree
        String encodedString = tree.encodeString(inputString);

        // Writes to file
        validFile = false;

        // Creates file to write compressed data to
        while (!validFile) {
            System.out.println("Enter the path and filename of the compressed file to save:");
            try {
                String outputFilePath = input.nextLine();
                FileHandler.writeCompressed(encodedString, outputFilePath);
                validFile = true;
            } catch (IOException I) {
                System.out.println("File could not be created");
                validFile = false;
            }
        }
    }

    public static void decompressFile() {
        // Read file to decompress

        String compressedString = "";
        boolean validFile = false;
        Scanner input = new Scanner(System.in);

        while (!validFile) {
            System.out.println("Enter the path and filename of the compressed file to read:");
            try {
                String filePath = input.nextLine();
                compressedString = FileHandler.readCompressed(filePath);
                validFile = true;
            } catch (IOException i) {
                System.out.println("File could not be read");
                validFile = false;
            }
        }

        // Read char frequency file
        validFile = false;
        String charFreqs = "";

        while (!validFile) {
            System.out.println("Enter the path and filename of the character frequencies file to decompress");
            try {
                String filePath = input.nextLine();
                charFreqs = FileHandler.readPlainText(filePath);
                validFile = true;
            } catch (IOException i) {
                System.out.println("File could not be read");
                validFile = false;
            }
        }
        // Create tree, can be copied

        // Creates huffman tree object
        HuffmanTree tree = new HuffmanTree();

        // Creates Nodes
        tree.createNodesFromFreqString(charFreqs);

        //Creates huffman tree
        tree.makeTree();
        tree.makeCodes();

        // Decompress
        String decompressedString = tree.decodeString(compressedString);

        // Save decompressed file
        validFile = false;

        while (!validFile) {
            System.out.println("Enter the path and filename for the decompressed file");
            try {
                String filePath = input.nextLine();
                FileHandler.writeDecompressed(decompressedString, filePath);
                validFile = true;
            } catch (IOException I) {
                System.out.println("File could not be read");
                validFile = false;
            }
        }
    }
}
