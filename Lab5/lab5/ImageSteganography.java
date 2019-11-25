package lab5;

import lab5.Consts;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageSteganography {
    /**
     * Save data into image.
     *
     * @param data - the saved data
     * @param imagePath image path(only support png and jpg)
     * @return true: succeed false: failed
     */
    public static boolean toImg(String data, String imagePath) {
        File imageFile = new File(imagePath);
        File newImageFile = new File(Consts.PATH_HIDDEN);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imageFile);
            BufferedImage image = Utils.streamToImage(inputStream);

            final int imageLength = image.getHeight() * image.getWidth();
            final int startingOffset = Utils.calculateStartingOffset(Consts.PASSWORD, imageLength);

            // hide text
            Steganography steganography = new Steganography();
            steganography.encode(image, data, startingOffset);
            return ImageIO.write(image, Utils.getFileExt(imageFile), newImageFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file " + imagePath, e);
        } catch (IOException | SteganographyException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }


    /**
     * Get the data from image.
     *
     * @param imagePath image path.
     * @return String text
     */

    public static String fromImg(String imagePath) {
        File imageFile = new File(imagePath);
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(imageFile);
            BufferedImage image = Utils.streamToImage(inputStream);

            final int imageLength = image.getWidth() * image.getHeight();
            final int startingOffset = Utils.calculateStartingOffset(Consts.PASSWORD, imageLength);

            Steganography steganography = new Steganography();

            return steganography.decode(image, startingOffset);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file " + imagePath, e);
        } catch (IOException | SteganographyException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
