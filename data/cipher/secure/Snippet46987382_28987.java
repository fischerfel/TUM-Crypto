// The plaintext to encrypt, and the password we want to use.
String plaintext = "Hello, World!";
String password = "WizardsAreCool";

// Parameters for PBKDF2/AES.  Using a higher iteration count 
// is better in production.
int iterationCount = 25000;
int keySize = 128;
int tagSize = 128;
int saltSize = 16;
int nonceSize = 12;

// We generate a random salt for PBKDF2.
SecureRandom rng = new SecureRandom();
byte[] salt = new byte[saltSize];
rng.nextBytes(salt);

// We derive a 128-bit key using PBKDF2 from the password,
// as AES-128 expects a 128-bit key.  We also use SHA256 instead 
// of SHA1 for the underlying hash.
PBEKeySpec pwSpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keySize);
SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
byte[] key = keyFac.generateSecret(pwSpec).getEncoded();

// We convert the plaintext to binary and generate a 12-byte nonce for 
// GCM mode.
byte[] rawData = plaintext.getBytes(StandardCharsets.UTF_8);
byte[] nonce = new byte[nonceSize];
rng.nextBytes(nonce);

// We define the cipher.
Cipher aesGcm = Cipher.getInstance("AES/GCM/NoPadding");
GCMParameterSpec gcmSpec = new GCMParameterSpec(tagSize, nonce);
aesGcm.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), gcmSpec);

// We get the resulting ciphertext.
byte[] encResult = aesGcm.doFinal(rawData);

// We produce the final result by prepending the PBKDF2 salt and 
// the nonce.
byte[] result = new byte[saltSize + nonceSize + encResult.length];
System.arraycopy(salt, 0, result, 0, saltSize);
System.arraycopy(nonce, 0, result, saltSize, nonceSize);
System.arraycopy(encResult, 0, result, saltSize + nonceSize, encResult.length);

// Print the result as base64.
byte[] b64Result = Base64.getEncoder().encode(result);
System.out.println(new String(b64Result));

// Sample Output
// C100zs91Ku/TbQw4Mgw7e95didsA1Vj5oHGeMitohnRaUGIB08+T6uESro4P2Gf7q/7moMbWTTNT
