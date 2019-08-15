byte[] modulusBytes = Base64.decode(this.getString(R.string.public_key_modulus));
byte[] exponentBytes = Base64.decode(this.getString(R.string.public_key_exponent));
BigInteger modulus = new BigInteger( modulusBytes );                
BigInteger exponent = new BigInteger( exponentBytes);

RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey pubKey = fact.generatePublic(rsaPubKey);

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] cipherData = cipher.doFinal( new String("big kitty dancing").getBytes() );    
