import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

import sun.security.pkcs11.SunPKCS11;

public class App {

    public static void main(String[] args) throws Exception {

        try {
            String passphrase = "mysecretkey";
            SunPKCS11 provider = new SunPKCS11("/home/user/pkcs11.cfg");
            KeyStore keystore = KeyStore.getInstance("PKCS11", provider);
            keystore.load(null, passphrase.toCharArray());
            String textToEncrypt = "this is my text";
            Certificate cert = keystore.getCertificate("my-SHA1WITHRSA-2048-bits-key");
            PublicKey publicKey = cert.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedData = DatatypeConverter.printBase64Binary(cipher.doFinal(textToEncrypt.getBytes()));

            PrivateKey privateKey = (PrivateKey) keystore.getKey("my-SHA1WITHRSA-2048-bits-key",
                    passphrase.toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodedEncryptedData = DatatypeConverter.parseBase64Binary(encryptedData);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int blocks = decodedEncryptedData.length / 256;
            int offset = 0;
            for (int blockIndex = 0; blockIndex < blocks; blockIndex++) {
                byte[] nextBlock = getNextBlock(decodedEncryptedData, offset);
                stream.write(cipher.doFinal(nextBlock));
                offset += 256;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static byte[] getNextBlock(byte[] cipherText, int offset) {
        byte[] block = new byte[256];
        System.arraycopy(cipherText, offset, block, 0, 256);
        return block;
    }

}
