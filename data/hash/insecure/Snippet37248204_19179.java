private String m21862a(String str) {
    try {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(str.getBytes());
        byte[] digest = instance.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            String toHexString = Integer.toHexString(b & RadialCountdown.PROGRESS_ALPHA);
            while (toHexString.length() < 2) {
                toHexString = "0" + toHexString;
            }
            stringBuffer.append(toHexString);
        }
        return stringBuffer.toString();
    } catch (Exception e) {
        Logger.m22252a((Object) this, e);
        return BuildConfig.FLAVOR;
    }
}
