//example input
String userInput = "this is a user input with bad entropy";

HKDF hkdf = HKDF.fromHmacSha256();

//extract the "raw" data to create output with concentrated entropy
byte[] pseudoRandomKey = hkdf.extract(staticSalt32Byte, userInput.getBytes(StandardCharsets.UTF_8));

//create expanded bytes for e.g. AES secret key and IV
byte[] expandedAesKey = hkdf.expand(pseudoRandomKey, "aes-key".getBytes(StandardCharsets.UTF_8), 16);
byte[] expandedIv = hkdf.expand(pseudoRandomKey, "aes-iv".getBytes(StandardCharsets.UTF_8), 16);

//Example boilerplate encrypting a simple string with created key/iv
SecretKey key = new SecretKeySpec(expandedAesKey, "AES"); //AES-128 key
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(expandedIv));
byte[] encrypted = cipher.doFinal("my secret message".getBytes(StandardCharsets.UTF_8));
