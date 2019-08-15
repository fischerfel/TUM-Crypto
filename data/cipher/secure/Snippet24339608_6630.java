try{
        String strModulus = "tr82UfeGetV7yBKcOPjFTWs7pHqqr/5YKKWMUZHG4HnCmWrZsOhuR1FBnMZ/g2YiosoSlu0zd7Ukz9lX7wv2RLfWXfMvZYGpAAvfYWwzbyQ2i1q+tKE/thgKNscoSRellDD+uJcYn1H4hnaudVyYJH9miVhOKhKlExMzw8an6U=";
        String strExponent = "AQAB";
        byte[] modulusBytes = DatatypeConverter.parseBase64Binary("tr82UfeGetV7yBKcOPjFTWs7pHqqr/5YKKWMUZ/HG4HnCmWrZsOhuR1FBnMZ/g2YiosoSlu0zd7Ukz9lX7wv2RLfWXfMvZYGpAAvfYWwzbyQ2i1q+tKE/thgKNscoSRellDD+uJcYn1H4hnaudVyYJH9miVhOKhKlExMzw8an6U=");
        byte[] exponentBytes = DatatypeConverter.parseBase64Binary("AQAB");

        BigInteger modulus = new BigInteger(1, modulusBytes );
        BigInteger exponent = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(rsaPubKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] plainBytes = new String("Manchester United").getBytes("UTF-8");
        byte[] cipherData = cipher.doFinal(plainBytes);
        //String encryptedString = Base64.encodeToString(cipherData, Base64.NO_PADDING);
        String encryptedString = DatatypeConverter.printBase64Binary(cipherData);
        String encryptedData = encryptedString;
    }
    catch(Exception e){
    }       
}
