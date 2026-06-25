package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    public LocalFileSystemImageManager() {

    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("File cannot be null!");
        }
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("File does not exist or is not a regular file!");
        }
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unsupported image format!");
        }
        return image;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Directory is null!");
        }
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("Directory does not exist or is not a regular directory!");
        }
        File[] filesFromDirectory = imagesDirectory.listFiles();
        List<BufferedImage> images = new ArrayList<>();
        for (File file : filesFromDirectory) {
            BufferedImage currentImage = ImageIO.read(file);
            if (currentImage == null) {
                throw new IOException("Unsupported image format!");
            }
            images.add(currentImage);
        }
        return images;

    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image or file is null!");
        }
        File parentDir = imageFile.getParentFile();
        if (parentDir == null || !parentDir.exists()) {
            throw new IOException("Parent Directory is null or does not exist!");
        }
        if (imageFile.exists()) {
            throw new IOException("Image file already exists!");
        }
        List<String> allowedFormats = List.of("png", "jpg", "jpeg", "bmp");
        String fileName = imageFile.getName();
        String[] fileSeparator = fileName.split("\\.");
        String fileFormat = fileSeparator[fileSeparator.length - 1];
        if (!allowedFormats.contains(fileFormat)) {
            throw new IOException("Unsupported image format!");
        }
        try {
            ImageIO.write(image, fileFormat, imageFile);
        } catch (IOException e) {
            throw new IOException("A problem occurred while trying to save the image!", e);
        }
    }
}
