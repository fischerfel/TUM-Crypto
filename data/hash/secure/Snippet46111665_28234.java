public String Hash(String str) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(str.getBytes("UTF-16LE"));
        byte messageDigest[] = digest.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String h = Integer.toHexString(0xFF & messageDigest[i]);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        result = hexString.toString().toLowerCase();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return ReverseString(result).substring(5, 25);
}

 public static String ReverseString(String s)
    {
        return new StringBuilder(s).reverse().toString();
    }
