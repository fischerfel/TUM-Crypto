    MessageDigest di = java.security.MessageDigest.getInstance("MD5");
    di.update(cadena.getBytes());
    byte mdi[] = di.digest();

    StringBuffer md5= new StringBuffer();
    for (byte b : mdi) {
        md5.append(Integer.toHexString(0xFF & b));
    }
