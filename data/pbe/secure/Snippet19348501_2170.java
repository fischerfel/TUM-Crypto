private static final int HASH_BYTE_SIZE = 64; // 512 bits
private static final int PBKDF2_ITERATIONS = 1000;      

// generate random salt
SecureRandom random = new SecureRandom();
byte salt[] = new byte[SALT_BYTE_SIZE]; // use salt size at least as long as hash
random.nextBytes(salt);

// generate Hash
PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // we would like this to be "PBKDF2WithHmacSHA512" instead? What Provider implements it?
byte[] hash = skf.generateSecret(spec).getEncoded();

// convert hash and salt to hex and store in DB as CHAR(64)...
