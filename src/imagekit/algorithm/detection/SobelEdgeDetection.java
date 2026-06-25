package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private ImageAlgorithm grayscaleAlgorithm;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null!");
        }
        BufferedImage img = grayscaleAlgorithm.process(image);
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gx = 0, gy = 0;
                int[][] kernelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
                int[][] kernelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = getGrayValue(img, x + j, y + i, width, height);
                        gx += pixel * kernelX[i + 1][j + 1];
                        gy += pixel * kernelY[i + 1][j + 1];
                    }
                }
                double G = Math.sqrt(gx * gx + gy * gy);
                int pixelValue = Math.min(255, (int) Math.round(G));
                int rgb = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }

    private int getGrayValue(BufferedImage img, int x, int y, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        return img.getRGB(x, y) & 0xFF;
    }
}
