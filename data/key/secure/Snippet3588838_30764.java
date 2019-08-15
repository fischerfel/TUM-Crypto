private String decrypt(String token)
{       
//parse token into its IV and token components
byte[] ivAndToken = Base64.decodeBase64(token);
byte[] iv = new byte[ivLength];
System.arraycopy(ivAndToken, 0, iv, 0, ivLength);

int length = ivAndToken.length - ivLength;
byte[] tokenBytes = new byte[length];
System.arraycopy(ivAndToken, ivLength, tokenBytes, 0, length);

//prepare initialization vector specification
IvParameterSpec spec = new IvParameterSpec(iv);

//create cipher instance based on transformer params
Cipher cipher = Cipher.getInstance(algorithm + mode + padding, CRYPTO_PROVIDER);

//convert key bytes into valid key format
Key key = new SecretKeySpec(Base64.decodeBase64(symkey), algorithm);

//initialize cipher for decryption
cipher.init(Cipher.DECRYPT_MODE, key, spec);

//decrypt the payload
String plaintext = new String(cipher.doFinal(tokenBytes));

return plaintext;

} 
