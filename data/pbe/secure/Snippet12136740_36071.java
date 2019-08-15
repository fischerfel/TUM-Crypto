// Note that we are not reading the image in here...
System.out.print("Decryption Password: ");
System.out.flush();
PBEKeySpec pbeKeySpec = new PBEKeySpec(scanner.next().toCharArray());
// Set up other parameters to be used by the password-based
// encryption.
PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
SecretKeyFactory keyFac = SecretKeyFactory
        .getInstance("PBEWithMD5AndDES");
SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
// Make a PBE Cyper object and initialize it to decrypt using
// the given password.
Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

// We're now going to read the image in, using the cipher
// input stream, which wraps a file input stream
File inputFile = new File("sheepTest.png");
FileInputStream fis = new FileInputStream(inputFile);
CipherInputStream cis = new CipherInputStream(fis, pbeCipher);
// We then use all that to read the image
BufferedImage input = ImageIO.read(cis);
cis.close();

// We then write the dcrypted image out...
// Decrypt the ciphertext and then print it out.
FileOutputStream output = new FileOutputStream("sheepTest.png");
ImageIO.write(input, "PNG", output);
