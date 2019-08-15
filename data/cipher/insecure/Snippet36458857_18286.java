PasswordDeriveBytes myPass = new PasswordDeriveBytes(password, salt);
SecretKeyFactory kf;
    try {
        Cipher desEDE = Cipher.getInstance("DESede/CB/NoPadding");
        kf = SecretKeyFactory.getInstance("DESede");
        key = myPass.getBytes(192);
        desEDEKey= kf.generateSecret(new DESedeKeySpec(key));           
        byte[] iv = DatatypeConverter.parseBase64Binary(ivText);
        desEDE.init(Cipher.DECRYPT_MODE, desEDEKey, new IvParameterSpec(iv));
        byte[] ct = desEDE.doFinal(DatatypeConverter.parseBase64Binary(texts));   
    }
