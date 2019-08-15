KeyGenerator keygen = KeyGenerator.getInstance("AES");
SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
byte[] iv = sr.generateSeed(16);
IvParameterSpec ivSpec = new IvParameterSpec(iv);
SecretKey aesKey = keygen.generateKey();

//save byte array in text file to recreate key later
byte[] encodedKey = aesKey.getEncoded();
new File("myPath\\AESKey.txt");
FileOutputStream fos = new FileOutputStream("myPath\\AESKey.txt");
//save AesKey in first 16 bytes and Initial Vector in next 16 bytes
fos.write(encodedKey);
fos.write(iv);
fos.close();

String secretText = "Hello cryptography";      
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
byte[] encrypted = cipher.doFinal(secretText.getBytes());
BASE64Encoder myEncoder  = new BASE64Encoder();
String encodedSecretText = myEncoder.encode(encrypted);

new File("myPath\\encodedSecretText.txt");
FileOutputStream fos2 = new FileOutputStream("myPath\\encodedSecretText.txt");
fos2.write(encodedSecretText.getBytes());  
fos2.close();
