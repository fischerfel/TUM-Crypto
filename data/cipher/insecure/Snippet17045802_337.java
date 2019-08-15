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

public static String verschluesselnZuBase64String(String daten) throws Exception{
    String b64Daten;
    byte[] datenArray = verschlüsseln(daten);
    new Base64(true);
    b64Daten = Base64.encodeBase64String(datenArray);
    return b64Daten;
}

public static String bytesToHex(byte[] bytes) {
    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    char[] hexChars = new char[bytes.length * 2];
    int v;
    for ( int j = 0; j < bytes.length; j++ ) {
        v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}

public static byte[] verschlüsseln(String daten) throws Exception {
    // Benötigt: daten, DreifachDES.password, DreifachDES.macString
    // Ändert: saltString
    // Ausführt: Verschlüsselt "daten," 3DES mit Salt und ein MAC wird
    // benutzt.
    // hash(DreifachDES.password + salt) ist der Schlüssel.
    // Der Output ist ein byte[]

    // Erzeugen Digest für Passwort + Salt
    password="pw";
            macstring="mac";
    final MessageDigest md = MessageDigest.getInstance("SHA1");

    // Erzeugen zufällig 24 Byte Salt
    Random züfallig = new SecureRandom();
    byte[] salt = new byte[24];
    züfallig.nextBytes(salt);

    ByteArrayOutputStream pwsalt = new ByteArrayOutputStream();
    pwsalt.write(password.getBytes("UTF-8"));
    pwsalt.write(salt);
    byte[] unhashedBytes = pwsalt.toByteArray();

    //Hash the pw+salt
    byte[] digestVonPassword = md.digest(unhashedBytes);

    //SHA1 only generates 20 bytes and we need more, so concatenate the salt onto the end.
    byte[] digestVonPassword2 = new byte[digestVonPassword.length + salt.length];
    System.arraycopy(digestVonPassword, 0, digestVonPassword2, 0, digestVonPassword.length);
    System.arraycopy(salt, 0, digestVonPassword2, digestVonPassword.length, salt.length);

    // Erzeugen die MAC (Message Authentication Code, Mesage
    // Authentifizierung Chiffre)
    // Später mache ich einmal ein zufällig String, und wir benutzen das
    // immer.
    SecretKeySpec macSpec = new SecretKeySpec(
            digestVonPassword, "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(macSpec);
    byte[] macBytes = mac.doFinal(macString.getBytes());

    // Wir brauchen nur 24 Bytes, benutze die Erste 24 von der Digest
    final byte[] keyBytes = Arrays.copyOf(digestVonPassword2, 24);

    // Erzeugen eine züfallig IV
    byte[] ivSeed = new byte[8];
    züfallig.nextBytes(ivSeed);
    final IvParameterSpec iv = new IvParameterSpec(ivSeed);

    // Erzeugen der Schlüssel
    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");

    // Erzeugen Cipher mit 3DES, CBC und PKCS5Padding
    final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    // Erzeugen byte[] von String message
    final byte[] plainTextBytes = daten.getBytes("UTF-8");
    byte[] vorIvCipherText = cipher.doFinal(plainTextBytes);

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
