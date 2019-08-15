 public static SecretKey generateKey( String passphraseOrPin ) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final int iterations = 10000;

    // Generate a 256-bit key
    final int outputKeyLength = 256;

    char[] chars = new char[passphraseOrPin.length()];
    passphraseOrPin.getChars( 0, passphraseOrPin.length(), chars, 0 );
    byte[] salt = "thisSaltIsInClient".getBytes();

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );
    KeySpec keySpec = new PBEKeySpec( chars, salt, iterations, outputKeyLength );
    return secretKeyFactory.generateSecret( keySpec );
}
