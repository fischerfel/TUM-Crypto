class EncryptDecrypt {
    private Cipher cipher;
    private static SecretKeySpec secretKeySpec;
    private static IvParameterSpec ivParameterSpec;
    private boolean do_encrypt = true;

    /**
     * Construct EncryptDecrypt instance that checks the current logged-in
     * user status; basically if the user is the special NOUSER, where
     * the user does not use a password, then encryption decryption is
     * skipped, the alternative constructor does not undergo this check and
     * thus will always encrypt (see alternative)
     * @param context   The context, required for database usage (user)
     * @param skey      The secret key to be used to encrypt/decrypt
     * @param userid    The userid (so that the salt can be obtained)
     */
    EncryptDecrypt(Context context, String skey, long userid) {
        DBUsersMethods users = new DBUsersMethods(context);
        if (MainActivity.mLoginMode == LoginActivity.LOGINMODE_NONE) {
            do_encrypt = false;
            return;
        }
        String saltasString = users.getUserSalt(userid);
        String paddedskey = (skey + saltasString).substring(0,16);

        secretKeySpec = new SecretKeySpec(paddedskey.getBytes(),"AES/CBC/PKCS5Padding");
        ivParameterSpec = new IvParameterSpec((saltasString.substring(0,16)).getBytes());
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e){
        }
    }

    /**
     * Construct EncryptDecrypt instance that does not check user login-in
     * mode, thus the assumption is that this user is NOT the special user
     * NOUSER that doesn't require a password to login; this constructor
     * is designed to ONLY be used when a user has been added by NOUSER,
     * and to then encrypt the data using the enccryptForced method solely
     * to encrypt any existing card data for the new user that has a password.
     *
     * @param context   The context, required for database usage (user)
     * @param skey      The secret key to be used to encrypt/decrypt
     * @param userid    The userid (so that the salt can be obtained)
     * @Param mode      Not used other than to distinguish constructor
     */
    EncryptDecrypt(Context context, String skey, long userid, boolean mode) {
        DBUsersMethods users = new DBUsersMethods(context);
        String saltasString = users.getUserSalt(userid);
        String paddedskey = (skey + saltasString).substring(0,16);
        secretKeySpec = new SecretKeySpec(paddedskey.getBytes(),"AES/CBC/PKCS5Padding");
        ivParameterSpec = new IvParameterSpec((saltasString.substring(0,16)).getBytes());
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    /**
     * Normal encryption routine that will not encrypt data if the user is
     * the special case NOUSER (i.e LOGIN mode is NOUSER), otherwise data
     * is encrypted.
     *
     * @Param toEncrypt     The string to be encrypted
     * @return              The encryted (or not if NOUSER) data as a string
     */
    String encrypt(String toEncrypt) {
        if (!do_encrypt) {
            return toEncrypt;
        }
        byte[] encrypted;
        try {
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
            encrypted = cipher.doFinal(toEncrypt.getBytes());
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

    /**
     * Encryption, irrespective of the USER type, noting that this should
     * only be used in conjunction with an EncryptDecrypt instance created
     * using the 2nd/extended constructor
     *
     * @param toEncrypt     The string to be encrypted
     * @return              The encrypted data as a string
     */
    String encryptForced(String toEncrypt) {
        byte[] encrypted;
        try {
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
            encrypted = cipher.doFinal(toEncrypt.getBytes());
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

    /**
     * Decrypt an encrypted string
     * @param toDecrypt     The encrypted string to be decrypted
     * @return              The decrypted string
     */
    String decrypt(String toDecrypt)  {
        if (!do_encrypt) {
            return toDecrypt;
        }
        byte[] decrypted;
        try {
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);
            decrypted = cipher.doFinal(Base64.decode(toDecrypt,Base64.DEFAULT));
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return new String(decrypted);
    }
}
