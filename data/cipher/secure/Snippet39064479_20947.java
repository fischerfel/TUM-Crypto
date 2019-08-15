// password to encrypt the file
String password = "p@sswordDataNoW";

// salt is used for encoding
byte[] salt = new byte[8];
SecureRandom secureRandom = new SecureRandom();
secureRandom.nextBytes(salt);
FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
saltOutFile.write(salt);
saltOutFile.close();

SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
                128);
SecretKey secretKey = factory.generateSecret(keySpec);
SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

//padding AES encryption
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();

// iv adds randomness to the text and just makes the mechanism more
// secure
// used while initializing the cipher
// file to store the iv
FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
ivOutFile.write(iv);
ivOutFile.close();

//file encryption
byte[] input = new byte[64];
int bytesRead;

while ((bytesRead = inFile.read(input)) != -1) {
        byte[] output = cipher.update(input, 0, bytesRead);
        if (output != null)
                outFile.write(output);
}

byte[] output = cipher.doFinal();
if (output != null)
        outFile.write(output);

inFile.close();
outFile.flush();
outFile.close();
