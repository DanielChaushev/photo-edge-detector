# photo-edge-detector
Java image processing library implementing grayscale conversion and Sobel edge detection with file system management.

A Java image processing library built as a university assignment.
Implements grayscale conversion and edge detection algorithms alongside a file system manager for loading and saving images.

## Structure

### Algorithms
- `ImageAlgorithm` — interface for image processing algorithms
- `GrayscaleAlgorithm` — marker interface for grayscale algorithms
- `EdgeDetectionAlgorithm` — marker interface for edge detection algorithms
- `LuminosityGrayscale` — converts images to grayscale using the Luminosity method (0.21R + 0.72G + 0.07B)
- `SobelEdgeDetection` — detects edges using the Sobel operator with zero padding for border pixels

### File System
- `FileSystemImageManager` — interface for loading and saving images
- `LocalFileSystemImageManager` — loads and saves JPEG, PNG, and BMP images using `ImageIO`

## How it works

**Grayscale conversion** applies the Luminosity method to each pixel, weighting the RGB channels
to produce a perceptually accurate black-and-white image.

**Sobel edge detection** first converts the image to grayscale, then applies horizontal and vertical
convolution kernels to compute the gradient magnitude at each pixel. High-gradient areas appear
white (edges), low-gradient areas appear black (uniform regions).

## Notes

- All algorithms return a new `BufferedImage` of type `TYPE_INT_RGB`
- Border pixels in Sobel are handled with zero padding
- Tests written with JUnit 5 and `@TempDir` for file system testing
