package lab2;


import java.security.Key;
import java.security.Security;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Main {
    static String keyWord = "keyword1";
    static byte[] keyBytes = keyWord.getBytes();

    public static void main(String []args) {
        System.out.println("Enter string:");
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        byte[] input = inputString.getBytes();
        try {
            System.out.println("DES ECB: \n" );
            DesECB(keyBytes,input);
            System.out.println("\nTripleDES ECB: \n" );
            TripleDES(keyBytes,input);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void DesECB(byte[] keyBytes,byte[] input) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC");
        System.out.println("input : " + new String(input));

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        System.out.println("cipher: " + new String(cipherText).getBytes("UTF-8").toString());

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        System.out.println("plain : " + new String(plainText));
    }


    public static void TripleDES(byte[] keyBytes,byte[] input) throws Exception {
        byte[] keyBytes1 = Arrays.copyOf(keyBytes, 24);
        SecretKey securekey = new SecretKeySpec(keyBytes1,"DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        System.out.println("input : " + new String(input));
        cipher.init(Cipher.ENCRYPT_MODE, securekey);

        byte[] buf = cipher.doFinal(input);
        System.out.println("cipher : " + new String(buf).getBytes("UTF-8").toString());

        Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, securekey);

        byte[] plainText = decipher.doFinal(buf);
        System.out.println("plain : " + new String(plainText));

    }
}
