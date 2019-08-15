        byte[] var21;
        try {

            Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
            cp.init(mode, key);
            int blockSize = cp.getBlockSize();
            int blocksNum = (int) Math.ceil((double) data.length / (double) blockSize);
            int calcSize = blockSize;
            Object buffer = null;
            outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < blocksNum; ++i) {
                if (i == blocksNum - 1) {
                    calcSize = data.length - i * blockSize;
                }

                byte[] var22 = cp.doFinal(data, i * blockSize, calcSize);

                try {
                    outputStream.write(var22);
                } catch (IOException var19) {
                    throw new GeneralSecurityException("RSA error", var19);
                }
            }
            var21 = outputStream.toByteArray();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException var18) {
                    ;
                }
            }

        }
