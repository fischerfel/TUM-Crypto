byteArrayMessage = plainMessage.getBytes(ENCODING);
int keySize = keyMode;
int maxBlockSize = (keySize / 8 - 11);
int blocksCount = (int) Math.ceil((double) byteArrayMessage.length / maxBlockSize);
byte[][] blocksCollection = new byte[blocksCount][];
KeyFactory kf = null;
X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(ConvertData.hexStringToByteArray(hexPubliKey));
PublicKey pubKey = null;
kf = KeyFactory.getInstance("RSA");
pubKey = kf.generatePublic(pubSpec);
cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

// encoding
        byte[] encrypted = null;
        int i = 0;
        int startIndex;
        int endIndex;
        int sizeOfBlocks = 0;
        while (i < blocksCount) {
            startIndex = i * (maxBlockSize);
            endIndex = startIndex + maxBlockSize;
            try {
                encrypted = cipher.doFinal((Arrays.copyOfRange(byteArrayMessage, startIndex, endIndex)));
                sizeOfBlocks += encrypted.length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            blocksCollection[i] = encrypted;
            i++;
        }


        i = 0;
        int n = blocksCollection.length;
        String gluedEncodedData = "";
        String encodedChunk;
        while (i < n) {
            encodedChunk = (ConvertData.byteArrayToHexString(blocksCollection[i]));
            gluedEncodedData += encodedChunk;
            i++;
        }
        Logger.trace("Glued Encoded data " + gluedEncodedData);
        return gluedEncodedData;
