import java.util.Arrays;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import verschlüsseln.FalscheMACOderSaltException;


    public static byte[] verschlüsseln(String daten) throws Exception {
        // Benötigt: daten, DreifachDES.password, DreifachDES.macString
        // Ändert: saltString
        // Ausführt: Verschlüsselt "daten," 3DES mit Salt und ein MAC wird
        // benutzt.
        // hash(DreifachDES.password + salt) ist der Schlüssel.
        // Der Output ist ein byte[]

        // Erzeugen Digest für Passwort + Salt
        password="key12345key54321key15243";
        final MessageDigest md = MessageDigest.getInstance("SHA1");

        // Erzeugen zufällig 24 Byte Salt
        Random züfallig = new SecureRandom();
        byte[] salt = new byte[24];
        züfallig.nextBytes(salt);
        String saltString = Arrays.toString(salt);

        // Digest Passwort + Salt um der Schlüssel zu erzeugen
        final byte[] digestVonPassword = md.digest((password + saltString)
                .getBytes("UTF-8"));
        new Base64(true);
        String b64Daten = Base64.encodeBase64String(digestVonPassword);

        // Wir brauchen nur 24 Bytes, benutze die Erste 24 von der Digest
        final byte[] keyBytes = Arrays.copyOf(digestVonPassword, 24);

        // Erzeugen der Schlüssel
        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");

        // Erzeugen eine züfallig IV
        byte[] ivSeed = new byte[8];
        züfallig.nextBytes(ivSeed);
        final IvParameterSpec iv = new IvParameterSpec(ivSeed);

        // Erzeugen Cipher mit 3DES, CBC und PKCS5Padding
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        // Erzeugen byte[] von String message
        final byte[] plainTextBytes = daten.getBytes("UTF-8");
        byte[] vorIvCipherText = cipher.doFinal(plainTextBytes);

        // Erzeugen die MAC (Message Authentication Code, Mesage
        // Authentifizierung Chiffre)
        // Später mache ich einmal ein zufällig String, und wir benutzen das
        // immer.
        SecretKeySpec macSpec = new SecretKeySpec(
                (password + saltString).getBytes("UTF-8"), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(macSpec);
        byte[] macBytes = mac.doFinal(macString.getBytes());

        // Erzeugen byte outputStream um die Arrays zu verbinden
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();

        // Verbinden IV, Salt, MAC, und verschlüsselt String
        ostream.write(cipher.getIV());
        ostream.write(salt);
        ostream.write(macBytes);
        ostream.write(vorIvCipherText);

        final byte[] cipherText = ostream.toByteArray();

        return cipherText;
    }
