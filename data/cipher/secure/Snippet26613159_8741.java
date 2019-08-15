byte[] modulusBytes = Base64.decode("zgfXY1oUe4nyndX4qtobP1BMxtJ1/rfKU5csdAcWrSVu6ZaEAX3rL3cWnaSLzX4E1BNjSP9pjge6TH7UoaWqOQ==");
byte[] exponentBytes = Base64.decode("AQAB");

BigInteger modulus = new BigInteger(1, modulusBytes );               
BigInteger exponent = new BigInteger(1, exponentBytes);

RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);

KeyFactory fact = KeyFactory.getInstance("RSA");

PublicKey pubKey = fact.generatePublic(rsaPubKey);

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] plainBytes = new String("Admin123").getBytes("UTF-8");

byte[] cipherData = cipher.doFinal( plainBytes );

String string = new String(cipherData);

System.out.println(URLEncoder.encode(string,"UTF-8"));
