String myKey = "dfslkskfs";
MessageDigest sha = null;
key = myKey.getBytes("UTF-8");
sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16); // use only first 128 bit
secretKey = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
byte[]   bytesEncoded = Base64.encodeBase64(cipher.doFinal(json
        .getBytes("UTF-8")));
jsontext =  new String(bytesEncoded );
