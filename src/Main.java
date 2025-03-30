import java.util.Scanner;


public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selamat datang di program kompresi gambar!");

        System.out.println("Masukkan Alamat Abosult Gambar yang ingin dikompresi:");
        String inputImagePath = scanner.nextLine();
        System.out.print("===================================\n");
        System.out.println("Masukkan Metode perhitungan Error:");
        System.out.println("1. Metode Variance");
        System.out.println("2. Metode Mean Absolute Deviation (MAD)");
        System.out.println("3. Metode Max Pixel Difference");
        System.out.println("4. Metode Entropy");
        System.out.println("5. Keluar");
        System.out.print("===================================\n");
        System.out.print("Pilih metode (1-5): ");

        int choice = scanner.nextInt();

        System.out.print("Masukkan ambang batas (threshold): ");
        double threshold = scanner.nextDouble();
        
        System.out.print("Masukkan ukuran blok minimum: ");
        int minBlockSize = scanner.nextInt();
        
        System.out.print("Masukkan alamat absolut untuk menyimpan gambar hasil kompresi: ");
        String outputImagePath = scanner.nextLine();
    }
}



