MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(fileName);

    byte[] dataBytes = new byte[1024];

    int nread = 0;
    while ((nread = fis.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
    }

    fis.close();

    byte[] mdbytes = md.digest();

    StringBuffer hexString = new StringBuffer();

    for (int i = 0; i < mdbytes.length; i++) {
        hexString.append(Integer.toString((mdbytes[i] & 0xFF) + 256, 16)
                .substring(1));
    }


    return hexString.toString();
