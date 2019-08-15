package test.cipher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CipherTest {


    public void runTest() throws Exception{
        KeyGenerator generator=KeyGenerator.getInstance("AES");
        generator.init(256);
        SecretKey key=generator.generateKey();

        byte[] bytes=new byte[16];
        (new Random()).nextBytes(bytes);
        IvParameterSpec spec=new IvParameterSpec(bytes);

        Server s=new Server(key,spec);
        Client c=new Client(key,spec);

        s.startListening();
        c.sendMessage("Hi there");
        c.sendMessage("this is a test");
        c.sendMessage("The rain in Spain stays mainly on the plains.");

        MessageDigest md=MessageDigest.getInstance("SHA-512");
        md.update("The rain in Spain stays mainly on the plains.  The rain in Spain stays mainly on the plains.  The rain in Spain stays mainly on the plains. The rain in Spain stays mainly on the plains. The rain in Spain stays mainly on the plains. ".getBytes());
        byte[] digest=md.digest();
        c.sendHash(digest);

        s.stopServer();
    }


    public static void main(String[] args) throws Exception{
        CipherTest t=new CipherTest();
        t.runTest();
    }

}

class Message implements Serializable{
    byte[] message; 
    public static Message createMessageFromHash(byte[] hash){
        StringBuilder sb=new StringBuilder();
        for (byte b:hash){
            sb.append(String.format("%02x", b));
        }
        return new Message(sb.toString().getBytes());
    }

    public Message(byte[] bytes) {
        message=bytes;
   }
}

class Server implements Runnable{
    Cipher cipher;
    ServerSocket server;
    boolean wasStopped=false;
    Thread serverThread;

    public Server(SecretKey key,IvParameterSpec spec) {
        try {
            cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,spec);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void startListening() throws IOException{
        server=new ServerSocket(9189);
        Thread t=new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        serverThread=Thread.currentThread();
        try {
            if (server!=null){
                while (!wasStopped){
                    Socket clientSocket=server.accept();                
                    ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
                    Message message=(Message)ois.readObject();
                    System.out.println( new String(cipher.doFinal(message.message)) );
                    ois.close();
                }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopServer(){
         serverThread.interrupt();
         wasStopped=true;
         try {
             serverThread.join(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         serverThread=null;
         server=null;
    }
}


class Client{
    Cipher cipher;

public Client(SecretKey key, IvParameterSpec spec){
        try {
            cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void sendHash(byte[] hash) throws Exception{
        Socket server=new Socket("localhost", 9189);
        Message message=Message.createMessageFromHash(hash);
        System.out.println(new String(message.message));
        byte[] ciphertext=cipher.doFinal(message.message);
        ObjectOutputStream oos=new ObjectOutputStream(server.getOutputStream());
        oos.writeObject(new Message(ciphertext));
        oos.close();
 }
    public void sendMessage(String message) throws Exception{
        Socket server=new Socket("localhost", 9189);
        byte[] ciphertext=cipher.doFinal(message.getBytes());
        ObjectOutputStream oos=new ObjectOutputStream(server.getOutputStream());
        oos.writeObject(new Message(ciphertext));
        oos.close();
    }

}
