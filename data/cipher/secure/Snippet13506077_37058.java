File archivo_llave_publica = new File(direccion);
        byte[] bytes_llave = leer(archivo_llave_publica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");          
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes_llave);
        PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(src);
        return cipherData;
