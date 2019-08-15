    InputStream input =  new FileInputStream(fileName);

    StringBuffer hexString = new StringBuffer();
    MessageDigest md = MessageDigest.getInstance("MD5");

    byte[] hash = md.digest();

    for (int i = 0; i < hash.length; i++) {
        if ((0xff & hash[i]) < 0x10) {
            hexString.append("0"
                    + Integer.toHexString((0xFF & hash[i])));
        } else {
            hexString.append(Integer.toHexString(0xFF & hash[i]));
        }
    }
    String string = hexString.toString();
    System.out.println(string);
