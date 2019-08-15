  public String encrypt( String stringToEncrypt, IvParameterSpec ivSpec ) {
    if ( stringToEncrypt == null ) {
      return null;
    }
    try {
      Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec( key, "AES" );
      cipher.init( Cipher.ENCRYPT_MODE, keySpec, ivSpec );
      byte[] data = cipher.doFinal( stringToEncrypt.getBytes( "UTF-8" ) );
      return String.format( "%s:%s", Base64.encode( ivSpec.getIV() ), Base64.encode( data ) );
    } catch ( Exception e ) {
      throw new RuntimeException( "Unable to encrypt the string", e );
    }
  }
