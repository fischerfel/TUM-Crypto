public static void main( String[] args ) throws Exception {
    String password = "a lovely password of any length";

    // source file encoding should be UTF-8 to support this literal
    String plain = "á pláïn téxt wîth non-ascii characters";
    System.out.println( plain );

    // proper cipher modes need iv
    byte[] ivBytes = new byte[ 8 ];
    SecureRandom.getInstance( "SHA1PRNG" ).nextBytes( ivBytes );

    String encrypted = encryptString( plain, password, ivBytes );
    System.out.println( encrypted );

    String decrypted = decryptString( encrypted, password, ivBytes );
    System.out.println( decrypted );
}

public static String encryptString( String plainText, String password, byte[] ivBytes ) throws Exception {
    MessageDigest sha1 = MessageDigest.getInstance( "SHA1" );
    byte[] keyBytes = Arrays.copyOf( sha1.digest( password.getBytes( "UTF-8" ) ), 24 );
    SecretKeySpec key = new SecretKeySpec( keyBytes, "DESede" );

    IvParameterSpec iv = new IvParameterSpec( ivBytes );

    Cipher cipher = Cipher.getInstance( "DESede/CTR/PKCS5Padding" );
    cipher.init( Cipher.ENCRYPT_MODE, key, iv );

    byte[] plainBytes = plainText.getBytes( "UTF-8" );
    byte[] encryptedBytes = cipher.doFinal( plainBytes );
    String encryptedBase64 = new BASE64Encoder().encode( encryptedBytes );
    String urlEncodedEncryptedBase64 = URLEncoder.encode( encryptedBase64, "UTF-8" );

    return urlEncodedEncryptedBase64;
}

public static String decryptString( String urlEncodedEncryptedBase64, String password, byte[] ivBytes ) throws Exception {
    MessageDigest sha1 = MessageDigest.getInstance( "SHA1" );
    byte[] keyBytes = Arrays.copyOf( sha1.digest( password.getBytes( "UTF-8" ) ), 24 );
    SecretKeySpec key = new SecretKeySpec( keyBytes, "DESede" );

    IvParameterSpec iv = new IvParameterSpec( ivBytes );

    Cipher cipher = Cipher.getInstance( "DESede/CTR/PKCS5Padding" );
    cipher.init( Cipher.DECRYPT_MODE, key, iv );

    // exact mirror of encryption sequence
    String encryptedBase64 = URLDecoder.decode( urlEncodedEncryptedBase64, "UTF-8" );
    byte[] encryptedBytes = new BASE64Decoder().decodeBuffer( encryptedBase64 );
    byte[] decryptedBytes = cipher.doFinal( encryptedBytes );
    String decryptedText = new String( decryptedBytes, "UTF-8" );

    return decryptedText;
}
