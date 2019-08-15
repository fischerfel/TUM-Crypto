                    // Encrypt
                    byte[] input = jo.toString().getBytes("UTF-8");

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] thedigest = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
                    SecretKeySpec skc = new SecretKeySpec(thedigest, "AES/ECB/PKCS5Padding");
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, skc);

                    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
                    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                    ctLength += cipher.doFinal(cipherText, ctLength);
                    String query = Base64.encodeToString(cipherText, Base64.DEFAULT);
