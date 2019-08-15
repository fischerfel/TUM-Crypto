String keyString = "C0BAE23DF8B51807B3E17D21925FADF273A70181E1D81B8EDE6C76A5C1F1716E";
byte[] keyValue = hexStringToByte(keyString);
Key key = new SecretKeySpec(keyValue, "AES");
Cipher c1 = Cipher.getInstance("AES");
c1.init(Cipher.ENCRYPT_MODE, key);

String data = "Some data to encrypt";
byte[] encVal = c1.doFinal(data.getBytes());
String encryptedValue = Base64.encodeBase64String(encVal);


/* Copied the below code from another post in stackexchange */
public static byte[] hexStringToByte(String hexstr) 
{
  byte[] retVal = new BigInteger(hexstr, 16).toByteArray();
  if (retVal[0] == 0) 
  {
    byte[] newArray = new byte[retVal.length - 1];
    System.arraycopy(retVal, 1, newArray, 0, newArray.length);
    return newArray;
  }
  return retVal;
}
