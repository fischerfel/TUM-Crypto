// --- create cipher
final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

// --- generate new AES key
KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
aesKeyGen.init(256);
SecretKey aesKey = aesKeyGen.generateKey();

// --- generate IV and GCM parameters
SecureRandom random = new SecureRandom();
byte[] initVector   = new byte[96 / Byte.SIZE];
random.nextBytes(initVector);
GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initVector);
cipher.init(Cipher.ENCRYPT_MODE, aesKey,
        gcmParameterSpec);

// --- process any AAD (just a bunch of zero bytes in this example)
byte[] aad = new byte[128];
cipher.updateAAD(aad);

// --- process any data (just a bunch of zero bytes in this example)
byte[] data         = new byte[128];
// use cipher itself to create the right buffer size
byte[] encrypted    = new byte[cipher.getOutputSize(data.length)];
int updateSize = cipher.update(data, 0, data.length, encrypted, 0);
cipher.doFinal(encrypted, updateSize);
