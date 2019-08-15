String serialEnc = "s+3YvGfHzmTgDgBfWSGbyxZRQsHNmz3WcX03B0kVHz+HRZYbL+l4hoLBSROeSWnS"; // Java, works
//String serialEnc = "p06pEvjWe8Lc1t+g8ROQgYDsrFC6emHpCEyUKWoZMrI="; //Python, does not
serialEnc = serialEnc.replaceAll(" ", "+");

byte[] iv =
    { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

IvParameterSpec ips = new IvParameterSpec(iv);
SecretKeySpec skeySpec = new SecretKeySpec(decPayloadKey, "AES");
Cipher symCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
symCipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);

byte[] encBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(serialEnc);
byte[] bytes = symCipher.doFinal(encBytes);
String plain = new String(bytes, "UTF-8");
System.out.println(plain);    
