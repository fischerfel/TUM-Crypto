import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class FileTransferClient { 

    public static void main(String[] args) throws Exception{

        //Initialize socket
        Socket socket = new Socket(InetAddress.getByName("localhost"), 5000);
        byte[] contents = new byte[100000000];

        //Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream("/home/shanmukhh/Desktop/op.mp4");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();
        System.out.println("is: "+is);
        //No of bytes read in one read() call
        int bytesRead = 0; 
        String key ="1234567812345678";
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] outputBytes =null;
        while((bytesRead=is.read(is.toString().getBytes()))!=-1){
            outputBytes = cipher.doFinal(is.toString().getBytes());
            bos.write(outputBytes, 0, bytesRead); 
        }

        bos.flush(); 
        socket.close(); 



        System.out.println("File saved successfully!");
    }
    }
