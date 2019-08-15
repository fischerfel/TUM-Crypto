    String result = null;
    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(source);
    //The result should be one 128 integer
    byte temp[] = md.digest();
    char str[] = new char[16 * 2];
    int k = 0;
    for (int i = 0; i < 16; i++) {
        byte byte0 = temp[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
    }
    result = new String(str);
