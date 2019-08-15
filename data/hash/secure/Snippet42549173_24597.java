final class AndroidDeviceIdentifier {

private AndroidDeviceIdentifier() {
    // hidden constructor of singleton
}

/**
 * Returns a stable identifier for the current device.
 *
 * @param ctx The application's Context
 * @return The unique device identifier
 * @throws IllegalStateException If the device's identifier could not be determined
 */
public static String getUniqueDeviceIdentifier(@NonNull final Context ctx) throws IllegalStateException {
    try {
        return getDeviceUUID(ctx);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
        throw new IllegalStateException("Could not determine device identifier", e);
    }
}

private static String getDeviceUUID(Context ctx) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    byte[] hash = makeHash(getMac(ctx), getSerialNumber(ctx));
    return createUUIDFromHash(hash);
}

private static String createUUIDFromHash(byte[] hash) {
    return UUID.nameUUIDFromBytes(hash).toString().toLowerCase(); // Server side wants lower cased UUIDs
}

private static byte[] makeHash(final String mac, final String serialNumber) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest sha;

    sha = MessageDigest.getInstance("SHA-256");
    sha.reset();

    sha.update(mac.getBytes("UTF-8"));
    sha.update(serialNumber.getBytes("UTF-8"));

    return sha.digest();
}

private static String getSerialNumber(Context context) {
    String serialNumber = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    if (serialNumber == null) {
        serialNumber = "0000000000000000";
    }

    return serialNumber;
}

private static String getMac(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    String mac = wifiManager.getConnectionInfo().getMacAddress();
    if (mac == null) {
        mac = "000000000000";
    }
    return mac;
}
