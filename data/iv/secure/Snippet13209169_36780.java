// lets use the actual key value instead of the platform specific character decoding
byte[] secret = Hex.decodeHex("25d6c7fe35b9979a161f2136cd13b0ff".toCharArray());

// that's fine
SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");

// SecureRandom should either be slow or be implemented in hardware
SecureRandom random = new SecureRandom();

// first create the cipher
Cipher eCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

// filled with 00h characters first, use Cipher instance so you can switch algorithms
byte[] realIV = new byte[eCipher.getBlockSize()];

// actually fill with random
random.nextBytes(realIV);

// MISSING: create IvParameterSpec
IvParameterSpec ivSpec = new IvParameterSpec(realIV);

// create the cipher using the IV
eCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

// NOTE: you should really not encrypt passwords for verification
String stringToEncrypt = "mypassword";

// convert to bytes first, but don't use the platform encoding
byte[] dataToEncrypt = stringToEncrypt.getBytes(Charset.forName("UTF-8"));

// actually do the encryption using the data
byte[] encryptedData = eCipher.doFinal(dataToEncrypt);
