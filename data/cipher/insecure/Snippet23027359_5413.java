package custom_enc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
public class Custom_enc {

String ekey="";
String algorithm="";
String path1="";
File f;

public void Custom_enc()
{
    System.out.println("Enter the file name with extension and path : \n");
    Scanner s = new Scanner(System.in);
    String path1 = s.nextLine();
    f = new File(path1);
    System.out.println("Enter secret key : \n");
    ekey = s.nextLine();
}

public void encrypt() throws Exception
{
   Custom_enc();
   this.algorithm="DES/ECB/PKCS5Padding";
    FileInputStream fis =new FileInputStream(f);
    f=new File(f.getAbsolutePath()+"_encrypted_file.txt");
    FileOutputStream fos =new FileOutputStream(f);

    byte k[] = ekey.getBytes();
    SecretKeySpec key = new SecretKeySpec(k,"DES");
    Cipher encrypt = Cipher.getInstance(algorithm);

    encrypt.init(Cipher.ENCRYPT_MODE, key);
    CipherOutputStream cout=new CipherOutputStream(fos, encrypt);
    byte[] buf = new byte[1024];
    int read;

    while((read=fis.read(buf))!=-1) //reading data
        cout.write(buf,0,read); //writing encrypted data

    fis.close();
    cout.flush();
    cout.close();
    System.out.println("Encryption Done!!");
    //exit();
}

public void decrypt() throws Exception
{
    Custom_enc();
    this.algorithm="DES/ECB/PKCS5Padding";
    FileInputStream fis =new FileInputStream(f);
    f=new File(f.getAbsolutePath()+"_decrypted_file.txt");
    FileOutputStream fos =new FileOutputStream(f);

    byte k[] = ekey.getBytes();
    SecretKeySpec key = new SecretKeySpec(k,"DES");

    Cipher decrypt = Cipher.getInstance(algorithm);
    decrypt.init(Cipher.DECRYPT_MODE, key);
    CipherInputStream cin=new CipherInputStream(fis, decrypt);

    byte[] buf = new byte[1024];
    int read=0;

    while((read=cin.read(buf))!=-1) //reading encrypted data
    {
        fos.write(buf,0,read); //writing decrypted data
    }

    cin.close();
    fos.flush();
    fos.close();

    System.out.println("Encryption Done!!");
    //1exit();

}

public static void main(String[] args) throws Exception,     java.security.InvalidKeyException {
    Custom_enc obj = new Custom_enc();
    System.out.println("Enter your choice : \n 1 For Encryption \n 2 For Decryption");
    Scanner s1 = new Scanner(System.in);
    int choice = s1.nextInt();
    if(choice==1)
    {
        System.out.println("You've chosen to Encrypt\n");
        obj.encrypt();
    }
    else if(choice==2)
    {
        System.out.println("You've chosen to Decrypt\n");
        obj.decrypt();
    }
    else
    {
        System.out.println("Invalid Choice, Try again...");
    }
}

}
