public static String getSHA1(String plainText) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            md.update(plainText.getBytes(Charset.forName("UTF-8")));
            StringBuffer hexString = new StringBuffer();
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                hexString.append(Integer.toHexString(0xFF & bytes[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
