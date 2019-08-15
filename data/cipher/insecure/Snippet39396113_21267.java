package org.mycompany.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
//Apache commons IO
import org.apache.commons.io.IOUtils;

/**
 * Componente de soporte para codificar y descodificar mensajes
 * 
 * @author opentrends
 *
 */
public final class EncryptHelper {

    public static final String decrypt(final String encrypted, final String encoding)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, IOException, InvalidAlgorithmParameterException, NoSuchProviderException {
        //Mainly UTF-8
        Charset charset =  Charset.forName(encoding);

        //Decoding binary.
        byte[] base64CryptedMessageByteArr = Base64.getDecoder().decode(encrypted);

        //Init of descipher
        Cipher desCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[8]);
        desCipher.init(Cipher.DECRYPT_MODE, generateSecretKey(charset),ivSpec);

        //Decrypting binary
        byte[] byteDecryptedTextByteArr = desCipher.doFinal(base64CryptedMessageByteArr);
        String clearText = new String(byteDecryptedTextByteArr, encoding);
        return clearText;
    }




private final static Key generateSecretKey(Charset charset) throws IOException{     
            InputStream secretKeyFile = RACEEncrypter.class.getResourceAsStream("/DESedeRACE.key");
        InputStreamReader secretKeyReader = new InputStreamReader(secretKeyFile);
        byte[] scretKeyByteArr = IOUtils.toByteArray(secretKeyReader);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = factory.generateSecret(new DESedeKeySpec(scretKeyByteArr));
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }           
        }       
    }
