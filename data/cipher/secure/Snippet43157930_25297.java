FileInputStream fileInputStream = new FileInputStream(mypath);
InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
StringBuffer stringBuffer = new StringBuffer();
while ((fileContents = bufferedReader.readLine()) != null) {
    stringBuffer.append(fileContents + "\n");
}
String fileContentsString = stringBuffer.toString();
String[] fileContentsList = fileContentsString.split("]");
byte[] cipherText = fileContentsList[0].getBytes();
Toast.makeText(getApplicationContext(), fileContentsList[0], Toast.LENGTH_LONG).show();
byte[] iv = fileContentsList[1].getBytes();
byte[] salt = fileContentsList[2].getBytes();
KeySpec keySpec = new PBEKeySpec(submittedPassword.toCharArray(), salt, 1000, 256);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
SecretKey key = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
byte[] plaintext = cipher.doFinal(cipherText);
String plainrStr = new String(plaintext , "UTF-8");
textEdit.setText(plainrStr);
