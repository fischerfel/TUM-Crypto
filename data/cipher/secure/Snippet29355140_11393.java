import java.io.*;
import java.io.IOException;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.GeneralSecurityException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.io.FileUtils;

public class RsaEncryption {
    private Cipher _pkCipher;

    public RsaEncryption() throws GeneralSecurityException {
        // create RSA public key cipher
        _pkCipher = Cipher.getInstance("RSA");
    }

    public String loadKey(File in, String privateKey) throws GeneralSecurityException, IOException, Exception {
        privateKey = privateKey.replaceAll("-+.*?-+", "");
        byte[] encodedKey = Base64.decodeBase64(privateKey);

        // create private key
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey pk = (RSAPrivateKey) kf.generatePrivate(privateKeySpec);

        // read AES key
        _pkCipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] encryptedBytes       = FileUtils.readFileToByteArray(in);
        ByteArrayInputStream fileIn = new ByteArrayInputStream(encryptedBytes);
        CipherInputStream cis       = new CipherInputStream(fileIn, _pkCipher);
        DataInputStream dis         = new DataInputStream(cis);
        byte[] decryptedData        = new byte[32];
        dis.read(decryptedData);
        String key = new String(decryptedData);
        return key;
    }
}
