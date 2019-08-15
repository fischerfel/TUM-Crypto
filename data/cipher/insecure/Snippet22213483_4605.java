import java.io.*;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Arrays;


import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

public class CipherStreamDemo {
private static final byte[] salt={
    (byte)0xC9, (byte)0xEF, (byte)0x7D, (byte)0xFA,
    (byte)0xBA, (byte)0xDD, (byte)0x24, (byte)0xA9
};
private Cipher cipher;
private final SecretKey key;
public CipherStreamDemo() throws GeneralSecurityException, IOException{
    SecretKeyFactory kf=SecretKeyFactory.getInstance("DES");
    KeySpec spec=new DESKeySpec(salt);
    key=kf.generateSecret(spec);
    cipher=Cipher.getInstance("DES");
}
public void encrypt(byte[] buf) throws IOException, GeneralSecurityException{
    cipher.init(Cipher.ENCRYPT_MODE,key);
    OutputStream out=new CipherOutputStream(new FileOutputStream("crypt.dat"), cipher);
    out.write(buf);
    out.close();
}
public byte[] decrypt() throws IOException, GeneralSecurityException{
    cipher.init(Cipher.DECRYPT_MODE, key);
    InputStream in=new CipherInputStream(new FileInputStream("crypt.dat"), cipher);
    byte[] buf=new byte[300];
    int bytes=in.read(buf);
    buf=Arrays.copyOf(buf, bytes);
    in.close();
    return buf;
}
public static void main(String[] args) {
    try{
        CipherStreamDemo csd=new CipherStreamDemo();
        String pass="thisisasecretpassword";
        csd.encrypt(pass.getBytes());
        System.out.println(new String(csd.decrypt()));
        }catch(Exception e){
            e.printStackTrace();
        }
}
}
//Output: thisisasecretpass
