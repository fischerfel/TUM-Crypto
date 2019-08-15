try {


            // byte[] salt = generateSalt();
            byte[] salt = saltStr.getBytes();
           // Log.i(TAG, "Salt: " + salt.length + " " + HexEncoder.toHex(salt));
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            byte[] key = secret.getEncoded();
            //Log.i(TAG, "Key: " + HexEncoder.toHex(key));

            // PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATION_COUNT);

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // byte[] encryptionSalt = generateSalt();
            // Log.i(TAG, "Encrypted Salt: " + encryptionSalt.length + " " + HexEncoder.toHex(encryptionSalt));
            // PBEParameterSpec pbeParamSpec = new PBEParameterSpec(encryptionSalt, 1000);
            // byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            Log.i(TAG, encryptionCipher.getParameters() + " ");
          // byte[] iv = generateIv();
           //byte[] iv ="QmBSbUZMUwld31DPrqyVSA==".getBytes();
            IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOf(iv,16));

            encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
            byte[] encryptedText = encryptionCipher.doFinal(plainText.getBytes());
           // Log.i(TAG, "Encrypted: " + new String(encryptedText));
            Log.i(TAG, "Encrypted: " + Base64.encodeToString(encryptedText, Base64.DEFAULT));

            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
            byte[] decryptedText = decryptionCipher.doFinal(encryptedText);
            Log.i(TAG, "Decrypted....: " + new String(decryptedText));

        } catch (Exception e) {
            e.printStackTrace();
        }
