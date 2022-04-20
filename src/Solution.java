import java.io.*;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws IOException {
        System.out.println("Введите адрес файла");
        Scanner s = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader(s.nextLine()));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        s.close();
    }
}