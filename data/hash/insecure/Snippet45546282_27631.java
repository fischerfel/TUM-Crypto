/**
 * 
 * @param pkg packageName
 * @return
 */
public String getSingInfo (String pkg) {
    try {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(pkg, PackageManager.GET_SIGNATURES);
        Signature[] signs = packageInfo.signatures;
        Signature sign = signs[0];
        String s = getMd5(sign);
        return "md5:" + s ;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "";
}



private String getMd5 (Signature signature) {
    return encryptionMD5(signature.toByteArray());
}

public static String encryptionMD5(byte[] byteStr) {
    MessageDigest messageDigest = null;
    StringBuffer md5StrBuff = new StringBuffer();
    try {
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(byteStr);
        byte[] byteArray = messageDigest.digest();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return md5StrBuff.toString();
}
