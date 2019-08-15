if(getFileToEncrypt.exists()){
            System.out.println("-----------FILE EXISTS--------------");
            secRand = SecureRandom.getInstance("SHA1PRNG");
            key = new BigInteger(128, secRand).toString(16);
            rawKey = key.getBytes();
            sKeySpec = new SecretKeySpec(rawKey, "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            inputStream = new FileInputStream(getFileToEncrypt);
            byte[] byts = new byte[(int) getFileToEncrypt.length()];
            inputStream.read(byts);
            inputStream.close();
            encrypted = cipher.doFinal(byts);
            encryptedFile.createNewFile();
            ostr = new FileOutputStream(encryptedFile);
            ostr.write(encrypted);
            ostr.close();   
            System.out.println("--------FILE ENCRYPTION COMPLETE----------");

        }
