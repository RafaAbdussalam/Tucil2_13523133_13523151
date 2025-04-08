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

        int[] avgColor = ErrorCalculator.AverageColor(imageProcessor, x, y, width, height);
        
        double error = ErrorCalculator.calculateError(errorMethod, imageProcessor, x, y, width, height);
        
        if (error <= threshold || width <= minBlockSize || height <= minBlockSize || 
            width / 2 < minBlockSize || height / 2 < minBlockSize) {
            return new QuadTreeNode(x, y, width, height, avgColor[0], avgColor[1], avgColor[2]);
        }
        
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        
        QuadTreeNode topLeft = buildQuadTree(x, y, halfWidth, halfHeight);
        QuadTreeNode topRight = buildQuadTree(x + halfWidth, y, width - halfWidth, halfHeight);
        QuadTreeNode bottomLeft = buildQuadTree(x, y + halfHeight, halfWidth, height - halfHeight);
        QuadTreeNode bottomRight = buildQuadTree(x + halfWidth, y + halfHeight, width - halfWidth, height - halfHeight);
        
        return new QuadTreeNode(x, y, width, height, topLeft, topRight, bottomLeft, bottomRight);
    }
    
}
