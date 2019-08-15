String publicKey = "AJOnAeTfeU4K+do5QdBM2BQUhfrRI2rYf/Gk4a3jZJB2ewekgq2VgLNislBdql/glA39w0NjXZyTg0mW917JdUlHqKoQ9765pJc4aTjvX+3IxdFhteyO2jE3vKX1GgA3i3n6+sMBAJiT3ax57i68mbT+KAeP1AX9199aj2W4JZeP";
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
byte[] res = new Base64Encoder().decode(publicKey.getBytes());
X509EncodedKeySpec KeySpec = new X509EncodedKeySpec(res);
RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(KeySpec);

// here the exception occurs..

Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] cipherData = cipher.doFinal(input.getBytes());
return cipherData;
