package com.Encrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;

public class Encrypt {

public String encyptCard(String card) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException{
        FileInputStream is = new FileInputStream("C:/Users/admin/Desktop/keystore/ksjksformat.jks");    
        String keystpassw = "9801461740";
        String alias = "ksjksformat";       
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());                      
            ks.load(is,keystpassw.toCharArray() );          
            Certificate cert = ks.getCertificate(alias);            
            PublicKey publicKey = cert.getPublicKey();          
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal(card.getBytes());        
        String cipherData1 = Base64.encodeBase64String(cipherData);
        return cipherData1;             
    }
public String decrypte (String encCardNo) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    FileInputStream is = new FileInputStream("C:/Users/admin/Desktop/keystore/ksjksformat.jks");    
    String keystpassw = "9801461740";
    String alias = "ksjksformat";       
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());                      
        ks.load(is,keystpassw.toCharArray() );      
        Key key = ks.getKey(alias, keystpassw.toCharArray());
        Certificate cert = ks.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();
        new KeyPair(publicKey, (PrivateKey) (key));     
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystpassw.toCharArray());
        KeyStore.PrivateKeyEntry pkentry = (PrivateKeyEntry) ks.getEntry(alias, protParam);
        PrivateKey myPrivateKey =pkentry.getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, myPrivateKey); 
        byte[] cipherData = cipher.doFinal(encCardNo.getBytes());
        String decrypted =Base64.decodeBase64(cipherData).toString();
        return decrypted;
}
}
