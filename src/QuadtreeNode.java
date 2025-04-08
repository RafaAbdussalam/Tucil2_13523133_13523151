import java.awt.image.BufferedImage;

public class QuadtreeNode {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int avgRed;
    private final int avgGreen;
    private final int avgBlue;
    private final boolean isLeaf;
    private final QuadtreeNode topLeft;
    private final QuadtreeNode topRight;
    private final QuadtreeNode bottomLeft;
    private final QuadtreeNode bottomRight;
    

    public QuadtreeNode(int x, int y, int width, int height, int avgRed, int avgGreen, int avgBlue){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.avgRed = avgRed;
        this.avgGreen = avgGreen;
        this.avgBlue = avgBlue;
        this.isLeaf = true;
        this.topLeft = null;
        this.topRight = null;
        this.bottomLeft = null;
        this.bottomRight = null;
    }
    
    public QuadtreeNode(int x, int y, int width, int height, QuadtreeNode topLeft, QuadtreeNode topRight, QuadtreeNode bottomLeft, QuadtreeNode bottomRight){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isLeaf = false;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight; 
        long totalPixels = 0;
        long weightedSumRed = 0;
        long weightedSumGreen = 0;
        long weightedSumBlue = 0;
        QuadtreeNode[] children ={topLeft, topRight, bottomLeft, bottomRight};
        for (QuadtreeNode child : children){
            long pixels = child.getWidth() * child.getHeight();
            totalPixels += pixels;
            weightedSumRed += child.getAvgRed() * pixels;
            weightedSumGreen += child.getAvgGreen() * pixels;
            weightedSumBlue += child.getAvgBlue() * pixels;
        } if (totalPixels > 0){
            this.avgRed = (int) (weightedSumRed / totalPixels);
            this.avgGreen = (int) (weightedSumGreen / totalPixels);
            this.avgBlue = (int) (weightedSumBlue / totalPixels);
        } else{
            this.avgRed = 0;
            this.avgGreen = 0;
            this.avgBlue = 0;
        }
    }
    
    public void drawToImage(BufferedImage outputImage){
        if (isLeaf){
            int rgb = ImageProcess.createRGB(avgRed, avgGreen, avgBlue);
            for (int i = x; i < x + width; i++){
                for (int j = y; j < y + height; j++){
                    if (i < outputImage.getWidth() && j < outputImage.getHeight()){
                        outputImage.setRGB(i, j, rgb);
                    }
                }
            }
        } else{
            if (topLeft != null) topLeft.drawToImage(outputImage);
            if (topRight != null) topRight.drawToImage(outputImage);
            if (bottomLeft != null) bottomLeft.drawToImage(outputImage);
            if (bottomRight != null) bottomRight.drawToImage(outputImage);
        }
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getAvgRed(){
        return avgRed;
    }
    public int getAvgGreen(){
        return avgGreen;
    }
    public int getAvgBlue(){
        return avgBlue;
    }
    public boolean isLeaf(){
        return isLeaf;
    }
    public QuadtreeNode getTopLeft(){
        return topLeft;
    }
    public QuadtreeNode getTopRight(){
        return topRight;
    }
    public QuadtreeNode getBottomLeft(){
        return bottomLeft;
    }
    public QuadtreeNode getBottomRight(){
        return bottomRight;
    }
    

    public int countNodes(){
        if (isLeaf){
            return 1;
        } else{
            return 1 + topLeft.countNodes() + topRight.countNodes() + bottomLeft.countNodes() + bottomRight.countNodes();
        }
    }
    
    public int getMaxDepth(){
        if (isLeaf){
            return 0;
        } else{
            return 1 + Math.max(Math.max(topLeft.getMaxDepth(), topRight.getMaxDepth()), Math.max(bottomLeft.getMaxDepth(), bottomRight.getMaxDepth()));
        }
    }
}