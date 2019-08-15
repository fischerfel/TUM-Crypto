public static String getSHA1(String plainText) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            md.update(plainText.getBytes());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < md.digest().length; i++) {
                hexString.append(Integer.toHexString(0xFF & md.digest()[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
