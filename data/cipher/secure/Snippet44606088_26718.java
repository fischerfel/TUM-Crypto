try {
                SecretKey skeySpec = new SecretKeySpec(mImageProvider.getKey(), "AES");
                cipher = Cipher.getInstance("AES/CFB8/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(mImageProvider.getKey()));
            } catch (NoSuchAlgorithmException e) {
    ...
    }
