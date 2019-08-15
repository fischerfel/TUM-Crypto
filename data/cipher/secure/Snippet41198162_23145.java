private final String key = "12345678901234567890123456789012";

private void saveUserInfo() {
    String email = emailET.getText().toString();
    String password = passwordET.getText().toString();

   SecretKeySpec sks = generateKey();
    byte[] encodedEmail = encrypt(email, sks);
    byte[] encodedPassword = encrypt(password, sks);

    String decodedEmail = decrypt(encodedEmail, sks);
    String decodedPassword = decrypt(encodedPassword, sks);

    Log.d("Encoded Email", Base64.encodeToString(encodedEmail, Base64.DEFAULT));
    Log.d("Encoded Password", Base64.encodeToString(encodedPassword, Base64.DEFAULT));

    Log.d("Decoded Email", decodedEmail);
    Log.d("Decoded Password", decodedPassword);
}

public SecretKeySpec generateKey(){
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, secureRandom);
            SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");
            return sks;
        }
        catch (NoSuchAlgorithmException error){
            return null;
        }
    }

    public byte[] encrypt(String text, Key key){
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodedBytes = c.doFinal(text.getBytes());
            return encodedBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] text, Key key){
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = c.doFinal(text);
            return new String(decodedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
