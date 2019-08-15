public final String computeMD5Hash(final String pass){
    try {
        // Create MD5 Hash
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(pass.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String h = Integer.toHexString(0xFF & messageDigest[i]);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        System.out.println(hexString.toString());
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return "";
