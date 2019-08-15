    public String computeFingerPrint(final byte[] certRaw) {

    String strResult = "";

    MessageDigest md;
    try {
        md = MessageDigest.getInstance("SHA1");
        md.update(certRaw);
        for (byte b : md.digest()) {
            strAppend = Integer.toString(b & 0xff, 16);
            if (strAppend.length() == 1)
                strResult += "0";
            strResult += strAppend;
        }
        strResult = strResult.toUpperCase(DATA_LOCALE);
    }
    catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }

    return strResult;
}
