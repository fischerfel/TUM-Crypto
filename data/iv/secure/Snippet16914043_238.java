import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;


public class test {

/**
 * @param args
 */
public static void main(String[] args) {
    test te = new test();
    try {
        te.decryptSeedValue();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
    // TODO Auto-generated method stub

}

public static HashMap decryptSeedValue()throws Exception{



    String password = "G?20R+I+3-/UcWIN";



    String pbesalt ="EW0h0yUcDX72WU9UiKiCwDpXsJg=";
    String iv = "aaaaaaaaaaaaaaaaaaaaaaaa";

    int iteration = 128;




    String value = "pM7VB/KomPjq2cKaxPr5cKT1tUZN5tGMI+u1XKJTG1la+ThraPpLKlL2plKk6vQE";
    String valueDigest = "lbu+9OcLArnj6mS7KYOKDa4zRU0=";




    byte[] cipherText =null;


    //some parameters need to decode from Base64 to byte[]
    byte[] data = base64Decode(value.getBytes());
    //System.out.println("data(hex string) = " + HexBin.encode(data));//debug

    byte[] salt = base64Decode(pbesalt.getBytes());
    //System.out.println("salt(hex string) = " + HexBin.encode(salt));//debug

    byte[] initVec = base64Decode(iv.getBytes());
    //System.out.println("iv(hex string) = " + HexBin.encode(initVec));//debug

    //perform PBE key generation and AES/CBC/PKCS5Padding decrpyption

    HashMap hs = myFunction(data, password, initVec, salt, iteration); 

    String seedValue = (String)hs.get("DECRYPTED_SEED_VALUE");
    byte[] temp = (byte[])hs.get("HASH_OUTPUT");


    //System.out.println("hashed output(hex string) = " + HexBin.encode(temp));//debug

    //perform Base64 Encode 
    byte[] out = base64Encode(temp);

    String output = new String((out));
    System.out.println("output = "+output);
    System.out.println("valueD = "+valueDigest);
    //System.out.println("hashed output(base64) = " + output);

    //compare the result
    if(output.equals(valueDigest)){
        System.out.println("Hash verification successful for:-->"  );
          System.out.println("\n");

        //hs.put("SEED_VALUE", HexBin.encode(temp));
        hs.put("SEED_VALUE", seedValue);
        return hs;

    }
    else{
        System.out.println("Hash verification failed  for :-->");

        return null;

    }



}

public static HashMap myFunction(byte[] data, String password, byte[] initVec,
                              byte[] salt, int iteration) throws Exception{

    PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();

    byte[] pBytes = password.getBytes();

    generator.init(pBytes, salt, iteration);

    int keysize = 128;//fixed at AES key of 16 bytes
    int ivsize = initVec.length;

    ParametersWithIV params = (ParametersWithIV) generator.generateDerivedParameters(keysize, ivsize);

    KeyParameter keyParam = (KeyParameter) params.getParameters();

    //System.out.println("derived key = " + HexBin.encode(keyParam.getKey()));

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    IvParameterSpec paramSpec = new IvParameterSpec(initVec);

    SecretKeySpec key = new SecretKeySpec(keyParam.getKey(), "AES");

    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    //perform decryption
    byte[] secret = cipher.doFinal(data);

    //display the 20 bytes secret of the token 
    //System.out.println("token secret(hex string) = " + HexBin.encode(secret));

    //perform HMAC-SHA-1
    byte[] output = hmac_sha1(secret, keyParam.getKey());

    HashMap hs = new HashMap();

    hs.put("ENCRYPTION_KEY", HexBin.encode(keyParam.getKey()));
    hs.put("HASH_OUTPUT", output);

    hs.put("DECRYPTED_SEED_VALUE", HexBin.encode(secret));

    return hs;
}

public static byte[] base64Encode(byte[] passwordBytes) throws NoSuchAlgorithmException {
    Base64 base64 = new Base64();
    byte[] hashBytes2 = base64.encode(passwordBytes);
    return hashBytes2;
}

public static byte[] base64Decode(byte[] passwordBytes) throws NoSuchAlgorithmException {
    Base64 base64 = new Base64();
    byte[] hashBytes2 = base64.decode(passwordBytes);
    return hashBytes2;
}

public static byte[] hmac_sha1(byte[] dataByte, byte[] keyByte) throws Exception{
    Mac hmacSha1;
    hmacSha1 = Mac.getInstance("HmacSHA1");

    SecretKeySpec macKey = new SecretKeySpec(keyByte, "HmacSHA1");
    hmacSha1.init(macKey);
    byte[] result = hmacSha1.doFinal(dataByte);
    return result;

}
/**
 * Convert a byte array of 8 bit characters into a String.
 * 
 * @param bytes the array containing the characters
 * @param length the number of bytes to process
 * @return a String representation of bytes
 */
private static String toString(
    byte[] bytes,
    int    length)
{
    char[]  chars = new char[length];

    for (int i = 0; i != chars.length; i++)
    {
        chars[i] = (char)(bytes[i] & 0xff);
    }

    return new String(chars);
}

} 
