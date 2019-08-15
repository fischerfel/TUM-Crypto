            byte inBuf[] = outA.toByteArray();

            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DESede");
            DESedeKeySpec keySpec = new DESedeKeySpec(passwd.getBytes());
            SecretKey secKey = keyFac.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, secKey);

            cipher.init(Cipher.ENCRYPT_MODE, secKey);

            byte[] b = cipher.doFinal(inBuf);
