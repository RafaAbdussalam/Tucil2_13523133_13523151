import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMeasurements {
    
    public static double calculateError(int method, ImageProcess image, int x, int y, int width, int height) {
        switch (method) {
            case 1:
                return Variance(image, x, y, width, height);
            case 2:
                return MeanAbsoluteDeviation(image, x, y, width, height);
            case 3:
                return MaxPixelDifference(image, x, y, width, height);
            case 4:
                return Entropy(image, x, y, width, height);
            default:
                throw new IllegalArgumentException("Invalid error calculation method");
        }
    }

    public static int[] AverageColor(ImageProcess image, int x, int y, int width, int height) {
        long sumRed = 0; long sumGreen = 0; long sumBlue = 0;
        int pixels = 0;
        
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getPixel(i, j);
                sumRed += ImageProcess.getRed(rgb);
                sumGreen += ImageProcess.getGreen(rgb);
                sumBlue += ImageProcess.getBlue(rgb);
                pixels++;
            }
        }
        
        return new int[] {
            (int) (sumRed / pixels), (int) (sumGreen / pixels), (int) (sumBlue / pixels)
        };
    }

    private static double Variance(ImageProcess image, int x, int y, int width, int height){
        int[] avgColor = AverageColor(image, x, y, width, height);
        int avgRed = avgColor[0];
        int avgGreen = avgColor[1];
        int avgBlue = avgColor[2];
        
        double sumSqDiffRed = 0;
        double sumSqDiffGreen = 0;
        double sumSqDiffBlue = 0;
        int pixels = 0;
        
        for (int i = x; i < x + width && i < image.getWidth(); i++) {
            for (int j = y; j < y + height && j < image.getHeight(); j++) {
                int rgb = image.getPixel(i, j);
                int red = ImageProcess.getRed(rgb);
                int green = ImageProcess.getGreen(rgb);
                int blue = ImageProcess.getBlue(rgb);
                
                sumSqDiffRed += Math.pow(red - avgRed, 2);
                sumSqDiffGreen += Math.pow(green - avgGreen, 2);
                sumSqDiffBlue += Math.pow(blue - avgBlue, 2);
                pixels++;
            }
        }

        double redVariance = sumSqDiffRed / pixels;
        double greenVariance = sumSqDiffGreen / pixels;
        double blueVariance = sumSqDiffBlue / pixels;

        return (redVariance + greenVariance + blueVariance) / 3;
    }

    private static double MeanAbsoluteDeviation(ImageProcess image, int x, int y, int width, int height){
        int[] avgColor = AverageColor(image, x, y, width, height);
        int avgRed = avgColor[0];
        int avgGreen = avgColor[1];
        int avgBlue = avgColor[2];
        
        double sumAbsDiffRed = 0;
        double sumAbsDiffGreen = 0;
        double sumAbsDiffBlue = 0;
        int pixels = 0;
        
        for (int i = x; i < x + width && i < image.getWidth(); i++) {
            for (int j = y; j < y + height && j < image.getHeight(); j++) {
                int rgb = image.getPixel(i, j);
                int red = ImageProcess.getRed(rgb);
                int green = ImageProcess.getGreen(rgb);
                int blue = ImageProcess.getBlue(rgb);
                
                sumAbsDiffRed += Math.abs(red - avgRed);
                sumAbsDiffGreen += Math.abs(green - avgGreen);
                sumAbsDiffBlue += Math.abs(blue - avgBlue);
                pixels++;
            }
        }
        
        double madRed = sumAbsDiffRed / pixels;
        double madGreen = sumAbsDiffGreen / pixels;
        double madBlue = sumAbsDiffBlue / pixels;
        
        return (madRed + madGreen + madBlue) / 3;
    }
    
    private static double MaxPixelDifference(ImageProcess image, int x, int y, int width, int height){
        int minRed = 255, maxRed = 0;
        int minGreen = 255, maxGreen = 0;
        int minBlue = 255, maxBlue = 0;
        
        for (int i = x; i < x + width && i < image.getWidth(); i++) {
            for (int j = y; j < y + height && j < image.getHeight(); j++) {
                int rgb = image.getPixel(i, j);
                int red = ImageProcess.getRed(rgb);
                int green = ImageProcess.getGreen(rgb);
                int blue = ImageProcess.getBlue(rgb);
                
                minRed = Math.min(minRed, red);
                maxRed = Math.max(maxRed, red);
                minGreen = Math.min(minGreen, green);
                maxGreen = Math.max(maxGreen, green);
                minBlue = Math.min(minBlue, blue);
                maxBlue = Math.max(maxBlue, blue);
            }
        }
        
        double difRed = maxRed - minRed;
        double difGreen = maxGreen - minGreen;
        double difBlue = maxBlue - minBlue;
        
        return (difRed + difGreen + difBlue) / 3;
    }

    private static double Entropy(ImageProcess image, int x, int y, int width, int height){
        Map<Integer, Integer> redFreq = new HashMap<>();
        Map<Integer, Integer> greenFreq = new HashMap<>();
        Map<Integer, Integer> blueFreq = new HashMap<>();
        int pixels = 0;

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getPixel(i, j);
                int red = ImageProcess.getRed(rgb);
                int green = ImageProcess.getGreen(rgb);
                int blue = ImageProcess.getBlue(rgb);
                
                redFreq.put(red, redFreq.getOrDefault(red, 0) + 1);
                greenFreq.put(green, greenFreq.getOrDefault(green, 0) + 1);
                blueFreq.put(blue, blueFreq.getOrDefault(blue, 0) + 1);
                pixels++;
            }
        }
        
        double entropy = 0;
        
        List<Map<Integer, Integer>> freqs = Arrays.asList(redFreq, greenFreq, blueFreq);
        for (Map<Integer, Integer> freq : freqs) {
            for (int count : freq.values()) {
                double probability = (double) count / pixels;
                entropy -= (probability * (Math.log(probability) / Math.log(2))) / 3;
            }
        }

        return entropy;
    }
}