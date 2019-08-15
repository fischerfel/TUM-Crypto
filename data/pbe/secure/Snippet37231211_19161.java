// Create PBE parameter set
pbeParamSpec = new PBEParameterSpec(SALT, COUNT);
pbeKeySpec = new PBEKeySpec(get_SHA_1_SecurePassword(password, SALT).toCharArray());
keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");

SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
// PRINT HASH OF PW  
System.out.println(get_SHA_1_SecurePassword(password, SALT).toCharArray());

// Create PBE Cipher and initialise PBE Cipher with key and parameters
Cipher pbeCipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

// Get bytes of input file
FileInputStream inputStream = new FileInputStream(inputFile);
byte[] inputBytes = new byte[(int) inputFile.length()];
inputStream.read(inputBytes);

// Encrypt and get bytes of encrypted file
byte[] iv = pbeCipher.getIV();
byte[] outputBytes = pbeCipher.doFinal(inputBytes);

String fileName = inputFile.getName();
File outputFile = new File(fileName + ".enc");
FileOutputStream outputStream = new FileOutputStream(outputFile);

// store first 16 bytes in the file as the IV
outputStream.write(iv);

// store the rest of the encrypted file
outputStream.write(outputBytes);
inputStream.close();
outputStream.close();

return outputFile;
