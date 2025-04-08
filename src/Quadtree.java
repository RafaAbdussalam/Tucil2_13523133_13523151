
import java.awt.image.BufferedImage;
import java.io.File;

public class Quadtree {
    private final ImageProcess imageProcess;
    private final int errorMethod;
    private double threshold;
    private final int minBlockSize;
    private final QuadtreeNode root;
    private long compressedSize;
    
    public Quadtree(String imagePath, int errorMethod, double threshold, int minBlockSize) throws Exception {
        this.imageProcess = new ImageProcess(imagePath);
        this.errorMethod = errorMethod;
        this.threshold = threshold;
        this.minBlockSize = minBlockSize;
        this.root = buildQuadTree(0, 0, imageProcess.getWidth(), imageProcess.getHeight());
    }
    
    private QuadtreeNode buildQuadTree(int x, int y, int width, int height){
        int[] avgColor = ErrorMeasurements.AverageColor(imageProcess, x, y, width, height);
        double error = ErrorMeasurements.calculateError(errorMethod, imageProcess, x, y, width, height);
        
        if (error <= threshold || width <= minBlockSize || height <= minBlockSize || width / 2 < minBlockSize || height / 2 < minBlockSize){
            return new QuadtreeNode(x, y, width, height, avgColor[0], avgColor[1], avgColor[2]);
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        QuadtreeNode topLeft = buildQuadTree(x, y, halfWidth, halfHeight);
        QuadtreeNode topRight = buildQuadTree(x + halfWidth, y, width - halfWidth, halfHeight);
        QuadtreeNode bottomLeft = buildQuadTree(x, y + halfHeight, halfWidth, height - halfHeight);
        QuadtreeNode bottomRight = buildQuadTree(x + halfWidth, y + halfHeight, width - halfWidth, height - halfHeight);
        return new QuadtreeNode(x, y, width, height, topLeft, topRight, bottomLeft, bottomRight);
    }
    
    public BufferedImage generateCompressedImage(){
        BufferedImage outputImage = new BufferedImage(imageProcess.getWidth(), imageProcess.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        if (root != null){
            root.drawToImage(outputImage);
        }
        return outputImage;
    }

    public void saveCompressedImage(String outputPath) throws Exception{
        BufferedImage compressedImage = generateCompressedImage();
        ImageProcess.saveImage(outputPath, compressedImage);
        File outputFile = new File(outputPath);
        if (outputFile.exists()){
            this.compressedSize = outputFile.length();
        } else{
            this.compressedSize = estimateCompressedSize();
        }
    }
    
    private long estimateCompressedSize(){
        int leafNodes = countLeafNodes();
        int internalNodes = getTotalNodes() - leafNodes;
        long leafNodesSize = leafNodes * (16 + 3);
        long internalNodesSize = internalNodes * (16 + 16);
        long overhead = 1024;  
        return leafNodesSize + internalNodesSize + overhead;
    }
    
    public int countLeafNodes(){
        if (root == null){
            return 0;
        }
        return countLeafNodesRecursive(root);
    }
    
    private int countLeafNodesRecursive(QuadtreeNode node){
        if (node.isLeaf()){
            return 1;
        }
        return countLeafNodesRecursive(node.getTopLeft()) + countLeafNodesRecursive(node.getTopRight()) + countLeafNodesRecursive(node.getBottomLeft()) + countLeafNodesRecursive(node.getBottomRight());
    }
    
    public int getTotalNodes(){
        if (root == null){
            return 0;
        }
        return root.countNodes();
    }
    
    public int getMaxDepth(){
        if (root == null){
            return 0;
        }
        return root.getMaxDepth();
    }
    
    public long getCompressedSize(){
        return compressedSize;
    }

    public double getThreshold(){
        return threshold;
    }
    
    public void setThreshold(double threshold){
        this.threshold = threshold;
    }
}
