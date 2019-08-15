final String KEY_GENERATION_ALG = "d(*Mu96p@lg91¨%0c*f7&d^`pkçly$f7";
    final int HASH_ITERATIONS = 10000;
    final int KEY_LENGTH = 256;

    char[] humanPassphrase = { 'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l', 'u',
            'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
            'n', 't' };
    byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE,
            0xF }; 

    PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt, HASH_ITERATIONS, KEY_LENGTH);
    SecretKeyFactory keyfactory = null;
    SecretKey sk = null;
    SecretKeySpec skforAES = null;

    try {
        keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
        sk = keyfactory.generateSecret(myKeyspec);

    } catch (NoSuchAlgorithmException nsae) {
        Log.e("AESdemo",
                "no key factory support for PBEWITHSHAANDTWOFISH-CBC");

    } catch (InvalidKeySpecException ikse) {
        Log.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC");

    }

    byte[] skAsByteArray = sk.getEncoded();
    skforAES = new SecretKeySpec(skAsByteArray, "AES");

    final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

    byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD,
            91 };
    IvParameterSpec IV = new IvParameterSpec(iv);

    String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, ciphertext));

    return decrypted;
