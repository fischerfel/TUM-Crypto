public static String generateDeviceIdentifier(Context context) {
    String pseudoId = "35" +
            Build.BOARD.length() % 10 +
            Build.BRAND.length() % 10 +
            Build.CPU_ABI.length() % 10 +
            Build.DEVICE.length() % 10 +
            Build.DISPLAY.length() % 10 +
            Build.HOST.length() % 10 +
            Build.ID.length() % 10 +
            Build.MANUFACTURER.length() % 10 +
            Build.MODEL.length() % 10 +
            Build.PRODUCT.length() % 10 +
            Build.TAGS.length() % 10 +
            Build.TYPE.length() % 10 +
            Build.USER.length() % 10;

    String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


    String longId = pseudoId + androidId;

    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(longId.getBytes(), 0, longId.length());

        // get md5 bytes
        byte md5Bytes[] = messageDigest.digest();

        // creating a hex string
        String identifier = "";

        for (byte md5Byte : md5Bytes) {
            int b = (0xFF & md5Byte);

            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF) {
                identifier += "0";
            }

            // add number to string
            identifier += Integer.toHexString(b);
        }

        // hex string to uppercase
        identifier = identifier.toUpperCase();
        return identifier;
    } catch (Exception e) {
        return UUID.randomUUID().toString();
    }

}
