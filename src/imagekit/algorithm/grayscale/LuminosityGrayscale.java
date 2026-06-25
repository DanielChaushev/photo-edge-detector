package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    public LuminosityGrayscale() {

    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null!");
        }
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int grayscale = (int) (Math.round(0.21 * red + 0.72 * green + 0.07 * blue));
                if (grayscale < 0) {
                    grayscale = 0;
                }
                if (grayscale > 255) {
                    grayscale = 255;
                }
                int resultRGB = (grayscale << 16) | (grayscale << 8) | grayscale;
                result.setRGB(x, y, resultRGB);
            }
        }
        return result;
    }
}
