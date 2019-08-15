MessageDigest digest = MessageDigest.getInstance("MD5");

        FileInputStream is = new FileInputStream(FTP_listFiles[i]); //ERROR HERE   
                                                                    //FTP_files is a FTPFile from FTPClient apache commons.
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while( (read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            System.out.println("MD5: \n" + output);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            }
            catch(IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
