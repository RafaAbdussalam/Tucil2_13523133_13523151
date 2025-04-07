import java.awt.image.BufferedImage;

public class QuadtreeNode {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage imageBlock;
    private QuadtreeNode[] children;
    private boolean isLeaf;

    public QuadtreeNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imageBlock = null;
        this.children = null;
        this.isLeaf = true; 
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }
    
}
