// SecureRandom is expensive to initialize (takes several milliseconds) –
// consider keeping the instance around if you are generating many keys.
SecureRandom random = new SecureRandom();
byte[] keyBytes = new byte[16];
random.nextBytes(keyBytes);
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
