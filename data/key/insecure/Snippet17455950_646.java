String input="Text";
String key="1234567891234567";
    byte[] crypted = null;
            try {
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skey);
                crypted = cipher.doFinal(input.getBytes());
            } catch (Exception exception) {
                throw exception;
            }
            return new String(Base64.encodeBase64(crypted));
