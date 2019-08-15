Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

// create IV array of the correct size
final byte[] ivData = new byte[enCipher.getBlockSize()];
// create (or retrieve) a cryptographic secure random implementation (auto-seeded)
final SecureRandom rng = new SecureRandom();
// fill the IV array with random data
rng.nextBytes(ivData);
// generate the ParameterSpec (to create a general parameter for Cipher.init())
IvParameterSpec iv = new IvParameterSpec(ivData);
// and initialize with the new IV
enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
