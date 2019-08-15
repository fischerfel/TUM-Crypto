        key = KeyGenerator.getInstance(algorithm).generateKey();
        cipher = Cipher.getInstance("DESede");
        messageSource.getMessage("encryption.algorithm",null,localeUtil.getLocale());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] outputBytes = cipher.doFinal(input.getBytes());
