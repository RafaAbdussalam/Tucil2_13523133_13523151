import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcess {
    private final BufferedImage image;
    private int width;
    private int height;

    public ImageProcess(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        this.image = ImageIO.read(imageFile);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getPixel(int x, int y) {
        return image.getRGB(x, y);
    }

    public int getWidth (){
        return width;
    }

    public int getHeight (){
        return height;
    }

    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }
    
    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    
    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }
    
    public static int createRGB(int r, int g, int b) {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static long getImageSize(String imagePath) {
        File file = new File(imagePath);
        return file.length();
    }

    //debug
    // public void showImageInfo() {
    //     System.out.println("Lebar: " + width + " piksel");
    //     System.out.println("Tinggi: " + height + " piksel");
    // }

    public static void saveImage(String outputPath, BufferedImage outputImage) throws IOException {
        File outputFile = new File(outputPath);
        String extension = outputPath.substring(outputPath.lastIndexOf('.') + 1);
        ImageIO.write(outputImage, extension, outputFile);
    }
}