import java.awt.image.BufferedImage;
import java.io.File;

public class Quadtree {
    private ImageProcess ImageProcess;
    private int minBlockSize;
    private double threshold;
    private int errorMethod;
    private QuadTreeNode root;
    private long imageCompressSize;

    public Quadtree(String image, int errorMethod, double threshold, int minBlockSize) throws Exception {
        this.ImageProcess = new ImageProcess(image);
        this.minBlockSize = minBlockSize;
        this.threshold =   threshold;
        this.errorMethod = errorMethod;
        this.root = buildQuadtree(0, 0, ImageProcess.getWidth(), ImageProcess.getHeight());
    }

    public buildQuadtree(int X, int Y, int width, int height) {

        if (width <= minBlockSize || height <= minBlockSize) {
            return new QuadTreeNode(X, Y, width, height);
        }

    }
    
}
