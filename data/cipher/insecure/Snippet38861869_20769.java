/**
 * 
 */
package com.ebiznext.utils

import java.io.Serializable;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @version $Id $
 *
 */
class ByteSerializer
{
    def static Object deserialize(String string) throws IOException, ClassNotFoundException
    {
        byte[] data = DatatypeConverter.parseBase64Binary(string)
        ObjectInputStream objectInputStream =
            new ObjectInputStream(new ByteArrayInputStream(data))
        Object object = objectInputStream.readObject()
        objectInputStream.close()
        return object
    }

    def static String serialize(Serializable object) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(object)
        objectOutputStream.close()
        return new String(DatatypeConverter.printBase64Binary(byteArrayOutputStream
            .toByteArray()))
    }

    def static String toEncryptedString(Serializable object)
            throws Exception {
        return encrypt(toString(object));
    }

    def static Object fromEncryptedString(String encryptedObject)
            throws Exception {
        return fromString(decrypt(encryptedObject));
    }

    private static final String ALGORITHM = "AES";

    private static final byte[] keyValue = "ThisKeyIsVeryPrivateNobodyKnowsAboutItYeah!!"
            .substring(0, 32).getBytes();

    def static String encrypt(String valueToEnc) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        return encryptedValue;
    }

    def static String decrypt(String encryptedValue) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        // SecretKeyFactory keyFactory =
        // SecretKeyFactory.getInstance(ALGORITHM);
        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
        return key;
    }
}
