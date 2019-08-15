// Create PBE parameter set
pbeParamSpec = new PBEParameterSpec(SALT, COUNT);
pbeKeySpec = new PBEKeySpec(hashedReadPasswords[index].toCharArray());
keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");

SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

// Create PBE Cipher
Cipher pbeCipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

// Get bytes of encrypted file
FileInputStream inputStream = new FileInputStream(encryptedFile);
byte[] inputBytes = new byte[(int)encryptedFile.length()];
inputStream.read(inputBytes);

// Get IV
byte[] iv = new byte[16];
for(int i=0; i<16; i++)
{
  iv[i] = inputBytes[i];
}

// Get encrypted data
byte[] cipherBytes = new byte[inputBytes.length-16];
int y=0;
for(int i=16; i<inputBytes.length; i++)
{
  cipherBytes[y] = inputBytes[i];
  y++;
}

// Initialise PBE Cipher with key and parameters
IvParameterSpec ivSpec = new IvParameterSpec(iv);
pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, ivSpec);

// decrypt and get bytes of plain text file
byte[] outputBytes = pbeCipher.doFinal(cipherBytes);

File outputFile = new File("decrypted_BS13");
FileOutputStream outputStream = new FileOutputStream(outputFile);

// store the rest of the decrypted file
outputStream.write(outputBytes);
inputStream.close();
outputStream.close();

return outputFile;
