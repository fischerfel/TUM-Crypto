import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import util.HexString;

public class Util {
public static void main(String ...x) throws NoSuchAlgorithmException,         
NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
String keydata=  "5E8B6E1998F421204C6576544FE1A26B44FC775982D8CE2E";

String inputData = "13E37073120A47D119E82545CAAF1505E3E94E5E7D8B52F3";

byte[] keyByte = new BigInteger(keydata,16).toByteArray();

SecretKeySpec key=new SecretKeySpec(keyByte, "DESede");    

Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");

c.init(Cipher.DECRYPT_MODE, key);

byte[] output = c.doFinal(inputData.getBytes());

String hexStr = HexString.bytesToHexString(output);
System.out.println("Decoded value : "+hexStr);

} }
