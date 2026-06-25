package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalFileSystemImageManagerTest {

    private final LocalFileSystemImageManager manager = new LocalFileSystemImageManager();

    @TempDir
    File tempDir;

    private BufferedImage createSampleImage() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 0, 0).getRGB());
        image.setRGB(1, 1, new Color(0, 255, 0).getRGB());
        return image;
    }

    @Test
    void testLoadImageNullFileThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null),
                "File is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testLoadImageNonExistentFileThrowsIOException() {
        File missingFile = new File(tempDir, "does-not-exist.png");
        assertThrows(IOException.class, () -> manager.loadImage(missingFile),
                "File does not exist and IOException must be thrown!");
    }

    @Test
    void testLoadImageDirectoryInsteadOfFileThrowsIOException() {
        assertThrows(IOException.class, () -> manager.loadImage(tempDir),
                "File is a directory and IOException must be thrown!");
    }

    @Test
    void testLoadImageValidFileReturnsBufferedImage() throws IOException {
        File imageFile = new File(tempDir, "sample.png");
        ImageIO.write(createSampleImage(), "png", imageFile);

        BufferedImage loaded = manager.loadImage(imageFile);

        assertNotNull(loaded);
        assertEquals(2, loaded.getWidth());
        assertEquals(2, loaded.getHeight());
    }

    @Test
    void testLoadImagesFromDirectoryNullDirectoryThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImagesFromDirectory(null),
                "Directory is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testLoadImagesFromDirectoryNonExistentDirectoryThrowsIOException() {
        File missingDir = new File(tempDir, "missing-folder");
        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(missingDir),
                "Directory does not exist and IOException must be thrown!");
    }

    @Test
    void testLoadImagesFromDirectoryFileInsteadOfDirectoryThrowsIOException() throws IOException {
        File notADirectory = new File(tempDir, "file.png");
        ImageIO.write(createSampleImage(), "png", notADirectory);

        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(notADirectory),
                "Given path is a file, not a directory, and IOException must be thrown!");
    }

    @Test
    void testLoadImagesFromDirectoryReturnsAllImages() throws IOException {
        ImageIO.write(createSampleImage(), "png", new File(tempDir, "first.png"));
        ImageIO.write(createSampleImage(), "png", new File(tempDir, "second.png"));

        var images = manager.loadImagesFromDirectory(tempDir);

        assertEquals(2, images.size());
    }

    @Test
    void testSaveImageNullImageThrowsIllegalArgumentException() {
        File target = new File(tempDir, "out.png");
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, target),
                "Image is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testSaveImageNullFileThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(createSampleImage(), null),
                "File is null and IllegalArgumentException must be thrown!");
    }

    @Test
    void testSaveImageAlreadyExistingFileThrowsIOException() throws IOException {
        File target = new File(tempDir, "car.jpg");
        ImageIO.write(createSampleImage(), "jpg", target);

        assertThrows(IOException.class, () -> manager.saveImage(createSampleImage(), target),
                "File already exists and IOException must be thrown!");
    }

    @Test
    void testSaveImageMissingParentDirectoryThrowsIOException() {
        File target = new File(tempDir, "missing-folder/out.png");
        assertThrows(IOException.class, () -> manager.saveImage(createSampleImage(), target),
                "Parent directory does not exist and IOException must be thrown!");
    }

    @Test
    void testSaveImageValidFileIsCreatedOnDisk() throws IOException {
        File target = new File(tempDir, "saved.png");

        manager.saveImage(createSampleImage(), target);

        assertTrue(target.exists());
    }
}