String username = "username";
String password = "password";
String id = "123456"; 

String toBeHashed = username + password + id;
MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
byte[] hashed = sha256.digest(toBeHashed.getBytes("UTF-8"));

String hashString = "=" + Base64.encodeBase64String(hashed);
System.out.println(hashString);

String salt = "salt";
String anotherId = "123";
byte[] forAuth = (salt + orgId + hashString).getBytes("UTF-8");

//Mocked "secret key". Original key string is of size 16 bytes.
byte[] secKey = "secret key".getBytes("UTF-8");

SecretKey secretKey = new SecretKeySpec(secKey, 0, secKey.length, "AES");

Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

byte[] authorizationKey = aesCipher.doFinal(forAuth);

System.out.println("-------------------");
System.out.println("-------------------");
System.out.println(Base64.encodeBase64String(authorizationKey));
