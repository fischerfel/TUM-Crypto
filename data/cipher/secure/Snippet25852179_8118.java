BigInteger e = new BigInteger("9d7aa162117a8a9610ed2ddea713d7b", 16);
BigInteger m = new BigInteger("c9869917572adbb60a2c30ddec2551f", 16);

RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);
KeyFactory keyFac = KeyFactory.getInstance("RSA", "BC");
PublicKey pubKey = keyFac.generatePublic(spec);

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

// UNENCRYPTED_LOGIN = "201211130215"
// UNECRYPTED_TYPE = "1"

byte[] login = cipher.doFinal(UNENCRYPTED_LOGIN.getBytes("ASCII"));
byte[] type = cipher.doFinal(UNENCRYPTED_TYPE.getBytes("ASCII"));

// login = 00d571a40ef7359b2e9e10b7c5dd621c
// should be 02f0cc389fb88e6b4aaa4e2477858ca9

// type = 0a5c2e176c81b23b2e1dd635f2427c0f
// it is correct
