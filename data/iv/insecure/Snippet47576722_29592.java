String path = "/path/to/secret/saved/in/text";
String payload = "...ENCRYPTED DATA...";
StringBuilder output = new StringBuilder();

String iv = payload.substring(0, 16);
byte[] secret = Base64.getDecoder().decode(Files.readAllBytes(Paths.get(path)));
String data = payload.substring(16);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(), 0, cipher.getBlockSize());
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec); // This line throws exception : 

cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
