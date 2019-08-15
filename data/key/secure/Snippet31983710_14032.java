strSymAlg = rb.getString("SYM_KEY_ALG"); //SYM_KEY_ALG=AES

                cipher = Cipher.getInstance(strSymAlg);

                SecKey = new SecretKeySpec(hex2Byte(sSymmetricKey), strSymAlg);

                cipher.init(Cipher.DECRYPT_MODE, SecKey);

                baos = recoverFile(new FileInputStream(fileEnv), cipher);

                if (baos != null &&  isRecoveredFileValid((InputStream) new ByteArrayInputStream(baos.toByteArray()))) {

                    fileRecovered = (InputStream) new ByteArrayInputStream(baos.toByteArray());
                }

            }


private ByteArrayOutputStream recoverFile(FileInputStream in, Cipher cipher)  {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        int outLength = 0;
        boolean more = true;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            while (more) {
                inLength = in.read(inBytes);
                if (inLength == blockSize) {
                    outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                    baos.write(outBytes, 0, outLength);
                } else {
                    more = false;
                }
            }
            if (inLength > 0) {
                outBytes = cipher.doFinal(inBytes, 0, inLength);
            } else {
                outBytes = cipher.doFinal();
            }
            baos.write(outBytes);

        } catch (Exception e) {
            System.out.println("recoverFile1: " + e.getMessage());
           // e.printStackTrace();
            baos = null;
        }

        return baos;
    }
