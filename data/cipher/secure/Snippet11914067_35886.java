Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");

// repeat this for each cipher text
byte[] ivBytes = new byte[aes.getBlockSize()];
SecureRandom rnd = new SecureRandom();
rnd.nextBytes(ivBytes);
aes.init(Cipher.ENCRYPT_MODE, sk, new IvParameterSpec(ivBytes));

// now prepend the ivBytes to the output, e.g. by writing it to a stream first
// remove and use as IV at the receiving side
