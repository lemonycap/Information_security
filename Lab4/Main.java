import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.Random;


public class Main {
    private static int keyLength = 256;
    static  File user1File = new File("C:\\Users\\User\\Desktop\\save1.ser");
    static  File restoredKey = new File("C:\\Users\\User\\Desktop\\key.ser");
    public static void main(String [] args) throws IOException, ClassNotFoundException,NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, ShortBufferException,
            BadPaddingException, IllegalBlockSizeException {
        String str = "Hello world";
        String shaEncryptedString;
        User user1 = new User();
        User user2 = new User();

           shaEncryptedString = encryptSHA1(str);
           System.out.println("Original hash: " + shaEncryptedString);
            AES aes = new AES();
            Random rng  = new Random();
            aes.setKey(rng,keyLength);
            aes.generateAESKey(aes.getKey());
            String encryptedKey = Base64.getEncoder().encodeToString(aes.getSecretKey().getEncoded());
            byte[] encryptedText =  aes.encrypt(shaEncryptedString,aes.getSecretKey());

            KeyPair user1Pair = user1.generateKeys();
            user1.setPrivateKey(user1Pair.getPrivate());
            user1.storePublicKeys(user1File,user1Pair);
            Key publicUser1Key = user2.deserializeKey(user1File);
            byte[] encryptedAESKey = user2.encryptMessage(encryptedKey,publicUser1Key);

          String decryptedAESKey =  user1.decryptMessage(encryptedAESKey,user1.getPrivateKey());
            byte[] decodedKey = Base64.getDecoder().decode(decryptedAESKey);
           SecretKeySpec originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

           byte[] decryptedMessage =  aes.decrypt(encryptedText, originalKey);
           System.out.println("Decrypted hash:" + new String(decryptedMessage));




    }

    public static String encryptSHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] result = messageDigest.digest(input.getBytes());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < result.length; ++i) {
            buffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return buffer.toString();
    }
}
