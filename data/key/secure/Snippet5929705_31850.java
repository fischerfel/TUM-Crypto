cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(key.getBytes(DEFAULT_CHARSET), ALGORITHM));
