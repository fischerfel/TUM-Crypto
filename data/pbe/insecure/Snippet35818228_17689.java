static final int ITERATION_COUNT = 10_000;
static final byte[] SALT = {37, -19, . . . 88, 0, 127, 3, 82};
static final PBEParameterSpec PBE_PARAM_SPEC = 
    new PBEParameterSpec(SALT, ITERATION_COUNT);
static final String ALGORITHM = "PBEwithSHAand128bitAES-CBC-BC";

char[] passwordChars = “SECRET!”.toCharArray();

PBEKeySpec pbeKeySpec = new PBEKeySpec(passwordChars);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
Cipher encryptingCipher = Cipher.getInstance(ALGORITHM);
encryptingCipher.init(Cipher.ENCRYPT_MODE, pbeKey, PBE_PARAM_SPEC);
Cipher decryptingCipher = Cipher.getInstance(ALGORITHM);
decryptingCipher.init(Cipher.DECRYPT_MODE, pbeKey, PBE_PARAM_SPEC);

byte[] plainBytesIn = . . . the message to be encrypted . . .

byte[] cipherBytes = encryptingCipher.doFinal(plainBytesIn);

byte[] plainBytesOut = decryptingCipher.doFinal(cipherBytes);
