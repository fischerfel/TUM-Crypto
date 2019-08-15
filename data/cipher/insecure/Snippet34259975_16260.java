       try {
            Utils.logDebug(TAG, "Decrypting!");
            File encfile = new File(getFilesDir() + "/encrypted.axx");
            int read;
            if (!encfile.exists())
                encfile.createNewFile();
            File decfile = new File(getFilesDir() + "/decrypted.mp4");
            if (!decfile.exists())
                decfile.createNewFile();
            FileInputStream encfis = new FileInputStream(encfile);
            FileOutputStream decfos = new FileOutputStream(decfile);
            Cipher decipher = Cipher.getInstance("AES");
            byte key[] = Base64.decode("CWTr 45Qg eHhy n23d YPC3 DjRi IxUe bt77 TVzQ NtSh HEc=", Base64.DEFAULT);
            SecretKey skey = new SecretKeySpec(key, 0, key.length, "AES");
            decipher.init(Cipher.DECRYPT_MODE, skey);
            CipherOutputStream cos = new CipherOutputStream(decfos, decipher);
            while ((read = encfis.read()) != -1) {
                cos.write(read);
                cos.flush();
            }
            cos.close();
            Utils.logDebug(TAG, "Done decrypting!");
        } catch (Exception e) {
            Utils.logError(TAG, "TESTING error: " + e.getMessage());
        }
