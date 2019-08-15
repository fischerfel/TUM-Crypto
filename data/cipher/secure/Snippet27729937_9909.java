package user.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.KeyStore.PasswordProtection;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.blowfish.core.Blowfish;

import user.util.UserConstants;

import user.bean.UserLoginBean;
import user.util.DButil;



public class UserDao {

public int insertUserDetails(Object bean)
{

    user.bean.UserLoginBean beanobj=(UserLoginBean)bean;
    Connection conn=null;
    PreparedStatement ps=null;
    PreparedStatement ps1=null;
    ResultSet rs = null;
    int result=0;
    try{

    conn=DButil.getConnection();
    StringBuffer sbinsert=new StringBuffer();
    sbinsert.append("insert into ");
    sbinsert.append(UserConstants.USER_DETAILS_TABLE_NAME);
    sbinsert.append(" values(?,?,?,?,?,?)");

    //KeyStore Table

    conn=DButil.getConnection();
    StringBuffer sbinsert1 =new StringBuffer();
    sbinsert1.append("insert into ");
    sbinsert1.append(UserConstants.USER_DETAILS_TABLE_NAME1);
    sbinsert1.append(" values(?,?)");

    //

    Security.addProvider(new blowfishProvider());


    Cipher cipher = Cipher.getInstance("AES128_CBC", "blowfish");
    KeyGenerator keyGen = KeyGenerator.getInstance("AES", "blowfish");
    SecretKey secKey = keyGen.generateKey();



    // Storing the secret Key
                final String keyStoreFile = "C:\\mykey.jceks";
                 //String keyStoreFile = new String(sbinsert1);
                //final String keyStoreDB = beanobj.getKeylock();
                 KeyStore keyStore = createKeyStore(keyStoreFile, "java0123");
                 System.out.println("Stored Key: " + (secKey));
                 System.out.println("secured key: " + (keyStore));


    // store the secret key
                 KeyStore.SecretKeyEntry keyStoreEntry = new KeyStore.SecretKeyEntry(secKey);
                 PasswordProtection keyPassword = new PasswordProtection("www-secret".toCharArray());
                 keyStore.setEntry("mySecretKey", keyStoreEntry, keyPassword);
                 keyStore.store(new FileOutputStream(keyStoreFile), "java0123".toCharArray());
                 //keyStore.store(new FileOutputStream(keyStoreDB), "java0123".toCharArray());


    //Encryption of string
    String clearText = beanobj.getPassword() ;
    byte[] clearTextBytes = clearText.getBytes("UTF8");
    cipher.init(Cipher.ENCRYPT_MODE, secKey);
    byte[] cipherBytes = cipher.doFinal(clearTextBytes);
    String cipherText = new String(cipherBytes, "UTF8");

    //Decryption of String
    cipher.init(Cipher.DECRYPT_MODE, secKey );
    byte[] decryptedBytes = cipher.doFinal(cipherBytes);
    String decryptedText = new String(decryptedBytes, "UTF8");

    System.out.println("Before encryption: " + clearText);
    System.out.println("After encryption: " + cipherText);
    System.out.println("After decryption: " + decryptedText);

    //


    ps=conn.prepareStatement(sbinsert.toString());
    ps.setString(1,beanobj.getFirstname());
    ps.setString(2, beanobj.getLastname());
    ps.setString(3, beanobj.getUsername());
    ps.setString(4, cipherText);
    ps.setString(5, beanobj.getEmail());
    ps.setString(6, beanobj.getMobileno());

    ps1=conn.prepareStatement(sbinsert1.toString());
    beanobj.setKeylock("mykey"); // Dummy key for checking if logic works
    ps1.setString(1,beanobj.getUsername());
    ps1.setString(2,beanobj.getKeylock());

    result=ps.executeUpdate();
    result=ps1.executeUpdate();

    }
    catch(SQLException e)
    {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();


    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    finally{
        DButil.closeAllDBResources(conn, ps, null);
    }
    return result;


    }
public boolean isRegisteredUser(String un,String pw)
{
    boolean result=false;
    Connection conn=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    try
    {
        conn=DButil.getConnection();
        StringBuffer sbselect=new StringBuffer();
        sbselect.append("select * from ");
        sbselect.append(UserConstants.USER_DETAILS_TABLE_NAME);
        sbselect.append(" where BINARY Username=? and Password=?");
        ps=conn.prepareStatement(sbselect.toString());


        // Retreving the key 

        Security.addProvider(new FlexiCoreProvider());
        Cipher cipher1 = Cipher.getInstance("AES128_CBC", "FlexiCore");
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        System.out.println(keyStore);
        FileInputStream fis = new FileInputStream("C:\\mykey.jceks";);

        keyStore.load(fis, "java0123".toCharArray());              
        Key secKey = keyStore.getKey("mySecret","www-secret".toCharArray());


        //Encrypting the User Passowrd and comparing with the DB enPassword one way process

        String clearText = pw ;
        byte[] clearTextBytes = clearText.getBytes("UTF8");
        cipher1.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] cipherBytes = cipher1.doFinal(clearTextBytes);
        String cipherText1 = new String(cipherBytes, "UTF8");




        ps.setString(1, un);
        ps.setString(2, cipherText1);
        //System.out.println(ps.toString());
        rs=ps.executeQuery();
        if(rs.next())
        {
            result=true;
        }
    }
    catch(SQLException e)
    {
        System.out.println(e);
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (CertificateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    finally
    {
        DButil.closeAllDBResources(conn, ps, rs);
    }


    return result;
}
public int executeUpdate(String query)
{
    int result=0;
    Connection conn=null;
    PreparedStatement ps=null;
    try
    {
        conn=DButil.getConnection();
        ps=conn.prepareStatement(query);
        result=ps.executeUpdate();

    }
    catch(SQLException e)
    {
        e.printStackTrace();

    }
    finally{
        DButil.closeAllDBResources(conn, ps, null);
    }
    return result;
}

private static java.security.KeyStore createKeyStore(String keyStoreFile,
        String pw) throws Exception {
    // TODO Auto-generated method stub
     File file = new File("mykey.jceks");
     /**
      * Note that if you are storing a SecretKey or using any part of the SunJCE provider
      *  (Java Cryptography Extension),
      *  you will need to set your KeyStore type to JCEKS.
      */
        final KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (file.exists()) {
            // .keystore file already exists => load it
            keyStore.load(new FileInputStream(file), www.toCharArray());
        } else {
            // .keystore file not created yet => create it
            keyStore.load(null, null);
            keyStore.store(new FileOutputStream("mykey1.jceks"), www.toCharArray());
        }

        return keyStore;
       }

       }
