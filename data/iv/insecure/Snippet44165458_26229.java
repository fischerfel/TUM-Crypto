Java Code: 

public static String Encrypt(String PlainText) throws Exception {
        try {
            _aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = "jywseolkdiwpkqse".getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] iv = "jywseolkdiwpkqse".getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            _aesCipher.init(1, (java.security.Key) keySpec, ivSpec);
            byte[] plainText = PlainText.getBytes();
            byte[] result = _aesCipher.doFinal(plainText);
            return Base64.encode(result);
        } catch (Exception ex1) {
            System.out.println("Exception setting up cipher: "
                    + ex1.getMessage() + "\r\n");
            ex1.printStackTrace();
            return "";
        }
    }
