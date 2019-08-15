package org.dpdouran.attach;
import java.io.ByteArrayOutputStream;import java.io.IOException;import java.io.InputStream;import java.security.InvalidAlgorithmParameterException;import java.security.InvalidKeyException;import java.security.NoSuchAlgorithmException;import java.security.spec.AlgorithmParameterSpec;import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CustomClassLoader extends ClassLoader {
    private static final int BUFFER_SIZE = 8192;
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        System.out.println("loading...  "+className);
        String clsFile = className.replace('.', '/') + ".class";
        InputStream in = getResourceAsStream(clsFile);
        if(in==null)
            return null;
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int n = -1;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //do decrypt
        byte[] classBytes = out.toByteArray();
        byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
                0x07, 0x72, 0x6F, 0x5A };
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        Cipher dcipher=null;
        try {
            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte keyBytes[] = "abcdEFGH".getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "DES");
        try {
            dcipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] dbytes = null;
        try {
            dbytes = dcipher.doFinal(classBytes);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return defineClass(className, dbytes, 0, dbytes.length);
    }
    public CustomClassLoader( ClassLoader parent){
        super(sun.misc.Launcher.getLauncher().getClassLoader());
    }
}
