public class EncryptionHandler {
    private final static String PREFS_FILE_NAME = "SAVED_CREDENTIALS";
    private final static String MSG_FORMAT = "%s;%s";

    public static void saveCredentials(Context context, String username, String password) {
        try {
            String formattedMsg = String.format(MSG_FORMAT, username, password);

            String encryptedMsg = new String(AESCrypt.encrypt(getKeySpec(), getIV(), stringToBytes(formattedMsg)));

            //save encryptedMsg to SharedPreferences
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] stringToBytes(String msg) {
        return msg.getBytes(Charset.forName("UTF-8"));
    }

    public static LoginCredentials getSavedCredentials(Context context) {
        try {
            String encryptedMsg = getSharedPreferences(context).getString(PREFS_FILE_NAME, "");
            String messageAfterDecrypt = new String(AESCrypt.decrypt(getKeySpec(), getIV(), stringToBytes(encryptedMsg)));

            String[] split = messageAfterDecrypt.split(";");

            //irrelevant code

            return credentials;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] ivBytes = {0x00, 0x12, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x23, 0x52, 0x00, 0x00, 0x00, 0x00};

    private static SecretKeySpec secretKeySpec = new SecretKeySpec(ivBytes, "AES");

    private static SecretKeySpec getKeySpec() throws Exception {
        return secretKeySpec;
    }

    private static byte[] getIV() {
        return ivBytes;
    }
}
