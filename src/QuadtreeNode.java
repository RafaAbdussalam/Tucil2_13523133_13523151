import java.awt.image.BufferedImage;

public class QuadtreeNode {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isLeaf;
    private int redAverage;
    private int greenAverage;
    private int blueAverage;

    private final QuadTreeNode topLeft;
    private final QuadTreeNode topRight;
    private final QuadTreeNode bottomLeft;
    private final QuadTreeNode bottomRight;

    public QuadtreeNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;


        this.imageBlock = null;
        this.children = null;
        this.isLeaf = true; 
    }

    public QuadtreeNode(int x, int y, int width, int height, int redAverage, int greenAverage, int blueAverage) {
        this.x =x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isLeaf = true;
        this.redAverage = redAverage;
        this.greenAverage = greenAverage;
        this.blueAverage = blueAverage;

        this.topLeft = null;
        this.topRight = null;
        this.bottomLeft = null;
        this.bottomRight = null;
    }

    public QuadtreeNode(int x, int y, int width, int height, QuadTreeNode topLeft, QuadTreeNode topRight, QuadTreeNode bottomLeft, QuadTreeNode bottomRight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isLeaf = false;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
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
