package lab5;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private Utils() {
        // Prevent initialization
    }

    public static boolean isEmpty(final String value) {
        String EMPTY_STRING = "";
        return (null == value || EMPTY_STRING.equalsIgnoreCase(value.trim()));
    }

    /**
     * Gets an image from the given stream
     *
     * @param stream Stream to get the image from
     * @return The extracted image
     */
    public static BufferedImage streamToImage(InputStream stream) throws IOException {
        return ImageIO.read(stream);
    }

    /**
     * Calculates the index of the starting byte
     *
     * @param password The password given from the user
     * @param maxValue The length of the image
     * @return The index of the starting byte
     */
    public static int calculateStartingOffset(final String password, final long maxValue) {
        int offset = 0;

        if (!Utils.isEmpty(password)) {
            for (char c : password.toCharArray()) {
                offset += c;
            }

            offset %= maxValue;
        }

        return offset;
    }

    /**
     * Get file extension.
     *
     * @param image image file
     * @return file's extension
     */
    public static String getFileExt(File image) {
        String name = image.getName();
        int lastDotIndex = name.indexOf(".");
        return name.substring(lastDotIndex + 1);
    }
}
