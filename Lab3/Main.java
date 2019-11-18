import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class Main {
 static  File user1File = new File("C:\\Users\\User\\Desktop\\save1.ser");
 static File user2File = new File("C:\\Users\\User\\Desktop\\save2.ser");
/*   static File user1EncryptedMessage = new File("C:\\Users\\User\\Desktop\\user1MessageEncrypted.txt");
   static File user2EncryptedMessage = new File("C:\\Users\\User\\Desktop\\user2MessageEncrypted.txt");*/

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        User user1 = new User();
        User user2 = new User();
        System.out.println("user1 :");
        String user1Message = user1.createMessage();
        System.out.println("Generated message for this user: " + user1Message);
        System.out.println("user2 :");
        String user2Message = user2.createMessage();
        System.out.println("Generated message for this user: " + user2Message);
        try {
            KeyPair user1Pair = user1.generateKeys();
            user1.setPrivateKey(user1Pair.getPrivate());
            user1.storePublicKeys(user1File,user1Pair);

            KeyPair user2Pair = user2.generateKeys();
            user2.setPrivateKey(user2Pair.getPrivate());
            user1.storePublicKeys(user2File,user2Pair);

            Key publicUser2Key = user1.deserializeKey(user2File);
            Key publicUser1Key = user2.deserializeKey(user1File);

           byte[] encryptedMessage1 = user1.encryptMessage(user1Message,publicUser2Key);
            user2.decryptMessage(encryptedMessage1,user2.getPrivateKey());

            byte[] encryptedMessage2 = user2.encryptMessage(user2Message,publicUser1Key);
            user1.decryptMessage(encryptedMessage2,user1.getPrivateKey());


        } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
