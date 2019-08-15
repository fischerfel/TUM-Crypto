public static String encrypt(int saltLength, byte[] seed, String textToBeEncrypted) throws Exception {

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
Mac hmac = Mac.getInstance("HmacSHA256");

// Get the salt
SecureRandom random = new SecureRandom();
byte[] salt = new byte[saltLength];
random.nextBytes(salt);

// Secret key
SecretKey secretKey = getSecretKey(seed, salt);

// Initialization vector, as CBC requires IV
byte[] iv = new byte[cipher.getBlockSize()];
random.nextBytes(iv);

// Algorithm spec for IV
IvParameterSpec ivParams = new IvParameterSpec(iv);

// Get Cipher instance for AES with Padding algorithm PKCS5
cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
hmac.init(secretKey);

ByteArrayOutputStream blob = new ByteArrayOutputStream();
MacOutputStream macStream = new MacOutputStream(blob, hmac);
DataOutputStream dataBlob = new DataOutputStream(macStream);

dataBlob.writeShort(saltLength);
dataBlob.write(salt);
dataBlob.write(iv.length);
dataBlob.write(iv);

// Encrypt the text
byte[] encryptedTextInBytes = cipher.doFinal(textToBeEncrypted
        .getBytes(StandardCharsets.UTF_8));

dataBlob.writeInt(encryptedTextInBytes.length);
dataBlob.write(encryptedTextInBytes);

dataBlob.writeShort(hmac.getMacLength());
dataBlob.write(macStream.getMac());

return Base64Encoder encode(blob.toByteArray());
