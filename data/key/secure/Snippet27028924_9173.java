package CryptoFunction;

import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Utility class for encrypting/decrypting files.
 * by: Michael Lones
 * Minor Revisions by Glenn Hall
 */
 public class CryptoFunction {

public static final int AES_Key_Size = 256;

Cipher pkCipher, aesCipher;
byte[] aesKey;
SecretKeySpec aeskeySpec;

/**
 * Constructor: creates ciphers
 */
public CryptoFunction() throws GeneralSecurityException {

     // create RSA public key cipher
pkCipher = Cipher.getInstance("RSA");

     // create AES shared key cipher
aesCipher = Cipher.getInstance("AES");
}

/**
 * Creates a new AES key
 * A random AES key is generated to encrypt files. 
 * A key size (AES_Key_Size) of 256 bits is standard for AES
 */
public void makeKey() throws NoSuchAlgorithmException {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(AES_Key_Size);
    SecretKey key = kgen.generateKey();
    aesKey = key.getEncoded();
    aeskeySpec = new SecretKeySpec(aesKey, "AES");
}

/**
 * Decrypts an AES key from a file using an RSA private key
 */
@SuppressWarnings("resource")
public void loadKey(File in, File privateKeyFile) throws GeneralSecurityException, IOException {
// read private key to be used to decrypt the AES key
byte[] encodedKey = new byte[(int)privateKeyFile.length()];             new FileInputStream(privateKeyFile).read(encodedKey);

// create private key
PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
KeyFactory kf = KeyFactory.getInstance("RSA");
PrivateKey pk = kf.generatePrivate(privateKeySpec);

// read AES key
pkCipher.init(Cipher.DECRYPT_MODE, pk);
aesKey = new byte[AES_Key_Size/8];
CipherInputStream is = new CipherInputStream(new FileInputStream(in), pkCipher);
is.read(aesKey);
aeskeySpec = new SecretKeySpec(aesKey, "AES");
}

/**
 * Encrypts the AES key to a file using an RSA public key
 */
@SuppressWarnings("resource")
public void saveKey(File out, File publicKeyFile) throws IOException, GeneralSecurityException {
// read public key to be used to encrypt the AES key
     byte[] encodedKey = new byte[(int)publicKeyFile.length()];
new FileInputStream(publicKeyFile).read(encodedKey);

// create public key
X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
KeyFactory kf = KeyFactory.getInstance("RSA");
PublicKey pk = kf.generatePublic(publicKeySpec);

// write AES key
pkCipher.init(Cipher.ENCRYPT_MODE, pk);
CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher);
os.write(aesKey);
os.close();
}

/**
 * Encrypts and then copies the contents of a given file.
 */
public void encrypt(File in, File out) throws IOException, InvalidKeyException {

aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);

FileInputStream is = new FileInputStream(in);
CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher);       
System.out.println(out.getAbsolutePath());
copy(is, os);

os.close();
}

/**
 * Decrypts and then copies the contents of a given file.
 */
public void decrypt(File in, File out) throws IOException, InvalidKeyException {

        aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);

    CipherInputStream is = new CipherInputStream(new FileInputStream(in), aesCipher);
    FileOutputStream os = new FileOutputStream(out);
    System.out.println(out.getAbsolutePath());
    copy(is, os);

    is.close();
    os.close();
}

/**
 * Copies a stream.
 */
private void copy(InputStream is, OutputStream os) throws IOException {
    int i;
    byte[] b = new byte[1024];
    while((i=is.read(b))!=-1) {
        os.write(b, 0, i);
    }
}


public static void main(String[] args) throws GeneralSecurityException, IOException {


    CryptoFunction secure = new CryptoFunction();

    // Encrypt code
    System.out.println("Begin Encyption!");

    File encryptFile = new File("encrypt.data");
              File publicKeyData = new File("src/publickey.der");
              File originalFile = new File("src/stufftoencrypt.txt");
              File secureFile = new File("secure.data");

    // create AES key
    secure.makeKey();

    // save AES key using public key
    secure.saveKey(encryptFile, publicKeyData);

    // save original file securely 
    secure.encrypt(originalFile, secureFile);
    System.out.println("End Encryption!"); 
    System.out.println("Begin decryption!"); 

    // Decrypt code  
    //File encryptFile1 = new File("encrypt.data");
    File privateKeyFile = new File("src/privatekey.der");
    File secureFile1 = new File("secure.data");
    File unencryptedFile = new File("unencryptedFile.txt");

    // load AES key
    secure.loadKey(encryptFile, privateKeyFile);

    // decrypt file
    secure.decrypt(secureFile1, unencryptedFile);
    System.out.println("End decryption!"); // Display the string.
}
}
