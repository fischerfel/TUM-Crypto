String fileName = "C:\\somewhere\\aesKey.dat";
byte[] encoded = Files.readAllBytes(Paths.get(fileName));
return new SecretKeySpec(encoded, "AES");
