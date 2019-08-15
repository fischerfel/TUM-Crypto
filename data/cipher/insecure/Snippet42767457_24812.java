public static void standardExceptionHandling(Exception exc, Logger alog) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exc.printStackTrace(pw);
    alog.info(sw.toString()); /* Line 292 */
}

/**
 * Method that takes a key/value set, converts it into a standard web parameter string
 * and then encrypts the string.
 *
 * @param values the key value set
 * @return the encrypted string
 *
 */
public static String encrypt(Map<String, String> values) {
    StringBuilder unencrypted = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, String> value : values.entrySet()) {
        if (first) {
            first = false;
        } else {
            unencrypted.append("&");
        }
        unencrypted.append(value.getKey())
                .append("=")
                .append(value.getValue());
    }

    try {
        Cipher cipher = Cipher.getInstance("AES");
        Key aesKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(unencrypted.toString().getBytes("UTF8"));

        String enc = new sun.misc.BASE64Encoder().encode(encrypted);
        return enc;
    } catch (Exception e) {
        standardExceptionHandling(e, log);
        return "";
    }
}

/**
 * Method that takes an encrypted string containing a standard web parameter string
 * and converts it to a key/value set
 *
 * @param encrypted the encrypted string
 * @return the key value set
 */
public static Map<String, String> decrypt(String encrypted) {
    String decrypted = "";
    try {
        Cipher cipher = Cipher.getInstance("AES");
        Key aesKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(encrypted);
        decrypted = new String(cipher.doFinal(dec), "UTF8");
    } catch (Exception e) {
        standardExceptionHandling(e, log);
    }

    Map<String, String> values = new HashMap<String, String>();
    for (String pair : decrypted.split("&")) {
        String[] split_pair = pair.split("=");
        String key, value;
        if (split_pair.length == 1) {
            key = split_pair[0];
            value = "";
        } else if (split_pair.length == 2) {
            key = split_pair[0];
            value = split_pair[1];
        } else if (split_pair.length > 2) {
            log.debug("Error when decrypting string, parameter found with more than 2 parts (" + pair + ")");
            continue;
        } else {
            // We should never reach this, as it is impossible to split a string into a 0 length array.
            log.debug("The impossible happened, we split a String into a 0 length array (" + pair + ")");
            continue;
        }
        // This is only reach when key and value have been initialised thank to the continue statements when we hit
        // an error state.
        values.put(key, value);
    }
    return values;
}
