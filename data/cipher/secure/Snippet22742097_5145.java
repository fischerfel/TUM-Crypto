byte[] expBytes = Base64.decode("AQAB".getBytes("UTF-8"), Base64.DEFAULT);
byte[] modBytes = Base64.decode("rFsMn+idg8jmVMk249DzJc7AFft3+/jcnYDTh9wHee3tgFu1gBRh7e+ao+MWq7NEN0N7kUHa7O4c/ND2Ahcx/h4mXD5KDoixFRBUsxYqCJVA68qYJ7vozVPMjNr4jeOo1xt+oevO5+mUWtcaib5Iw51u1Jq/6qCqLsm8Eq3cnsE=".getBytes("UTF-8"), Base64.DEFAULT);
byte[] dBytes = Base64.decode("Gs8mzZDPP3p2aWXLBfCwgYcBVeoBpc318wHg5VcSSqL5uGeLedqxyOLmOOvP0PFXgQkcJWIK/aOkGqcePQECo3TNiK+uLSwc97V3spZah70FFJVyh23Y+o0wlRGHAm5Nj9QieHlVwhgJPkNUJYgH9qkwB9aCpl+rdAG3da2fQ2E=".getBytes("UTF-8"), Base64.DEFAULT);

BigInteger modules = new BigInteger(1, modBytes);
BigInteger exponent = new BigInteger(1, expBytes);
BigInteger d = new BigInteger(1, dBytes);

KeyFactory factory = KeyFactory.getInstance("RSA");
Cipher cipher = Cipher.getInstance("RSA");
String input = "test";

RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
PublicKey pubKey = factory.generatePublic(pubSpec);
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] encrypted = cipher.doFinal(input.getBytes());

String strEncrypted = Base64.encodeToString(encrypted, Base64.DEFAULT);
