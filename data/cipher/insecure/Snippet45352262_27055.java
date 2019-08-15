    Cipher cipher = Cipher.getInstance( "DESede/ECB/PKCS5Padding" );
    cipher.init( Cipher.ENCRYPT_MODE, encryptionKey );
    byte[] returnValue = cipher.doFinal( dataToEncrypt )
