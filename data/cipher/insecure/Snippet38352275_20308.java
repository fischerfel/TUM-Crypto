public static String encrypt(String accessToken) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        String merchantKey = "11111111111111111111";
        String st = StringUtils.substring(merchantKey, 0, 16);
        System.out.println(st);
        Key secretKey = new SecretKeySpec(st.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(accessToken.getBytes());

        // convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encryptedByte.length; i++) {
            sb.append(Integer.toString((encryptedByte[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
