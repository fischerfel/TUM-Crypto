import java.io.*;
import java.security.*;
import java.security.KeyStore.PasswordProtection;
import java.security.cert.CertificateException;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.encoders.Base64;

public class KeyPairUtil {

final static String keyStoreFile = "D:\\aeskey.jks";

private static final ASN1ObjectIdentifier AES = ASN1ObjectIdentifier.getInstance(NISTObjectIdentifiers.id_aes128_CBC);

public static void main(String[] args) throws Exception {

    final java.security.KeyPairGenerator gen = java.security.KeyPairGenerator.getInstance("RSA");
    gen.initialize(1024);
    final KeyPair keyPair = gen.generateKeyPair();
    wrapKeypairWithSymmetricKey(keyPair);
}

public static KeyPair wrapKeypairWithSymmetricKey(KeyPair keyPair) {

    try {
        PrivateKey priv = keyPair.getPrivate();
        SecretKey symmetricKey = getSymmetricKeyFromJKSFile();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        cipher.init(Cipher.WRAP_MODE, symmetricKey, iv);
        System.out.println(iv.getIV());
        ASN1Encodable params = new DEROctetString(iv.getIV());
        AlgorithmIdentifier algId = new AlgorithmIdentifier(AES, params);
        byte[] wrappedKey = cipher.wrap(priv);
        KeyFactory keyFactory = KeyFactory.getInstance(priv.getAlgorithm());
        byte[] pkcs8enc = new EncryptedPrivateKeyInfo(algId, wrappedKey).getEncoded();
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pkcs8enc);
        PrivateKey privateKey2 = keyFactory.generatePrivate(privateKeySpec); //throwing error in this line
        KeyPair keypair = new KeyPair(keyPair.getPublic(), privateKey2);
        return keypair;
    } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | IOException | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return null;
}

private static SecretKey getSymmetricKeyFromJKSFile() {

    String jkspassword = "password";
    PasswordProtection keyPassword = new PasswordProtection("keypassword".toCharArray());
    try {
        KeyStore keyStore = loadKeyStore(keyStoreFile, jkspassword);
        // retrieve the stored key back
        KeyStore.Entry entry = keyStore.getEntry("keyentry", keyPassword);
        SecretKey keyFound = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        return keyFound;
    } catch (CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
        e.printStackTrace();
    }
    return null;
}

private static KeyStore loadKeyStore(String fileName, String jkspassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

    File file = new File(fileName);
    final KeyStore keyStore = KeyStore.getInstance("JCEKS");
    if (file.exists()) {
        keyStore.load(new FileInputStream(file), jkspassword.toCharArray());
    }
    return keyStore;
}
}
