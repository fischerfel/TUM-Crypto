public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        sb.append("text_to_crypt_with_aes");
        String clearText = sb.toString();
        StringBuilder sbKey = new StringBuilder(" 4288f0b8060ca1b ");
        for (int i = 0; i < 7; i++) {
            sbKey.append("\0");
        }

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            Key key = new SecretKeySpec(sbKey.toString().getBytes("UTF-8"),
                    "AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedMessageInBytes = cipher.doFinal(clearText
                    .getBytes("UTF-8"));
            byte[] b64 = Base64.encodeBase64(encryptedMessageInBytes);
            String scrambled_text = new String(b64, Charset.forName("US-ASCII"));
            System.out.println(scrambled_text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
