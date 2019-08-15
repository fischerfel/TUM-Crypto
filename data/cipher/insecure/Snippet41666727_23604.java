import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Cryption {
    private Key publicKey;
    private PrivateKey privateKey;
    private String AES = "AES";
    private String RSA = "RSA";
    public Cryption() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA, "BC");
            keyPairGenerator.initialize(1024);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public byte[] getPublicKeyBytes() {
        return publicKey.getEncoded();
    }


    ////////////ENCRYPT


    public EncryptedData encrypt(Object o, byte[] keyRSA) {
        return encrypt(Utils.serialize(o), keyRSA);
    }

    public EncryptedData encrypt(byte[] bytes, byte[] keyRSA) {

        try {
            //get AES Random Key

            KeyGenerator keygen = KeyGenerator.getInstance(AES, "BC");
            keygen.init(128);
            Key aesKey= keygen.generateKey();
            byte[] aesKeyByte = aesKey.getEncoded();

            //AES encryption
            byte[] dataEncyptedAES = null;
            Cipher aescipher = Cipher.getInstance(AES, "BC");
            aescipher.init(Cipher.ENCRYPT_MODE, aesKey);
            dataEncyptedAES = aescipher.doFinal(bytes);

            //encode AES Key
            byte[] encodedAESkey = encodeRSA(aesKeyByte, keyRSA);
            return new EncryptedData(encodedAESkey, dataEncyptedAES);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] encodeRSA(byte[] data, byte[] keyRSA) {
        try {
            AsymmetricKeyParameter publicKey = PublicKeyFactory.createKey(keyRSA);
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(true, publicKey);

            List<Byte> value = new ArrayList<Byte>();
            int i = 0;
            int len = e.getInputBlockSize();
            while(i<data.length) {
                if(i+len > data.length) len = data.length - i;
                byte[] hexEncodedCipher = e.processBlock(data, i ,len);
                for(Byte b : hexEncodedCipher) {
                    value.add(b);
                }
                i+=e.getInputBlockSize();
            }
            return Utils.convert(value.toArray(new Byte[value.size()]));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    ///////////////////DECRYPT



    public Object decryptToObject(EncryptedData encryptedData) {
        return Utils.deserialize(decrypt(encryptedData));
    }

    private byte[] decrypt(EncryptedData encryptedData) {
        if(encryptedData == null) System.out.println("ENCRYPTED DATA == NULL");
        try {
            //decode AES key
            byte[] decodedAESKey = decryptRSA(encryptedData.getEncryptedAESKey());

            //decrypt data
            byte[] decodedBytes = null;
            Cipher cipherData = Cipher.getInstance(AES, "BC");
            cipherData.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decodedAESKey, 0, decodedAESKey.length, AES));
            decodedBytes = cipherData.doFinal(encryptedData.getDataEncryptedAES());
            return decodedBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] decryptRSA(byte[] data) {
        try {
            AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(this.privateKey.getEncoded());
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            List<Byte> value = new ArrayList<Byte>();
            int i = 0;
            int len = e.getInputBlockSize();
            while(i<data.length) {
                if(i+len > data.length) len = data.length - i;
                byte[] hexEncodedCipher = e.processBlock(data, i ,len);
                for(Byte b : hexEncodedCipher) {
                    value.add(b);
                }
                i+=e.getInputBlockSize();
            }
            return Utils.convert(value.toArray(new Byte[value.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        privateKey = null;
        publicKey = null;
    }
}
