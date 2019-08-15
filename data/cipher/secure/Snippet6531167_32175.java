String plainText = "24124124123";
String pwd = "BobsPublicPassword";
byte[] key = pwd.getBytes(); 
key = cutArray(key, 16);
byte[] input = plainText.getBytes();
byte[] output = null;
SecretKeySpec keySpec = null;
keySpec = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
output = cipher.doFinal(input);

private static byte[] cutArray(byte[] arr, int length){
byte[] resultArr = new byte[length];
for(int i = 0; i < length; i++ ){
    resultArr[i] = arr[i];
}
return resultArr;
}
