public static void encrypt() throws Exception {
    // Add the BouncyCastle Provider
    //Security.addProvider(new BouncyCastleProvider());


// Generate the key
byte[] keyBytes = "AAAAAAAAAAAAAAAA".getBytes();
SecretKeySpec   key = new SecretKeySpec(keyBytes, "AES");

// Generate the IV
byte[] ivBytes  = "AAAAAAAAAAAAAAAA".getBytes();
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

// Create the cipher object and initialize it
Cipher          cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

// Read all bytes from a file into a bytes array
byte[] inputBytes = GCM.readFile("input");
byte[] cipherBytes = cipher.doFinal(inputBytes);

BufferedOutputStream  outputStream = new BufferedOutputStream(new FileOutputStream("output.enc"));
outputStream.write(cipherBytes);

outputStream.close();   
}

public static void decrypt() throws Exception {
    // Add the BouncyCastle Provider
     //Security.addProvider(new BouncyCastleProvider());

 // Generate the key
 byte[] keyBytes = "AAAAAAAAAAAAAAAA".getBytes();
 SecretKeySpec   key = new SecretKeySpec(keyBytes, "AES");

 // Generate the IV
 byte[] ivBytes  = "AAAAAAAAAAAAAAAA".getBytes();
 IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

 // Create the cipher object and initialize it
 Cipher          cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
 cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

 // Read all bytes from a file into a bytes array
 byte[] cipherBytes = GCM.readFile("ouput.enc");
 byte[] decBytes = cipher.doFinal(cipherBytes);

 BufferedOutputStream  outputStream = new BufferedOutputStream(new FileOutputStream("regen.plain"));
 outputStream.write(decBytes);
 outputStream.close();   
}
