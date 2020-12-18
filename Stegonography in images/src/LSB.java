package lsb;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

public class LSB {

    private static int HEADER_SIZE = 54;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        
        if(args.length == 3){
            Encode(args[1], args[2]);
        } else {
            Decode(args[1]);
        }
        
         
    }

    public static String messtobin(String mess) {
        String result = "";
        for (char c : mess.toCharArray()) {
            String bin = Integer.toBinaryString(c);
            while (bin.length() < 8) { 
                bin = "0" + bin;
            }
            result += bin;
        }
        return result;
    }

    public static String bintomess(String bin) {
        int charCode = Integer.parseInt(bin, 2);
        String result = new Character((char) charCode).toString();
        return result;
    }

    public static void Encode(String mess, String srcname) throws IOException {
        String bin = messtobin(mess) + "00000000";
        File f = new File(srcname + ".png");
        BufferedImage bufferedImage = ImageIO.read(f);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        byte[] arr = bos.toByteArray();

        int data = HEADER_SIZE;
        char temp;
        int x = 0;
        if (bin.length() > (arr.length - HEADER_SIZE)) {
            System.out.println("message too large for image to hide!");
        } else {
            for (int i = 0; i < bin.length(); i++) {
                arr[data] >>= 1;
                arr[data] <<= 1;
                arr[data] += (bin.charAt(i) - 48);   //
                data++;
            }
        }


        File output = new File(srcname + ".png");
        bufferedImage = ImageIO.read(new ByteArrayInputStream(arr));
        if (ImageIO.write(bufferedImage, "png", output)) {
            System.out.println("Information encoded in" + srcname);
        }
    }

    public static void Decode() throws IOException {
        try {
            sc.nextLine();
            String destname = sc.nextLine();

            File f = new File(destname + ".png");
            BufferedImage bufferedImage = ImageIO.read(f);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", bos);
            byte[] arr = bos.toByteArray();

            int data = HEADER_SIZE;
            String databin = "";
            String mess = "";
            int ktra8bit0 = 0; 
            int count = 0; 
            while (true) {
                if (arr[data] % 2 == 0) {
                    databin += "0";
                    ktra8bit0 += 1;
                    count++;
                } else {
                    databin += "1";
                    ktra8bit0 = 0;
                    count++;
                }
                data++;
                if (count == 8) {
                    if (ktra8bit0 == 8) {
                        break;
                    }
                    ktra8bit0 = 0;
                    count = 0;
                }
            }
            databin = databin.substring(0, databin.length() - 8); 
            for (int i = 0; i < databin.length(); i = i + 8) {
                mess += bintomess(databin.substring(i, i + 8));
            }
            System.out.println(mess);
        } catch (IIOException ex) {
            System.out.println("can't read file");
        }
    }
}