package vigenere;

public class Model {
    private String keyword;
    private char [] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private int alphabetLength = alphabet.length - 1;
    private double indexOfCoindence = 0.0667;
    private char theMostCommonLetter = 'E';

    public char getTheMostCommonLetter() {
        return theMostCommonLetter;
    }

    public double getIndexOfCoindence() {
        return indexOfCoindence;
    }

    public int getAlphabetLength() {
        return alphabetLength;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public int findIndexInTheAlphabet (char symbol) {
        int index = 0;
        for (int i = 0; i < alphabet.length; ++i) {
            if (symbol == alphabet[i]) {
                index = i;
            }
        }
        return  index;
    }

    public char getLetterFromTheAlphabet (int code) {
        return alphabet[code];
    }
}
