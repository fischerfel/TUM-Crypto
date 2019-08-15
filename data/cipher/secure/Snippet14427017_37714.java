public String encrypt(String unencryptedString, byte[] ivBytes, byte[] keyBytes){

 String encryptedString = null;

   try {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec Key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] encryptedText = cipher.doFinal(plainText);
        encryptedString = new String(Base64.encodeBase64(encryptedText));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return encryptedString;
            }
