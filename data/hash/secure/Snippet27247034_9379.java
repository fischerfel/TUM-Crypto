package com.hexninja.datacrypt.crypt;

import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.engines.RijndaelEngine;
import org.spongycastle.crypto.io.CipherOutputStream;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.BlockCipherPadding;
import org.spongycastle.crypto.paddings.PKCS7Padding;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by David on 11/26/2014.
 */
public final class Crypt {

// -- SpongyCastle, i choose you!
// -- ok, so a lame pokemon reference.
// -- but seriously, use this provider!
static {
    Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
}

private static final Random random = new Random();

//Preconfigured Encryption Parameters
public static final int BlockBitSize = 256;
public static final int KeyBitSize = 256;

//Preconfigured Password Key Derivation Parameters
public static final int SaltBitSize = 64;
public static final int Iterations = 3;
public static final int MinPasswordLength = 4;

private static final Charset UTF8 = Charset.forName("UTF-8");

/// <summary>
/// Simple Encryption (AES) then Authentication (HMAC) of a UTF8 message
/// using Keys derived from a Password (PBKDF2).
/// </summary>
/// <param name="secretMessage">The secret message.</param>
/// <param name="password">The password.</param>
/// <param name="nonSecretPayload">The non secret payload.</param>
/// <returns>
/// Encrypted Message
/// </returns>
/// <exception cref="System.ArgumentException">password</exception>
/// <remarks>
/// Significantly less secure than using random binary keys.
/// Adds additional non secret payload for key generation parameters.
/// </remarks>
public static String SimpleEncryptWithPassword(String secretMessage, String password, byte[] nonSecretPayload)
{
    //if (String.IsNullOrEmpty(secretMessage))
    if (secretMessage.isEmpty() || secretMessage == null) {
        throw new IllegalArgumentException("Secret Message Required!");
    }

    //var plainText = Encoding.UTF8.GetBytes(secretMessage);
    //var cipherText = SimpleEncryptWithPassword(plainText, password, nonSecretPayload);
    //return Convert.ToBase64String(cipherText);


    byte[] plainText = null;
    byte[] cipherText = null;
    //byte[] plainText = Encoding.UTF8.GetBytes(secretMessage);
    try {
        plainText = secretMessage.getBytes("UTF-8");
        cipherText = SimpleEncryptWithPassword(plainText, password, nonSecretPayload);
        //return Convert.ToBase64String(cipherText);
    }
    catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    if (cipherText != null) {
    //return Base64.encodeToString(cipherText, Base64.DEFAULT);
    return Base64.toBase64String(cipherText);
    //return new String(Base64.encodeBase64(cipherText), UTF8);
}
else
    return null;



}

/// <summary>
/// Simple Authentication (HMAC) and then Descryption (AES) of a UTF8 Message
/// using keys derived from a password (PBKDF2).
/// </summary>
/// <param name="encryptedMessage">The encrypted message.</param>
/// <param name="password">The password.</param>
/// <param name="nonSecretPayloadLength">Length of the non secret payload.</param>
/// <returns>
/// Decrypted Message
/// </returns>
/// <exception cref="System.ArgumentException">Encrypted Message Required!;encryptedMessage</exception>
/// <remarks>
/// Significantly less secure than using random binary keys.
/// </remarks>
public static String SimpleDecryptWithPassword(String encryptedMessage, String password, int nonSecretPayloadLength) throws GeneralSecurityException, UnsupportedEncodingException {
    //if (String.IsNullOrWhiteSpace(encryptedMessage))
    if (encryptedMessage.isEmpty() || encryptedMessage == null) {
        throw new IllegalArgumentException("Encrypted Message Required!");
    }

    byte[] cipherText = Base64.decode(encryptedMessage);
    byte[] plainText = SimpleDecryptWithPassword(cipherText, password, nonSecretPayloadLength);
    //String s = new String(plainText, "ISO-8859-1");
    //byte[] stream = s.getBytes("ISO-8859-1");

    return new String(plainText, "UTF8");
    //return Base64.toBase64String(plainText);
    //return plainText.toString();
}

public static byte[] SimpleEncrypt(byte[] secretMessage, byte[] cryptKey, byte[] authKey, byte[] nonSecretPayload)
    {
        //User Error Checks
        if (cryptKey == null || cryptKey.length != KeyBitSize / 8)
            throw new IllegalArgumentException("Key needs to be " + KeyBitSize + " bit!");

        if (authKey == null || authKey.length != KeyBitSize / 8)
            throw new IllegalArgumentException("Key needs to be " + KeyBitSize + " bit!");

        if (secretMessage == null || secretMessage.length < 1)
            throw new IllegalArgumentException("Secret Message Required!");

        //non-secret payload optional
        //nonSecretPayload = nonSecretPayload ?? new byte[] { };
        if (nonSecretPayload == null) {
            nonSecretPayload = new byte[] { };
        }

        byte[] cipherText = null;
        //byte[] iv = null;

        System.out.println("SimpleEncrypt stage 1");

    try {

        System.out.println("SimpleEncrypt stage 2");

        //Grab IV from message
        byte[] ivData = new byte[BlockBitSize / 8];
        Random r = new Random(); // Note: no  seed here, ie these values are truly random
        r.nextBytes(ivData);

        // Select encryption algorithm and padding : AES with CBC and PCKS#7
        CipherParameters params = new ParametersWithIV(new KeyParameter(cryptKey), ivData);
        BlockCipherPadding padding = new PKCS7Padding();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine(256)), padding);

        cipher.reset();
        cipher.init(true, params); // first param = encode/decode

        // -- a wannabe using statement..
        // -- c# like memorystream
        try (ByteArrayOutputStream cipherStream = new ByteArrayOutputStream()) {
            // -- c# like CryptoStream
            try (CipherOutputStream cryptoStream = new CipherOutputStream(cipherStream, cipher)) {
                // -- binaryWriter like c#
                try (DataOutputStream binaryWriter = new DataOutputStream(cryptoStream)) {
                    // -- Encrypt Data
                    binaryWriter.write(secretMessage);
                }
                cipherText = cipherStream.toByteArray();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SimpleEncrypt stage 3");

        //Assemble encrypted message and add authentication
        try {
            System.out.println("SimpleEncrypt stage 4");
            Mac hmac = Mac.getInstance("HmacSHA256");
            System.out.println("authKey =" + Base64.toBase64String(authKey));
            SecretKey signingKey = new SecretKeySpec(authKey, "HMACSHA256");
            System.out.println("authKey=" +  Base64.toBase64String(authKey));
            hmac.init(signingKey);
            try (ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream()) {
                try (DataOutputStream binaryWriter = new DataOutputStream(encryptedStream)) {
                    //Prepend non-secret payload if any
                    binaryWriter.write(nonSecretPayload);
                    //Prepend IV
                    binaryWriter.write(ivData);
                    //Write Ciphertext
                    binaryWriter.write(cipherText);
                    binaryWriter.flush();


                    System.out.println("computeByte=" +  Base64.toBase64String(encryptedStream.toByteArray()));

                    //Authenticate all data
                    //var tag = hmac.ComputeHash(encryptedStream.ToArray());
                    //hmac.init(secretKeySpecy);
                    byte[] tag = hmac.doFinal(encryptedStream.toByteArray());
                    //System.out.println("tag text=" + tag.length);
                    System.out.println("tag=" +  Base64.toBase64String(tag));
                    //Postpend tag
                    binaryWriter.write(tag);
                }
                return encryptedStream.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

    return null;


}

public static byte[] SimpleDecrypt(byte[] encryptedMessage, byte[] cryptKey, byte[] authKey, int nonSecretPayloadLength)
{

    //Basic Usage Error Checks
    if (cryptKey == null || cryptKey.length != KeyBitSize / 8)
        throw new IllegalArgumentException("CryptKey needs to be " + KeyBitSize + " bit!");

    if (authKey == null || authKey.length != KeyBitSize / 8)
        throw new IllegalArgumentException("AuthKey needs to be " + KeyBitSize + " bit!");

    if (encryptedMessage == null || encryptedMessage.length == 0)
        throw new IllegalArgumentException("Encrypted Message Required!");


    try {
        // verify HMAC!
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKey signingKey = new SecretKeySpec(authKey, "HMACSHA256");
        hmac.init(signingKey);

        System.out.println("authKey=" +  Base64.toBase64String(authKey));


        // -- hmac is already 32 bytes.. I was expecting bits. (c# does bits)
        //byte[] sentTag = new byte[sha256_HMAC.getMacLength() / 8];
        byte[] sentTag = new byte[hmac.getMacLength()];




        byte[] computeByte = new byte[encryptedMessage.length - sentTag.length];
        System.arraycopy(encryptedMessage, 0, computeByte, 0, computeByte.length);

        System.out.println("computeByte=" +  Base64.toBase64String(computeByte));

        // -- fix for missing ComputeHash from c#
        //MessageDigest m = MessageDigest.getInstance("HMACSHA256");
        //byte[] digest = m.digest(computeByte);
        //String hashcode = new BigInteger(1,m.digest()).toString(16);

        //System.out.println("hashcode=" +  hashcode);


        byte[] calcTag = hmac.doFinal(computeByte);
        int ivLength = (BlockBitSize / 8);

        System.out.println("calcTag=" +  Base64.toBase64String(calcTag));

        //if message length is to small just return null
        if (encryptedMessage.length < sentTag.length + nonSecretPayloadLength + ivLength)
            return null;

        System.out.println("we are past encrypted Message length");

        //Grab Sent Tag
        System.arraycopy(encryptedMessage, encryptedMessage.length - sentTag.length, sentTag, 0, sentTag.length);


        System.out.println("sentTag=" +  Base64.toBase64String(sentTag));

        //Compare Tag with constant time comparison
        int compare = 0;
        for (int i = 0; i < sentTag.length; i++)
            compare |= sentTag[i] ^ calcTag[i];

        System.out.println("compare =" + compare);

        //if message doesn't authenticate return null
        if (compare != 0)
            return null;



        //Grab IV from message
        byte[] iv = new byte[ivLength];
        System.arraycopy(encryptedMessage, nonSecretPayloadLength, iv, 0, iv.length);

        System.out.println("iv=" +  Base64.toBase64String(iv));

        //iv = ivSpec.getIV();
        CipherParameters params = new ParametersWithIV(new KeyParameter(cryptKey), iv);
        BlockCipherPadding padding = new PKCS7Padding();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine(256)), padding);

        cipher.reset();
        cipher.init(false, params);

        try (ByteArrayOutputStream plainTextStream = new ByteArrayOutputStream()) {
            // -- c# like CryptoStream
            try (CipherOutputStream decrypterStream = new CipherOutputStream(plainTextStream, cipher)) {
                // -- binaryWriter like c#
                try (DataOutputStream binaryWriter = new DataOutputStream(decrypterStream)) {
                    // -- Encrypt Data
                    //Decrypt Cipher Text from Message
                    binaryWriter.write(
                            encryptedMessage,
                            nonSecretPayloadLength + iv.length,
                            encryptedMessage.length - nonSecretPayloadLength - iv.length - sentTag.length
                    );
                }
                //Return Plain Text
                //decrypterStream.flush();

            } finally {
                return plainTextStream.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

    return null;
}

public static byte[] SimpleEncryptWithPassword(byte[] secretMessage, String password, byte[] nonSecretPayload) throws NoSuchAlgorithmException {
    if (nonSecretPayload == null) {
        nonSecretPayload = new byte[] { };
    }

    //User Error Checks
    if (password.isEmpty() || password == null || password.length() < MinPasswordLength) {
        throw new IllegalArgumentException("Must have a password of at least "+MinPasswordLength+" characters!");
    }

    if (secretMessage == null || secretMessage.length == 0)
        throw new IllegalArgumentException("Secret Message Required!");

    byte[] payload = new byte[((SaltBitSize / 8) * 2) + nonSecretPayload.length];

    System.arraycopy(nonSecretPayload, 0, payload, 0, nonSecretPayload.length);
    //Array.Copy(nonSecretPayload, payload, nonSecretPayload.Length);
    int payloadIndex = nonSecretPayload.length;

    byte[] cryptKey = null;
    byte[] authKey = null;

    try {

        Random r = new SecureRandom();
        byte[] salt = new byte[SaltBitSize / 8];
        r.nextBytes(salt);


        Rfc2898DeriveBytes generator = new Rfc2898DeriveBytes(password, salt, Iterations);
        //Generate Keys
        cryptKey = generator.getBytes(KeyBitSize / 8);

        //Create Non Secret Payload
        System.arraycopy(salt, 0, payload, payloadIndex, salt.length);
        //System.arraycopy(nonSecretPayload, 0, payload, 0, nonSecretPayload.length);
        payloadIndex += salt.length;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }


    try {
        Random r = new SecureRandom();
        byte[] salt = new byte[SaltBitSize / 8];
        r.nextBytes(salt);

        Rfc2898DeriveBytes generator = new Rfc2898DeriveBytes(password, salt, Iterations);
        //Generate Keys
        authKey = generator.getBytes(KeyBitSize / 8);

        //Create Non Secret Payload
        System.arraycopy(salt, 0, payload, payloadIndex, salt.length);
        //System.arraycopy(nonSecretPayload, 0, payload, 0, nonSecretPayload.length);
        payloadIndex += salt.length;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    System.out.println("cryptKey=" +  Base64.toBase64String(cryptKey));
    System.out.println("authKey=" + Base64.toBase64String(authKey));

    return SimpleEncrypt(secretMessage, cryptKey, authKey, payload);
}

public static byte[] SimpleDecryptWithPassword(byte[] encryptedMessage, String password, int nonSecretPayloadLength) throws GeneralSecurityException {
    //User Error Checks
    if (password.isEmpty() || password == null || password.length() < MinPasswordLength) {
        throw new IllegalArgumentException("Must have a password of at least "+MinPasswordLength+" characters!");
    }

    if (encryptedMessage == null || encryptedMessage.length == 0)
        throw new IllegalArgumentException("Encrypted Message Required!");

    byte[] cryptSalt = new byte[SaltBitSize / 8];
    byte[] authSalt = new byte[SaltBitSize / 8];

    //Grab Salt from Non-Secret Payload
    System.arraycopy(encryptedMessage, nonSecretPayloadLength, cryptSalt, 0, cryptSalt.length);
    System.arraycopy(encryptedMessage, nonSecretPayloadLength + cryptSalt.length, authSalt, 0, authSalt.length);

    System.out.println("cryptSalt=" + Base64.toBase64String(cryptSalt));
    System.out.println("authSalt=" + Base64.toBase64String(authSalt));


    Rfc2898DeriveBytes generator = null;
    try {
        generator = new Rfc2898DeriveBytes(password, cryptSalt, Iterations);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    byte[] cryptKey = generator.getBytes(KeyBitSize / 8);

    try {
        generator = new Rfc2898DeriveBytes(password, authSalt, Iterations);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    byte[] authKey = generator.getBytes(KeyBitSize / 8);

    //byte[] cryptKey = Rfc2898DeriveBytes(password, cryptSalt, Iterations);
    //byte[] authKey = Rfc2898DeriveBytes(password, authSalt, Iterations);

    System.out.println("cryptKey=" +  Base64.toBase64String(cryptKey));
    System.out.println("authKey=" + Base64.toBase64String(authKey));

    return SimpleDecrypt(encryptedMessage, cryptKey, authKey, cryptSalt.length + authSalt.length + nonSecretPayloadLength);
}

/*
private static byte[] Rfc2898DeriveBytes(String password, byte[] salt, int iterations) throws GeneralSecurityException {

    // get raw key from password and salt
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, Iterations, SaltBitSize);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    SecretKeySpec secretKey = new SecretKeySpec(keyFactory.generateSecret(pbeKeySpec).getEncoded(), "HMACSHA256");
    byte[] rawKeyData = secretKey.getEncoded();

    return rawKeyData;
}
*/

}
