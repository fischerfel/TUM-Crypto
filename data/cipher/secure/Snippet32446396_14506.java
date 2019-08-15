    loggedUser = null;
    sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES_FILE_NAME, MODE_PRIVATE);
    if (sharedPreferences.getAll().isEmpty()) {
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finishAffinity();
    }

    String loggedUserUsername = sharedPreferences.getString(AppConstants.LOGIN_CREDENTIAL_USERNAME_KEY, "");
    if (loggedUserUsername.isEmpty()) {
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginActivityIntent);
        finishAffinity();
    } else {
        File filesDirectory = new File(getFilesDir().getPath());
        for (File file: filesDirectory.listFiles()) {
            if (file.getName().contains(loggedUserUsername) && file.getName().contains(AppConstants.USER_INFO_FILE_SUFFIX)) {
                String deviceId;
                String deviceKey;
                byte[] secretBytes;
                byte[] ivBytes;
                FileInputStream fileInputStream = null;
                CipherInputStream cipherInputStream = null;
                ObjectInputStream objectInputStream = null;

                try {
                    deviceId = sharedPreferences.getString(AppConstants.LOGIN_CREDENTIAL_DEVICE_ID_KEY, "").replace("-", "");
                    deviceKey = sharedPreferences.getString(AppConstants.LOGIN_CREDENTIAL_DEVICE_KEY_KEY, "").replace("-", "");
                    secretBytes = deviceKey.substring(0, 16).getBytes();
                    ivBytes = deviceId.substring(deviceId.length() - 16, deviceId.length()).getBytes();

                    fileInputStream = openFileInput(file.getName());
                    final SecretKey secretKey = new SecretKeySpec(secretBytes, "AES");
                    final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
                    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
                    cipherInputStream = new CipherInputStream(fileInputStream, cipher);
                    objectInputStream = new ObjectInputStream(cipherInputStream);
                    loggedUser = new User((String)objectInputStream.readObject());
                } catch (ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException | JSONException e) {
                    if (AppConstants.DEBUG) Log.e(TAG, AppConstants.EXCEPTION_CAUGHT_MESSAGE + e.getMessage(), e);
                    loggedUser = null;
                } finally {
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e) {
                            if (AppConstants.DEBUG) Log.e(TAG, AppConstants.EXCEPTION_CAUGHT_MESSAGE + e.getMessage(), e);
                        }
                    } else {
                        if (AppConstants.DEBUG) Log.e(TAG, "objectOutputStream:null");
                    }

                    if (cipherInputStream != null) {
                        try {
                            cipherInputStream.close();
                        } catch (IOException e) {
                            if (AppConstants.DEBUG) Log.e(TAG, AppConstants.EXCEPTION_CAUGHT_MESSAGE + e.getMessage(), e);
                        }
                    } else {
                        if (AppConstants.DEBUG) Log.e(TAG, "cipherOutputStream:null");
                    }

                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            if (AppConstants.DEBUG) Log.e(TAG, AppConstants.EXCEPTION_CAUGHT_MESSAGE + e.getMessage(), e);
                        }
                    } else {
                        if (AppConstants.DEBUG) Log.e(TAG, "fileOutputStream:null");
                    }
                }
                break;
            }
        }
    }
