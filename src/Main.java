import java.io.File;
import java.util.Scanner;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String BOLD = "\u001B[1m";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueProgram = true;
        while (continueProgram) {
            try {
                System.out.println(BLUE + BOLD + "\n======================================================");
                System.out.println("=                                                    =");
                System.out.println("=       PROGRAM KOMPRESI GAMBAR DENGAN QUADTREE      =");
                System.out.println("=                                                    =");
                System.out.println("=            IF2211 Strategi Algoritma               =");
                System.out.println("=                                                    =");
                System.out.println("======================================================\n" + RESET);
                String inputImagePath = getValidImagePath(scanner);
                if (inputImagePath == null) {
                    continueProgram = askToContinue(scanner);
                    continue;
                }

                System.out.println(BLUE + BOLD + "======================================================");
                System.out.println("=          METODE PERHITUNGAN ERROR                  =");
                System.out.println("======================================================");
                System.out.println("| 1. Metode Variance                                 |");
                System.out.println("| 2. Metode Mean Absolute Deviation (MAD)            |");
                System.out.println("| 3. Metode Max Pixel Difference                     |");
                System.out.println("| 4. Metode Entropy                                  |");
                System.out.println("| 5. Keluar                                          |");
                System.out.println("======================================================" + RESET);
                int errorMethod = getValidIntInput(scanner, "Pilih metode (1-5): ", 1, 5);
                if (errorMethod == -1) {
                    continueProgram = askToContinue(scanner);
                    continue;
                } 
                if (errorMethod == 5) {
                    System.out.println("Terima kasih telah menggunakan program ini!");
                    break;
                }
                String thresholdNote = "";  
                switch (errorMethod) {
                    case 1: // Variance
                        thresholdNote = " (rentang yang disarankan: 10-100)";
                        break;
                    case 2: // MAD
                        thresholdNote = " (rentang yang disarankan: 5-20)";
                        break;
                    case 3: // Max Pixel Difference
                        thresholdNote = " (rentang yang disarankan: 10-100)";
                        break;
                    case 4: // Entropy
                        thresholdNote = " (rentang yang disarankan: 0.1-1.0)";
                        break;
                }
                System.out.println("Ambang batas (threshold)" + thresholdNote);
                double threshold = getValidDoubleInput(scanner, "Masukkan nilai threshold (nilai positif): ", 0, Double.MAX_VALUE);
                if (threshold == -1) {
                    continueProgram = askToContinue(scanner);
                    continue;
                }
                int minBlockSize = getValidIntInput(scanner, "Masukkan ukuran blok minimum (nilai positif): ", 1, Integer.MAX_VALUE);
                if (minBlockSize == -1) {
                    continueProgram = askToContinue(scanner);
                    continue;
                }
                String outputImagePath = getValidOutputPath(scanner);
                if (outputImagePath == null) {
                    continueProgram = askToContinue(scanner);
                    continue;
                }
                System.out.println("\nMemulai proses kompresi...");
                QuadTree quadTree;
                double startTime = System.currentTimeMillis();
                final boolean[] processingComplete = {false};
                Thread loadingThread = new Thread(() -> {
                    String[] spinner = {"|", "/", "-", "\\"};
                    int i = 0;
                    try {
                        while (!Thread.currentThread().isInterrupted() && !processingComplete[0]) {
                            System.out.print("\rMemproses... " + spinner[i]);
                            i = (i + 1) % spinner.length;
                            Thread.sleep(150);
                        }
                    } catch (InterruptedException e) {
                    } finally {
                        System.out.println();
                    }
                });
                
                loadingThread.start();
                try {
                    quadTree = new QuadTree(inputImagePath, errorMethod, threshold, minBlockSize);
                    processingComplete[0] = true;
                    loadingThread.join();
                    System.out.println("Menyimpan gambar hasil kompresi...");
                    processingComplete[0] = false;
                    Thread savingThread = new Thread(() -> {
                        String[] spinner = {"|", "/", "-", "\\"};
                        int i = 0;
                        try {
                            while (!Thread.currentThread().isInterrupted() && !processingComplete[0]) {
                                System.out.print("\rMenyimpan... " + spinner[i]);
                                i = (i + 1) % spinner.length;
                                Thread.sleep(150);
                            }
                        } catch (InterruptedException e) {} 
                        finally {
                            System.out.println();
                        }
                    });
                    savingThread.start();
                    quadTree.saveCompressedImage(outputImagePath);
                    processingComplete[0] = true;
                    savingThread.join();
                    double endTime = System.currentTimeMillis();
                    double executionTime = endTime - startTime;
                    System.out.println(BLUE + BOLD + "\n======================================================" + RESET);
                    System.out.println("=                 HASIL KOMPRESI                     =");
                    System.out.println(BLUE + BOLD + "======================================================" + RESET);
                    double compressionPercentage = (1 - (double) quadTree.getCompressedSize()/(double) ImageProcessor.getFileSize(inputImagePath)) * 100;
                    System.out.printf("| %-25s : %-22s |\n", "Waktu eksekusi", executionTime + " ms");
                    System.out.printf("| %-25s : %-22s |\n", "Ukuran gambar sebelum", ImageProcessor.getFileSize(inputImagePath) + " bytes");
                    System.out.printf("| %-25s : %-22s |\n", "Ukuran gambar setelah", quadTree.getCompressedSize() + " bytes");
                    System.out.printf("| %-25s : %-22s |\n", "Persentase kompresi ", String.format("%.2f%%", compressionPercentage));
                    System.out.printf("| %-25s : %-22s |\n", "Kedalaman pohon", quadTree.getMaxDepth());
                    System.out.printf("| %-25s : %-22s |\n", "Banyak simpul pada pohon", quadTree.getTotalNodes());
                    System.out.printf("| %-25s : %-22s |\n", "Threshold digunakan", String.format("%.2f", quadTree.getThreshold()));
                    System.out.println("======================================================");
                    System.out.println("\nProses kompresi selesai! Gambar hasil kompresi disimpan di: " + YELLOW + outputImagePath + RESET);
                    continueProgram = askToContinue(scanner); 
                } catch (Exception e) {
                    processingComplete[0] = true;
                    try {
                        loadingThread.join(500);
                    } catch (InterruptedException ie) {}
                    System.err.println("Terjadi kesalahan: " + e.getMessage());
                    e.printStackTrace();
                    continueProgram = askToContinue(scanner);
                }
            } catch (Exception e) {
                System.err.println("Terjadi kesalahan: " + e.getMessage());
                e.printStackTrace();
                continueProgram = askToContinue(scanner);
            }
        }
        System.out.println(BLUE + BOLD + "\n======================================================");
        System.out.println("=          PROGRAM BERAKHIR. TERIMA KASIH!             =");
        System.out.println("======================================================" + RESET);
        scanner.close();
    }
    
    private static boolean askToContinue(Scanner scanner) {
        while (true) {
            System.out.print("\nApakah Anda ingin melanjutkan program? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            } else {
                System.out.println("Input tidak valid. Masukkan 'y' untuk ya atau 'n' untuk tidak.");
            }
        }
    }
    
    private static String getValidImagePath(Scanner scanner) {
        while (true) {
            System.out.println("Masukkan nama file gambar atau path absolut (ketik 'cancel' untuk batal):");
            String path = scanner.nextLine().trim();
            if (path.equalsIgnoreCase("cancel")) {
                return null;
            }
            File file = new File(path);
            if (!file.exists()) {
                File testImageFile = new File("../test/" + path);
                if (testImageFile.exists()) {
                    file = testImageFile;
                    path = testImageFile.getAbsolutePath();
                    System.out.println("Menggunakan file dari folder test: " + YELLOW + path + RESET);
                } else {
                    System.out.println("Error: File tidak ditemukan. Pastikan file ada di folder test atau masukkan path absolut.");
                    continue;
                }
            }
            
            if (!file.isFile()) {
                System.out.println("Error: Path bukan file. Coba lagi.");
                continue;
            }
            String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
            if (!extension.matches("jpg|jpeg|png|bmp")) {
                System.out.println("Error: File bukan gambar yang didukung. Format yang didukung: jpg, jpeg, png, bmp.");
                continue;
            } return path;
        }
    }
    
    private static String getValidOutputPath(Scanner scanner) {
        while (true) {
            System.out.println("Masukkan nama file output atau path absolut (ketik 'cancel' untuk batal):");
            String path = scanner.nextLine().trim();
            if (path.equalsIgnoreCase("cancel")) {
                return null;
            }
            String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
            if (!extension.matches("jpg|jpeg|png|bmp")) {
                System.out.println("Error: Format file output tidak didukung. Format yang didukung: jpg, jpeg, png, bmp.");
                continue;
            }
            if (!path.contains("/") && !path.contains("\\")) {
                File outputDir = new File("../test");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }
                path = "../test/" + path;
                System.out.println("File akan disimpan di: " + YELLOW + new File(path).getAbsolutePath() + RESET);
            }
            File outputFile = new File(path);
            File directory = outputFile.getParentFile();
            if (directory != null && !directory.exists()) {
                System.out.println("Direktori tujuan tidak ada. Membuat direktori...");
                boolean created = directory.mkdirs();
                if (!created) {
                    System.out.println("Error: Gagal membuat direktori. Coba lagi dengan path lain.");
                    continue;
                }
            } return path;
        }
    }
    
    private static int getValidIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) {
                return -1;
            } try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Error: Nilai harus antara " + min + " dan " + max + ". Coba lagi atau ketik 'cancel' untuk batal.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Input bukan angka integer yang valid. Coba lagi atau ketik 'cancel' untuk batal.");
            }
        }
    }
    
    private static double getValidDoubleInput(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();   
            if (input.equalsIgnoreCase("cancel")) {
                return -1;
            } try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("Error: Nilai harus antara " + min + " dan " + max + ". Coba lagi atau ketik 'cancel' untuk batal.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Input bukan angka yang valid. Coba lagi atau ketik 'cancel' untuk batal.");
            }
        }
    }
}
