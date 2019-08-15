try {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ips = new IvParameterSpec(iv);

            // we generate a AES SecretKeySpec object which contains the secret key.
            // SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
            Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), ips);

            byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes());
            byte[] base64encodedSecretData = Base64.encodeBase64(cipherText);
            String secretString = new String(base64encodedSecretData);
            return secretString;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Encryption error for " + textToEncrypt, e);
        }
        return "";
