package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {

    private final LuminosityGrayscale algorithm = new LuminosityGrayscale();

    @Test
    void testProcessNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.process(null), "Image is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testProcessRedPixelConvertsCorrectly() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 0, 0).getRGB());

        BufferedImage result = algorithm.process(image);

        int expectedGray = 54;
        int rgb = result.getRGB(0, 0);
        int actualRed = (rgb >> 16) & 0xFF;
        int actualGreen = (rgb >> 8) & 0xFF;
        int actualBlue = rgb & 0xFF;

        assertEquals(expectedGray, actualRed);
        assertEquals(expectedGray, actualGreen);
        assertEquals(expectedGray, actualBlue);
    }

    @Test
    void testProcessWhitePixelStaysWhite() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 255, 255).getRGB());

        BufferedImage result = algorithm.process(image);

        int rgb = result.getRGB(0, 0);
        int actualGray = rgb & 0xFF;
        assertEquals(255, actualGray);
    }

    @Test
    void testProcessPreservesImageDimensions() {
        BufferedImage image = new BufferedImage(3, 5, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = algorithm.process(image);

        assertEquals(3, result.getWidth());
        assertEquals(5, result.getHeight());
    }
}