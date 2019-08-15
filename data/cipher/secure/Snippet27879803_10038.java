ByteArrayOutputStream blob = new ByteArrayOutputStream();
DataOutputStream dataBlob = new DataOutputStream(blob);

// Get the salt
SecureRandom random = new SecureRandom();
byte[] salt = new byte[saltLength];
random.nextBytes(salt);

dataBlob.writeShort(saltLength);
dataBlob.write(salt);

// Secret key
SecretKey secretKey = getSecretKey(seed, salt);

// Get Cipher instance for AES with Padding algorithm PKCS5
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

// Initialization vector, as CBC requires IV
byte[] iv = new byte[cipher.getBlockSize()];
random.nextBytes(iv);

dataBlob.write(iv.length);
dataBlob.write(iv);

// Algorithm spec for IV
IvParameterSpec ivParams = new IvParameterSpec(iv);

cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

// Encrypt the text
byte[] encryptedTextInBytes = cipher.doFinal(textToBeEncrypted
        .getBytes(StandardCharsets.UTF_8));

dataBlob.writeInt(encryptedTextInBytes.length);
dataBlob.write(encryptedTextInBytes);


// out of scope: add HMAC protection over current contents of blob here
// (or while writing it to dataBlob, also update a HMAC) 

// Base64Encoder encode;
return Base64Encoder.encode(blob.toByteArray());
