package GUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//XOR Cipher Encryption and Decryption Algorithm
//Source: https://www.geeksforgeeks.org/encrypt-and-decrypt-image-using-java/
public class Encrypt_Decrypt {
    public void main(String filePath) throws IOException {
        int key = 1234;
        FileInputStream inputStream = new FileInputStream(filePath);
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        int count = 0;
        for (byte b : data) {
            data[count] = (byte)(b ^ key);
            count++;
        }
        FileOutputStream outputStream = new FileOutputStream(filePath);
        outputStream.write(data);
        outputStream.close();
        inputStream.close();
    }
}
