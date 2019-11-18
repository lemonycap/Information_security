import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;


public class AES {
    private static SecretKeySpec secret;

    public static byte[] getKey() {
        return key;
    }

    private static byte[] key;
    byte[] cipher;

    public static void setKey(Random rng, int length) {
        String vocab = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = vocab.charAt(rng.nextInt(vocab.length()));
        }
        key = new String(text).getBytes();

    }

    public static SecretKeySpec getSecretKey() {
        return secret;
    }

    public static SecretKeySpec generateAESKey(byte[] key)  {
        byte[] keyArray = Arrays.copyOf(key,16);
        secret = new SecretKeySpec(keyArray, "AES");
        return secret;
    }

    public static byte[] encrypt(String input,SecretKeySpec secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] inputBytes = input.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
       // System.out.println("Input : " + new String(input));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = new byte[cipher.getOutputSize(inputBytes.length)];
        int ctLength = cipher.update(inputBytes, 0, inputBytes.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
       // System.out.println("Cipher: " + new String(cipherText).getBytes("UTF-8").toString());
        return cipherText;
    }

    public static byte[] decrypt(byte[] cipher,SecretKeySpec secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipherDecrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainText = new byte[cipherDecrypt.getOutputSize(cipher.length)];
        plainText = cipherDecrypt.doFinal(cipher);
       // System.out.println("Decrypted text : " + new String(plainText));
        return plainText;
    }

}
