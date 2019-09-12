package vigenere;

public class View {
    public static final String GENERAL_INFO = "This program can encrypt and decrypt texts using Vigenere algorithm.";
    public static final String CHOOSE_FILE = "Choose file from your desktop:";
    public static final String CHOOSE_MODE = "Press 1  to encrypt text , 2 to decrypt, 3 for Babbage attack on Vigenere cipher.";
    public static final String ENTER_KEYWORD = "Please,enter keyword:";
    public static final String WRONG_INPUT="Wrong input! Try again,please.";

    public  void printMessage(String message) {
        System.out.println(message);
    }
}
