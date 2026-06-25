package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SobelEdgeDetectionTest {

    private final ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
    private final SobelEdgeDetection algorithm = new SobelEdgeDetection(grayscaleAlgorithm);

    @Test
    void testProcessNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.process(null),
                "Image is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testProcessPreservesImageDimensions() {
        BufferedImage image = new BufferedImage(4, 6, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = algorithm.process(image);

        assertEquals(4, result.getWidth());
        assertEquals(6, result.getHeight());
    }

    @Test
    void testProcessUniformImageProducesNoEdges() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.setRGB(x, y, new Color(100, 100, 100).getRGB());
            }
        }

        BufferedImage result = algorithm.process(image);

        int rgb = result.getRGB(1, 1);
        int gray = rgb & 0xFF;
        assertEquals(0, gray);
    }

    @Test
    void testProcessDetectsVerticalEdge() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            image.setRGB(0, y, new Color(0, 0, 0).getRGB());
            image.setRGB(1, y, new Color(0, 0, 0).getRGB());
            image.setRGB(2, y, new Color(255, 255, 255).getRGB());
        }

        BufferedImage result = algorithm.process(image);

        int rgb = result.getRGB(1, 1);
        int gray = rgb & 0xFF;
        assertEquals(255, gray);
    }

    @Test
    void testProcessResultChannelsAreEqual() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.setRGB(x, y, new Color(x * 50, y * 50, 10).getRGB());
            }
        }

        BufferedImage result = algorithm.process(image);

        int rgb = result.getRGB(1, 1);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        assertEquals(red, green);
        assertEquals(green, blue);
    }
}