public String decryptString(String dataToDecrypt) {
        SharedPreferences prefs= context.getSharedPreferences("appname", 0);
        if (prefs.getString("SECRET_KEY","") != "") {
            byte[] decodedBytes = null;
            try {
                Cipher c = Cipher.getInstance("AES");

                String key = prefs.getString("SECRET_KEY","")
                byte[] encodedKey = Base64.decode(key, Base64.DEFAULT);
                SecretKey originalKey = new SecretKeySpec(encodedKey, 0,
                        encodedKey.length, "AES");
                c.init(Cipher.DECRYPT_MODE, originalKey);

                byte[] dataInBytes = Base64.decode(dataToDecrypt,
                        Base64.DEFAULT);

                decodedBytes = c.doFinal(dataInBytes);
                return new String(decodedBytes);
            } catch (Exception e) {
//              Log.e(TAG, "AES decryption error");
                e.printStackTrace();
                return null;
            }

        } else
            return null;

    }
