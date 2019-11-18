import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.util.Random;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class User {
private Key privateKey;

    public void setPrivateKey(Key privateKey) {
        this.privateKey = privateKey;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    public static String createMessage () {
    byte[] array = new byte[100];
    new Random().nextBytes(array);
    String generatedString = array.toString();
    return generatedString;
}

public static KeyPair  generateKeys() throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    KeyPair keyPair = generator.generateKeyPair();
    return keyPair;
}

public static void storePublicKeys(File file,KeyPair keyPair) throws IOException{
    Key publicKey = keyPair.getPublic();

    FileOutputStream outputStream = new FileOutputStream(file);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(publicKey);
    outputStream.close();
    objectOutputStream.close();
}

public static Key deserializeKey(File file) throws IOException,ClassNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(file);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    Key sentPublicKey = (Key) objectInputStream.readObject();
    return  sentPublicKey;
    }

 public static byte[] encryptMessage(String message,Key publicUserKey) throws IOException, NoSuchPaddingException,
         NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
     byte[] input  = message.getBytes();
     Cipher cipher = Cipher.getInstance("RSA");
     cipher.init(Cipher.ENCRYPT_MODE,publicUserKey);
     byte[] buf = cipher.doFinal(input);
     System.out.println("Encrypted message: " + String.valueOf(buf));
     return buf;
     /*FileWriter fileWriter = new FileWriter(file);
     fileWriter.write(String.valueOf(buf));
     fileWriter.close();*/
 }

 public static void decryptMessage(byte[] message,Key privateUserKey) throws IOException, NoSuchPaddingException,
         NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
     //System.out.println("Encrypted message: " + message);
    // byte[] bytes = message.getBytes();
     Cipher cipher = Cipher.getInstance("RSA");
     cipher.init(Cipher.DECRYPT_MODE,privateUserKey);
     byte[] buffer = cipher.doFinal(message);
     System.out.println("Decrypted message: " +new String(buffer,UTF_8));
 }

}
