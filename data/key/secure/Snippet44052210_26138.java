private void receiveFile(Socket s, byte[] secret){
        try(FileOutputStream fileWriter = new FileOutputStream(FILE)){
            Cipher cipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5PADDING");
            SecretKeySpec keySpec = new SecretKeySpec(secret, ALGORITHM); 
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            try(CipherInputStream cipherIn = new CipherInputStream(s.getInputStream(), cipher)){
                byte[] fileBuffer = new byte[1024];
                int len;
                while ((len = cipherIn.read(fileBuffer)) >= 0)
                    fileWriter.write(fileBuffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        //TODO checkMD5
    }
