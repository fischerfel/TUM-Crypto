public static HashMap decryptSeedValue() throws Exception
{

String password = "G?20R+I+3-/UcWIN";


String pbesalt = "EW0h0yUcDX72WU9UiKiCwDpXsJg=";
String iv = "aaaaaaaaaaaaaaaaaaaaaaaa";

int iteration = 128;

String value = "pM7VB/KomPjq2cKaxPr5cKT1tUZN5tGMI+u1XKJTG1la+ThraPpLKlL2plKk6vQE";
String valueDigest = "lbu+9OcLArnj6mS7KYOKDa4zRU0=";

//YOU NEED THIS GUY BELOW TO VERIFY
String macKey = "jq/NdikC7AZf0Z+HEL5NrCICV8XW+ttzl/8687hVGHceoyJAaFws+111plQH6Mlg";

byte[] cipherText = null;

//some parameters need to decode from Base64 to byte[]
byte[] data = base64Decode(value.getBytes());
//System.out.println("data(hex string) = " + HexBin.encode(data));//debug

byte[] salt = base64Decode(pbesalt.getBytes());
//System.out.println("salt(hex string) = " + HexBin.encode(salt));//debug

byte[] initVec = base64Decode(iv.getBytes());
//System.out.println("iv(hex string) = " + HexBin.encode(initVec));//debug

//perform PBE key generation and AES/CBC/PKCS5Padding decrpyption
HashMap hs = myFunction(data, password, base64Decode(macKey.getBytes()), salt, iteration);

String seedValue = (String) hs.get("DECRYPTED_SEED_VALUE");
byte[] temp = (byte[]) hs.get("HASH_OUTPUT");

//System.out.println("hashed output(hex string) = " + HexBin.encode(temp));//debug
//perform Base64 Encode 
byte[] out = base64Encode(temp);

String output = new String((out));
System.out.println("output = " + output);
System.out.println("valueD = " + valueDigest);
//System.out.println("hashed output(base64) = " + output);

//compare the result
if (output.equals(valueDigest)) {
  System.out.println("Hash verification successful for:-->");
  System.out.println("\n");

  //hs.put("SEED_VALUE", HexBin.encode(temp));
  hs.put("SEED_VALUE", seedValue);
  return hs;

} else {
  System.out.println("Hash verification failed  for :-->");

  return null;

}

}
public static HashMap myFunction(byte[] data, String password, byte[] macData,
      byte[] salt, int iteration) throws Exception
{

PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();

byte[] pBytes = password.getBytes();

generator.init(pBytes, salt, iteration);

byte[] iv = new byte[16];
int ivsize = iv.length;
byte[] encryptedData = new byte[data.length - ivsize];
System.arraycopy(data, 0, iv, 0, iv.length);
System.arraycopy(data, ivsize, encryptedData, 0, encryptedData.length);

byte[] maciv = new byte[16];
byte[] encryptedMac = new byte[macData.length - maciv.length];
System.arraycopy(macData, 0, maciv, 0, maciv.length);
System.arraycopy(macData, maciv.length, encryptedMac, 0, encryptedMac.length);

int keysize = 128;//fixed at AES key of 16 bytes

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iteration, keysize);
SecretKey tmp = factory.generateSecret(spec);
SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");
Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

byte[] decryptedData = dcipher.doFinal(encryptedData);
// decryptedData is your token value!

dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(maciv));

byte[] decryptedMac = dcipher.doFinal(encryptedMac);

//display the 20 bytes secret of the token 
//System.out.println("token secret(hex string) = " + HexBin.encode(secret));
//perform HMAC-SHA-1

//Use the decrypted MAC key here for hashing!
byte[] output = hmac_sha1(data, decryptedMac);

HashMap hs = new HashMap();

hs.put("ENCRYPTION_KEY", password);
hs.put("HASH_OUTPUT", output);

hs.put("DECRYPTED_SEED_VALUE", HexBin.encode(decryptedData));

return hs;
}
