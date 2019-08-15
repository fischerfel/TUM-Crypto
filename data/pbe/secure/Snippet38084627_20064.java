 try {
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(ps, salt, ITERATIONS, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

            // Build encryptor and get IV
            ecipher = Cipher.getInstance(TRANSFORMATION);
            ecipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));

            // Build decryptor
            dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        } catch (Exception e) {
            e.printStackTrace();
        }
