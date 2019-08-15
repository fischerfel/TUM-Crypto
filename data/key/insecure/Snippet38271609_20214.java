import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class FileTransferServer { 

    public static void main(String[] args) throws Exception {
        //Initialize Sockets
        ServerSocket ssock = new ServerSocket(5000);
        Socket socket = ssock.accept();

        //The InetAddress specification
        InetAddress IA = InetAddress.getByName("localhost"); 

        //Specify the file
        File file = new File("/home/shanmukhh/Downloads/ApacheSpark.mp4");
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis); 

        //Get socket's output stream
        OutputStream os = socket.getOutputStream();

        //Read File Contents into contents array 
        byte[] contents;
        long fileLength = file.length(); 
        long current = 0;

        long start = System.nanoTime();
        while(current!=fileLength){ 
            int size = 100000000;
            if(fileLength - current >= size)
                current += size;    
            else{ 
                size = (int)(fileLength - current); 
                current = fileLength;
            } 
            contents = new byte[size]; 
            bis.read(contents, 0, size); 
            String key = "1234567812345678";
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] outputBytes = cipher.doFinal(bis.toString().getBytes("UTF-8"));

            os.write(outputBytes);
            System.out.println(outputBytes.toString().getBytes("UTF-8"));
            System.out.print("Sending file ... "+(current*100)/fileLength+"% complete!");
            System.out.println(outputBytes.toString().getBytes("UTF-8"));
        }   

        //os.flush(); 
        //File transfer done. Close the socket connection!
        socket.close();
        ssock.close();
        System.out.println("File sent succesfully!");
    }
}
