     public String encryptString(String dataToEncrypt) {

        try {
            SharedPreferences prefs = context.getSharedPreferences("appname", 0);
            if (prefs.getString("SECRET_KEY","") == "") {
                secretKeySpec = GenerateSecretKeySpecs();
                String stringSecretKey = Base64.encodeToString(
                        secretKeySpec.getEncoded(), Base64.DEFAULT);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("SECRET_KEY", stringSecretKey);
                editor.commit();

            }
            if (prefs.getString("SECRET_KEY","") != "") {
                byte[] encodedBytes = null;

                Cipher c = Cipher.getInstance("AES");
                String key =prefs.getString("SECRET_KEY","");

                byte[] encodedKey = Base64.decode(key, Base64.DEFAULT);
                SecretKey originalKey = new SecretKeySpec(encodedKey, 0,
                        encodedKey.length, "AES");
                c.init(Cipher.ENCRYPT_MODE, originalKey);
                encodedBytes = c.doFinal(dataToEncrypt.getBytes());

                return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (Exception e) {
//          Log.e(TAG, "AES encryption error");
            return null;
        }
    }
