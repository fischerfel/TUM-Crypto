            try {
                        Cipher desCipher;
                        byte[] decodedKey = pass.getText().getBytes();
                        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
                        desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                        byte[] text = input.getText().getBytes();
                        desCipher.init(Cipher.DECRYPT_MODE, originalKey);
                        byte[] textDecrypted = desCipher.doFinal(input.getText().getBytes());
                        output.setText(Arrays.toString(textDecrypted));
                    } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                        Error(ex.getMessage());
                        Logger(ex.getMessage());
                    }
