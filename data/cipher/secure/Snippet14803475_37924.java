import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            Socket socket = new Socket(InetAddress.getByName("192.168.0.9"), 12345);
            Socket arSocket = new Socket(InetAddress.getByName("example.com"), 23073);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(arSocket.getOutputStream());
            String line;
            while((line = input.readLine()) != null)
            {
                sendBytes(rsaEncrypt(line.getBytes()), dos);
            }
        }
        catch(SocketException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
            ex.printStackTrace();
        }

    }

    public static void sendBytes(byte[] myByteArray, DataOutputStream dos) throws IOException 
    {
        sendBytes(myByteArray, 0, myByteArray.length, dos);
    }

    public static void sendBytes(byte[] myByteArray, int start, int len, DataOutputStream dos) throws IOException 
    {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);

        dos.writeInt(len);
        if (len > 0) 
            dos.write(myByteArray, start, len);
    }

    static public byte[] rsaEncrypt(byte[] data) throws Exception
    {
        if(Test.pubKey == null)
            Test.pubKey = readKeyFromFile("public.key");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    }

    static PublicKey pubKey = null;

    static PublicKey readKeyFromFile(String keyFileName) throws IOException 
    {

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(keyFileName));
        try 
        {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Spurious serialisation error", e);
        } 
        finally 
        {
            oin.close();
        }
    }

}
