    import java.lang.Class.*;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import javax.crypto.Cipher;
    import javax.crypto.CipherInputStream;
    import javax.crypto.CipherOutputStream;
    import javax.crypto.SecretKey;
    import javax.crypto.SecretKeyFactory;
    import javax.crypto.spec.DESKeySpec;

public class Example1 {

public String keyGen() {
//create an array used for storing each character    
char array[] = new char[8];

    //for loop checks for each character between '!' and '~'
    for (char c0 = '!'; c0 <= '~'; c0++) {
    array[0] = c0;

    for (char c1 = '!'; c1 <= '~'; c1++) {
    array[1] = c1;

    for (char c2 = '!'; c2 <= '~'; c2++) {
    array[2] = c2;

    for (char c3 = '!'; c3 <= '~'; c3++) {
    array[3] = c3;

    for (char c4 = '!'; c4 <= '~'; c4++) {
    array[4] = c4;

    for (char c5 = '!'; c5 <= '~'; c5++) {
    array[5] = c5;

    for (char c6 = '!'; c6 <= '~'; c6++) {
    array[6] = c6;

    for (char c7 = '!'; c7 <= '~'; c7++) {
    array[7] = c7;

    //create new string that stores the array
    String pKey = new String(array);

    //trying to return the new string 
    return pKey;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } 
                }
            }

public static void main(String []args) {
try {

// I am getting an error here; I know it has something to do with static references

    String key = new String(keyGen(pKey); 

                    // needs to be at least 8 characters for DES

        FileInputStream fis = new FileInputStream("original.txt");
        FileOutputStream fos = new FileOutputStream("encrypted.txt");
        encrypt(key, fis, fos);

        FileInputStream fis2 = new FileInputStream("encrypted.txt");
        FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
        decrypt(key, fis2, fos2);
    } catch (Throwable e) {
        e.printStackTrace();
    }
}

public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
    encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
}

public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
    encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
}

public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

    DESKeySpec dks = new DESKeySpec(key.getBytes());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = skf.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

    if (mode == Cipher.ENCRYPT_MODE) {
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    } else if (mode == Cipher.DECRYPT_MODE) {
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        CipherOutputStream cos = new CipherOutputStream(os, cipher);
        doCopy(is, cos);
    }
}

public static void doCopy(InputStream is, OutputStream os) throws IOException {
    byte[] bytes = new byte[64];
    int numBytes;
    while ((numBytes = is.read(bytes)) != -1) {
        os.write(bytes, 0, numBytes);
    }
    os.flush();
    os.close();
    is.close();
}
