import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;

public class PasswordEncryption {

private static int ITERATIONS = 1000;
private static String saltString;
private static String ciphertextString;
private static String encryptPhrase;

private static void usage(){
    System.err.println("Usage: java PBE -e|-d password text");
    System.exit(1);
}

public static void main(String[] args) throws Exception{

    scan = new Scanner(System.in);

    System.out.println("Please enter plain text: ");
    String text = scan.nextLine();
    System.out.println("Please enter password to encrypt plain text: ");

    char [] pw = scan.nextLine().toCharArray();
    int option=3;

    while (option!=0){
    System.out.println("Are we encrypting(1) or decrypting(2) or Exit(0)? " );
    option = scan.nextInt();
    String output="exiting program";

    if (option == 1)
        output = encrypt(pw, text);
    else if (option ==2)
        output = decrypt(pw,text);
    System.out.println("Output: " +output);
    }

}
private static Scanner scan;

private static String encrypt(char[] password, String plaintext) throws Exception {

    //create random salt
    byte[]  salt = new byte[8];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);

    //create key based on password
    int iterationCount = ITERATIONS;
    PBEKeySpec pbeSpec = new PBEKeySpec(password, salt, iterationCount);
    SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES");

    //create a cipher
    Cipher myCipher = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES");

    Key encryptKey = keyFact.generateSecret(pbeSpec);
    myCipher.init(Cipher.ENCRYPT_MODE, encryptKey);

    byte[] cipherText = myCipher.doFinal();
    System.out.println("Encrypted Text: " +toString(cipherText));

    //produce salt to string
    saltString = salt.toString();
    System.out.println("SALT: " +saltString);

    //produce cipher text to string
    ciphertextString = toString(cipherText);

    //stores salt and cipher string in encryptPhrase
    encryptPhrase = saltString+ciphertextString;

    return saltString+ciphertextString;
}

public static String decrypt(char[] password, String encryptPhrase) throws Exception{
    //split the encryption data into salt and ciphertext
    //System.out.println("encrypt Phrase: " +encryptPhrase);


    //convert salt into bytearray
    byte[] bsalt = toByteArray(saltString);
    //convert ciphertext into bytearray
    byte[] bciphert= toByteArray(ciphertextString);

    //produce cipher

    /////////////////////////////////////////////////////////////////////       
    int iterationCount = ITERATIONS;
    PBEKeySpec pbeSpec = new PBEKeySpec(password, bsalt, iterationCount);
    //use SHA and 3DES

    //create the key
    SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES");
    Cipher cDec = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES");
    Key sKey = keyFact.generateSecret(pbeSpec);

    //perform decryption
    cDec.init(cDec.DECRYPT_MODE,sKey);

    byte [] plainTextb = cDec.doFinal(bciphert); //gives me an error here. Says BadPaddingException: pad block corrupted?

    String plainText = toString(plainTextb); 

    return plainText;
    //return encryptPhrase;

}

/**
 * Convert a byte array of 8 bit characters into a String.
 * 
 *@param bytes the array containing the characters
 * @param length the number of bytes to process
 * @return a String representation of bytes
 */
public static String toString(byte[] bytes, int length)
{
    char[]  chars = new char[length];

    for (int i = 0; i != chars.length; i++)
    {
        chars[i] = (char)(bytes[i] & 0xff);
    }

    return new String(chars);
}

/**
 * Convert a byte array of 8 bit characters into a String.
 * 
 * @param bytes the array containing the characters
 * @return a String representation of bytes
 */
public static String toString( byte[]   bytes)
{
    return toString(bytes, bytes.length);
}

/**
 * Convert the passed in String to a byte array by
 * taking the bottom 8 bits of each character it contains.
 * 
 * @param string the string to be converted
 * @return a byte array representation
 */
public static byte[] toByteArray(String string)
{
    byte[]  bytes = new byte[string.length()];
    char[]  chars = string.toCharArray();

    for (int i = 0; i != chars.length; i++)
    {
        bytes[i] = (byte)chars[i];
    }

    return bytes;
}
private static String digits = "0123456789abcdef";

public static String toHex(byte[] data, int length)
{
    StringBuffer buf = new StringBuffer();

    for (int i=0; i!= length; i++)
    {
        int v = data[i] & 0xff;

        buf.append(digits.charAt(v >>4));
        buf.append(digits.charAt(v & 0xf));
    }
    return buf.toString();

}

/**
 * Return the passed in byte array as a hex string.
 * 
 * @param data the bytes to be converted.
 * @return a hex representation of data.
 */

public static String toHex(byte[] data)
{
    return toHex(data, data.length);
}
}
