 try {
                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, sks);
                decodedBytes = c.doFinal(encodedBytes);
            } catch (Exception e) {
                Log.e("decryptFIN128AES", "AES decryption error");
            }
