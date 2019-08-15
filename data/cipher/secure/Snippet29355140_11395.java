import java.io.StringReader;
import java.io.File;
import java.io.IOException;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class RsaEncryption {
    private Cipher _pkCipher;
    private RSAPrivateKey _PrivateKey;
    private RSAPublicKey  _PublicKey;

    public RsaEncryption(String privateKey) throws GeneralSecurityException, IOException {
        loadKey(privateKey);
        // create RSA public key cipher
        _pkCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
    }

    private void loadKey(String privateKey) throws IOException {
        PEMParser pemParser          = new PEMParser(new StringReader(privateKey));
        PEMKeyPair pemKeyPair        = (PEMKeyPair) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair keyPair              = converter.getKeyPair(pemKeyPair);
        _PrivateKey                  = (RSAPrivateKey) keyPair.getPrivate();
        _PublicKey                   = (RSAPublicKey) keyPair.getPublic();
        pemParser.close();
    }

    public String decrypt(File in) throws GeneralSecurityException , IOException{
        _pkCipher.init(Cipher.DECRYPT_MODE, _PrivateKey);
        byte[] encryptedBytes = FileUtils.readFileToByteArray(in);
        String key = new String(_pkCipher.doFinal(encryptedBytes));
        System.out.println(key);
        return key;
    }


    public RSAPrivateKey getPrivateKey() { return _PrivateKey; }
    public RSAPublicKey getPublicKey()   { return _PublicKey;  }
}
