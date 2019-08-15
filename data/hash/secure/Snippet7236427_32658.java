/**
 * Gets a 1-way hashed value of the device's unique ID. This value is
 * encoded using a SHA-256 one way hash and cannot be used to determine what
 * device this data came from.
 * 
 * @param appContext
 *            The context used to access the settings resolver
 * @return An 1-way hashed identifier unique to this device or null if an
 *         ID, or the hashing algorithm is not available.
 */
public static String getGlobalDeviceId(final Context appContext) {
    String systemId = System.getString(appContext.getContentResolver(),
            System.ANDROID_ID);
    if (systemId == null) {
        return null;
    }

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(systemId.getBytes());
        BigInteger hashedNumber = new BigInteger(1, digest);
        return new String(hashedNumber.toString(16));

    } catch (NoSuchAlgorithmException e) {
        return null;
    }
}
