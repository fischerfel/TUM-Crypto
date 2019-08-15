public static byte[] decryptAES(String message) throws Exception 
{  
String secretKey = "JohnIsAwesome!1!";
SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key);
return cipher.doFinal(message.getBytes());  
}
