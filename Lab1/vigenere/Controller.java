package vigenere;

import javax.swing.*;
import java.io.*;
import java.util.*;


import static vigenere.RegexContainer.REGEX_KEYWORD_PARAMS;
import static vigenere.RegexContainer.REGEX_PROGRAM_MODE;

public class Controller {

    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;       //view = aView;
        this.model = model;
    }

    public void chooseAndProcessFileFromDesktop() throws IOException {
        view.printMessage(View.GENERAL_INFO);
        view.printMessage(View.CHOOSE_FILE);

        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Select file");
        File file = null;
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileopen.getSelectedFile();
        }

        FileReader fr = new FileReader(file);
        Scanner scan = new Scanner(fr);
        while (scan.hasNextLine())
            System.out.println(scan.nextLine());
        fr.close();

        view.printMessage(View.CHOOSE_MODE);
        String answer = inputCheck(REGEX_PROGRAM_MODE);
        System.out.print("Answer: " + answer + "\n");



        if (answer.equals("1")) {
            ArrayList<Character> keySequence = enterKeyword(file);
            vigenereEncrypt(file, keySequence);
        }
        else if (answer.equals("2")) {
            ArrayList<Character> keySequence = enterKeyword(file);
            vigenereDecrypt(file, keySequence);

        }
        else
            babbageAttack(file);


    }

    public String inputCheck(String regex) {
        Scanner sc = new Scanner(System.in);
        String buff = "";
        while (!((sc.hasNext()) && ((buff = sc.next()).matches(regex))))
            view.printMessage(View.WRONG_INPUT);

        return buff;
    }


    public void vigenereEncrypt(File file, ArrayList<Character> keySequence) throws IOException {
        System.out.print("\n");
        FileWriter fr = new FileWriter("C://Users//User//Desktop//TestEncryptedFile.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int buff = bufferedReader.read();
        int count = 0;
        try {
            while (buff != -1 && count < keySequence.size()) {
                int letterCode = (model.findIndexInTheAlphabet((char) buff) + model.findIndexInTheAlphabet(keySequence.get(count))) % model.getAlphabetLength();
                //System.out.println((char) buff + " " + model.findIndexInTheAlphabet((char) buff) + " " +
                     //   model.findIndexInTheAlphabet(keySequence.get(count)) + " " + model.getAlphabetLength() + " " + letterCode);
                fr.write(model.getLetterFromTheAlphabet(letterCode));
                count++;
                buff = bufferedReader.read();
            }
        } finally {
            bufferedReader.close();
            fr.close();
        }
        System.out.print("\n" + "Your encrypted data: ");
        FileReader fileReader = new FileReader("C://Users//User//Desktop//TestEncryptedFile.txt");
        Scanner scan = new Scanner(fileReader);
        while (scan.hasNextLine())
            System.out.println(scan.nextLine());
        fileReader.close();

    }

    public void vigenereDecrypt(File file, ArrayList<Character> keySequence) throws IOException {
        System.out.print("\n");
        FileWriter fr = new FileWriter("C://Users//User//Desktop//TestDecryptedFile.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int buff = bufferedReader.read();
        int count = 0;
        try {
            while (buff != -1 && count < keySequence.size()) {
                int letterCode = (model.findIndexInTheAlphabet((char) buff) + model.getAlphabetLength() - model.findIndexInTheAlphabet(keySequence.get(count))) % model.getAlphabetLength();
             //   System.out.println((char) buff + " " + model.findIndexInTheAlphabet((char) buff) + " " +
              //          model.findIndexInTheAlphabet(keySequence.get(count)) + " " + model.getAlphabetLength() + " " + letterCode);
                fr.write(model.getLetterFromTheAlphabet(letterCode));
                count++;
                buff = bufferedReader.read();
            }
        } finally {
            bufferedReader.close();
            fr.close();
        }
        System.out.print("\n" + "Your decrypted data: ");
        FileReader fileReader = new FileReader("C://Users//User//Desktop//TestDecryptedFile.txt");
        Scanner scan = new Scanner(fileReader);
        while (scan.hasNextLine())
            System.out.println(scan.nextLine());
        fileReader.close();

    }

    public void babbageAttack(File file) throws IOException {
        int textLength = countTextLength(file);
        HashMap<Integer, Double> map = new HashMap<>();
        int possibleKeyLength = 2;
        while (possibleKeyLength < 15) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            int buff = bufferedReader.read();
            ArrayList<Character> buffer = new ArrayList<>();
            int counter = 0;
            while (buff != -1) {
                if (counter % possibleKeyLength == 0)
                    buffer.add((char) buff);
                counter++;
                buff = bufferedReader.read();
            }

            ArrayList<Double> coincedenceIndexesForLetters = new ArrayList<>();
            for (int i = 0; i < model.getAlphabetLength(); ++i) {
                int coincedenceCounter = 0;
                for (int j = 0; j < buffer.size(); ++j) {
                    if (buffer.get(j) == model.getLetterFromTheAlphabet(i)) {
                        coincedenceCounter++;
                    }
                }
                // System.out.print("\n");
                //System.out.println("Letter: " + model.getLetterFromTheAlphabet(i) + " num: " + coincedenceCounter +" " + buffer.size());
                coincedenceIndexesForLetters.add(countCoincedenceIndex(coincedenceCounter, buffer.size()));
                //  System.out.print("\n");
            /*for (int m = 0; m < coincedenceIndexesForLetters.size(); ++m) {
                System.out.print(" Index: " + coincedenceIndexesForLetters.get(m) + " ");
            }*/
            }
            map.put(possibleKeyLength, model.getIndexOfCoindence() - findGeneralCoincedenceNumber(coincedenceIndexesForLetters));
            possibleKeyLength++;
        }
       /* for (Map.Entry entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: "
                    + entry.getValue());
        }*/
        int keyLength = findKeyLength(map);
        System.out.println("Key length: " + keyLength + "\n");
        ArrayList<ArrayList<Character>> groups = createGroups(keyLength, file);
       /* for (int i = 0; i < groups.size(); ++i) {
            for (int j = 0; j < groups.get(i).size(); ++j) {
                System.out.print(groups.get(i).get(j) + " ");
            }
            System.out.print("\n");
        }*/
        ArrayList<Integer> shiftsForEachGroup = new ArrayList<>();
        for (int i = 0; i < groups.size(); ++i) {
            HashMap<Character, Integer> frequencyOfLetters = new HashMap<>();
            for (int j = 0; j < groups.get(i).size(); ++j) {
                for (int m = 0; m < model.getAlphabetLength(); ++m) {
                    if (model.getLetterFromTheAlphabet(m) == groups.get(i).get(j)) {
                        if (frequencyOfLetters.containsKey(model.getLetterFromTheAlphabet(m))) {
                            int val = frequencyOfLetters.get(model.getLetterFromTheAlphabet(m)) + 1;
                            frequencyOfLetters.put(model.getLetterFromTheAlphabet(m), val);
                        } else {
                            frequencyOfLetters.put(model.getLetterFromTheAlphabet(m), 1);
                        }
                    }
                }


            }
           /* for (Map.Entry entry : frequencyOfLetters.entrySet()) {
                System.out.println("Key: " + entry.getKey() + " Value: "
                        + entry.getValue());
            }*/
            shiftsForEachGroup.add(findShift(frequencyOfLetters));
            //System.out.println("Shift for group # " + i + " : " + findShift(frequencyOfLetters));


        }
      // System.out.print("\n");
        /*for (int i = 0; i < shiftsForEachGroup.size(); ++i) {
            System.out.print(shiftsForEachGroup.get(i) + " ");
        }*/
        groups = rewriteGroups(groups,shiftsForEachGroup);
       /* for (int i = 0; i < groups.size(); ++i) {
            System.out.print("\n");
            for (int j = 0; j < groups.get(i).size(); ++j) {
                System.out.print(groups.get(i).get(j) + " ");
            }
        }*/
        System.out.print("\n");
        FileWriter fr = new FileWriter("C://Users//User//Desktop//Test2DecryptedFile.txt");

        int size = Integer.MIN_VALUE;
        for (int i = 0; i < groups.size(); ++i) {
          if (size < groups.get(i).size()) {
              size = groups.get(i).size();
          }
        }
       // System.out.println("\nSize: " + size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < groups.size(); ++j) {
                if (groups.get(j).size() <= i)
                    continue;
                fr.write(groups.get(j).get(i));
            }
        }
        fr.close();
    }

    public double countCoincedenceIndex(int counter, int textLength) {
        return (((double) counter * ((double) counter - 1)) / ((double) textLength * (((double) textLength - 1))));
    }

    public double findGeneralCoincedenceNumber(ArrayList<Double> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); ++i) {
            sum += list.get(i);
        }
        return sum;
    }

    public ArrayList<Character> enterKeyword(File file) throws IOException {
        view.printMessage(View.ENTER_KEYWORD);
        model.setKeyword(inputCheck(REGEX_KEYWORD_PARAMS));
        System.out.println("Keyword: " + model.getKeyword());
        char[] keywordArray = model.getKeyword().toCharArray();
        ArrayList<Character> keySequence = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        int symbol = br.read();
        int counter = 0;
        while (symbol != -1) {
            if (counter == keywordArray.length) {
                counter = 0;
            }
            keySequence.add(keywordArray[counter]);
            counter++;
            char c = (char) symbol;
            symbol = br.read();
        }
        br.close();
        return keySequence;
    }

    public int countTextLength(File file) throws IOException {
        int length = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int buff = bufferedReader.read();
        while (buff != -1) {
            length++;
            buff = bufferedReader.read();
        }
        bufferedReader.close();
        return length;
    }

    public int findKeyLength(HashMap<Integer, Double> map) {
        Double min = Collections.min(map.values());
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == min)
                return (int) entry.getKey();
        }
        return 0;
    }

    public ArrayList<ArrayList<Character>> createGroups(int keyLength, File file) throws IOException {
        ArrayList<ArrayList<Character>> groups = new ArrayList<>();
        for (int i = 0; i < keyLength; ++i) {
            ArrayList<Character> tmp = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            int buff = bufferedReader.read();
            int counter = 0;
            while (counter != i) {
                buff = bufferedReader.read();
                counter++;
            }
            tmp.add((char) buff);
            counter = 0;
            while (buff != -1) {
                counter++;
                buff = bufferedReader.read();
                if (counter == keyLength) {
                    tmp.add((char) buff);
                    counter = 0;
                }
            }
            groups.add(tmp);
            bufferedReader.close();
        }
        return groups;
    }

    public int findShift(HashMap<Character, Integer> map) {
        Integer max = Collections.max(map.values());
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == max) {
                if (model.findIndexInTheAlphabet((char) entry.getKey()) <
                        model.findIndexInTheAlphabet(model.getTheMostCommonLetter())) {
                    return (model.getAlphabetLength() + model.findIndexInTheAlphabet((char) entry.getKey()) -
                            model.findIndexInTheAlphabet(model.getTheMostCommonLetter()));
                } else {
                    return (model.findIndexInTheAlphabet((char) entry.getKey())
                            - model.findIndexInTheAlphabet(model.getTheMostCommonLetter()));
                }

            }
        }
        return 0;
    }

    public ArrayList<ArrayList<Character>> rewriteGroups(ArrayList<ArrayList<Character>> list, ArrayList<Integer> shifts) {
        for (int i = 0; i < list.size(); ++i) {
            for (int j = 0; j < list.get(i).size(); ++j) {
                int tmp = model.findIndexInTheAlphabet(list.get(i).get(j)) - shifts.get(i);
                if (tmp < 0) {
                    list.get(i).set(j, model.getLetterFromTheAlphabet( model.findIndexInTheAlphabet(list.get(i).get(j))
                            - shifts.get(i) + model.getAlphabetLength()));
                } else {
                    list.get(i).set(j, model.getLetterFromTheAlphabet(tmp));
                }
            }
        }
        return list;
    }
}
