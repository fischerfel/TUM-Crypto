  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
            byte[] plainBytes = Data.getBytes(UNICODE_FORMAT);
            byte[] encrypted = cipher.doFinal(plainBytes);
            String encryption = Base64.encodeBase64String(encrypted);
            return encryption;
