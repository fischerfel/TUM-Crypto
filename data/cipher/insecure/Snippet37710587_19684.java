public void decrypt(final byte[] secret, final File encryptedFile ) throws IOException {
        final ByteArrayOutputStream baos;
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, modelEncryptor.getKey());
            try (FileInputStream fis = new FileInputStream(encryptedFile); CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                baos = new ByteArrayOutputStream();
                byte[] buff = new byte[8];
                int numRead = cis.read(buff);
                while (numRead != -1) {
                    baos.write(buff, 0, numRead); //I was trying to combine this write with the unzipping write.
                    numRead = cis.read(buff);
                }
            }
        } catch (Exception ex) {
            throw new IOException("Unable to decrypt " + encryptedFile, ex);
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()); ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(bais))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                int BUFFER = 2048;
                ByteArrayOutputStream fos = new ByteArrayOutputStream(2048);
                try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                    int count;
                    byte data[] = new byte[BUFFER];
                    while ((count = zipIn.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                }
                final List<String> lines = Arrays.asList(fos.toString().split("\r\n"));
                //Do something with lines...
            }
        }
        baos.close();
    }
