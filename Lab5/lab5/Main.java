package lab5;

import lab5.Consts;
import lab5.ImageSteganography;

public class Main {
    public static void main(String[] args) {
        String message = Consts.MESSAGE;
        String path = Consts.PATH_ORIGIN;
        String pathHidden = Consts.PATH_HIDDEN;

        boolean isSucc = ImageSteganography.toImg(message, path);
        if (isSucc)
            System.out.println("Data was successfully saved in the image.");
        else
            System.out.println("Something went wrong");

        System.out.println("The length of message :  " + message.length());
        String text = ImageSteganography.fromImg(pathHidden);
        System.out.println("Decoded text : \n\t" + text);
    }
}
