import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean continueProgram = true;
        System.out.println("\n======================================================");
        System.out.println("=                  Selamat Datang                    =");
        System.out.println("=       PROGRAM KOMPRESI GAMBAR DENGAN QUADTREE      =");
        System.out.println("=                                                    =");
        System.out.println("=            IF2211 Strategi Algoritma               =");
        System.out.println("=                                                    =");
        System.out.println("======================================================\n");

        String inputImagePath = ValidImagePath(scanner);

        System.out.print("===================================\n");
        System.out.println("Masukkan Metode perhitungan Error:");
        System.out.println("1. Metode Variance");
        System.out.println("2. Metode Mean Absolute Deviation (MAD)");
        System.out.println("3. Metode Max Pixel Difference");
        System.out.println("4. Metode Entropy");
        System.out.println("5. Keluar");
        System.out.print("===================================\n");
        System.out.print("Pilih metode (1-5): ");

        int errorMethod = scanner.nextInt();
        scanner.nextLine();

        double threshold = getValidDoubleInput(scanner, "Masukkan ambang batas (threshold) (nilai positif) :", 0, Double.MAX_VALUE);
        
        int minBlockSize = getValidIntInput(scanner, "Masukkan ukuran blok minimum (nilai positif): ", 1, Integer.MAX_VALUE);
        
        System.out.print("Masukkan alamat absolut untuk menyimpan gambar hasil kompresi: ");
        String outputImagePath = ValidOutputPath(scanner);

        System.out.println("\nMemulai proses kompresi...");
        long startTime = System.currentTimeMillis();

        Quadtree quadtree;

        try {
            quadtree = new Quadtree(inputImagePath, errorMethod, threshold, minBlockSize);
        } catch (Exception e) {
            System.out.println("[Error] Gagal memproses gambar: " + e.getMessage());
            return;
        }

        System.out.println("Proses kompresi selesai.");
        System.out.println("Menyimpan gambar hasil kompresi ke: " + outputImagePath);

        try {
            quadtree.saveCompressedImage(outputImagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("\n======================================================");
        System.out.println("=                 HASIL KOMPRESI                     =");
        System.out.println("======================================================");
        System.out.println("|[Output] Waktu eksekusi           :" + executionTime + " ms");
        System.out.println("|[Output] Ukuran gambar sebelum    :" + ImageProcess.getImageSize(inputImagePath) + " bytes");
        System.out.println("|[Output] Ukuran gambar setelah    :" + quadtree.getCompressedSize() +" bytes");
        System.out.println("|[Output] Persentase kompresi      :" + (1 - (double) quadtree.getCompressedSize()/(double) ImageProcess.getImageSize(inputImagePath)) * 100 + "%");
        System.out.println("|[Output] Kedalaman pohon          :" + quadtree.getMaxDepth() + " level");
        System.out.println("|[Output] Banyak simpul pada pohon :" + quadtree.getTotalNodes() + " simpul");
        System.out.println("======================================================");

        continueProgram = ContinueProgram(scanner);
        
    }

    private static String ValidImagePath(Scanner scanner) {
        while (true) {
            System.out.println("Masukkan nama file gambar atau path absolut yang ingin dikompresi (ketik 'cancel' untuk batal):");
            String path = scanner.nextLine().trim();
            
            if (path.equalsIgnoreCase("keluar")) {
                return null;
            }
            
            File file = new File(path);
            if (!file.exists()) {
                File testImageFile = new File("../test/" + path);
                if (testImageFile.exists()) {
                    file = testImageFile;
                    path = testImageFile.getAbsolutePath();
                    System.out.println("Menggunakan file dari folder test: " + path);
                } else {
                    System.out.println("[Error] File tidak ditemukan. Pastikan file ada di folder test atau masukkan path absolut.");
                    continue;
                }
            }
            
            if (!file.isFile()) {
                System.out.println("[Error] Path bukan file. Coba lagi.");
                continue;
            }
            
            String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
            if (!extension.matches("jpg|jpeg|png")) {
                System.out.println("[Error] Program hanya dapat menerima File dengan Format : jpg, jpeg, png.");
                continue;
            }
            
            return path;
        }
    }
    
    private static String ValidOutputPath(Scanner scanner) {
        while (true) {
            System.out.println("Masukkan nama file output atau path absolut (ketik 'cancel' untuk batal):");
            String path = scanner.nextLine().trim();
            
            if (path.equalsIgnoreCase("cancel")) {
                return null;
            }

            String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
            if (!extension.matches("jpg|jpeg|png")) {
                System.out.println("[Error] Format file output tidak didukung. Format yang didukung: jpg, jpeg, png.");
                continue;
            }

            if (!path.contains("/") && !path.contains("\\")) {
                File outputDir = new File("../test");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }
                path = "../test/" + path;
                System.out.println("File akan disimpan di: " + new File(path).getAbsolutePath());
            }

            File outputFile = new File(path);
            File directory = outputFile.getParentFile();
            if (directory != null && !directory.exists()) {
                System.out.println("Direktori tujuan tidak ada. Membuat direktori...");
                boolean created = directory.mkdirs();
                if (!created) {
                    System.out.println("[Error] Gagal membuat direktori. Coba lagi dengan path lain.");
                    continue;
                }
            }
            
            return path;
        }
    }
    
    private static int getValidIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("[Error] Nilai harus antara " + min + " dan " + max + ". Coba lagi atau ketik 'cancel' untuk batal.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[Error] Input bukan angka integer yang valid. Coba lagi atau ketik 'cancel' untuk batal.");
            }
        }
    }
    
    private static double getValidDoubleInput(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("[Error] Nilai harus antara " + min + " dan " + max + ". Coba lagi atau ketik 'cancel' untuk batal.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[Error] Input bukan angka yang valid. Coba lagi atau ketik 'cancel' untuk batal.");
            }
        }
    }

    private static boolean ContinueProgram(Scanner scanner) {
        while (true) {
            System.out.print("\nApakah Anda ingin melanjutkan program? (y/n): ");
            String input = scanner.nextLine();
            if (input.equals("y") || input.equals("yes") || input.equals("Y") || input.equals("Yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("N") || input.equals("No")) {
                return false;
            } else {
                System.out.println("Input tidak valid. Masukkan yang valid berupa 'y' untuk ya atau 'n' untuk tidak.");
            }
        }
    }
}