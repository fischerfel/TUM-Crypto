        this.secretKeySpec = new SecretKeySpec(key, "AES");
        this.iv = new IvParameterSpec(iv);
        this.cipher = Cipher.getInstance(ALGORITHM_AES256);
