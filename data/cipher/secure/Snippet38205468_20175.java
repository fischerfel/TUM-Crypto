public String Encrypt (String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    try {
        AssetManager assets = context.getAssets();
        byte[] key = readFully(
                assets.open("encryption.der", AssetManager.ACCESS_BUFFER));
        KeySpec publicKeySpec = new X509EncodedKeySpec(key);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        Key pk = kf.generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CipherOutputStream cout = new CipherOutputStream(out, cipher);
        try {
            cout.write(plain.getBytes(UTF_8));
            cout.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                cout.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
        encrypted  = new String(encode(out.toByteArray(), DEFAULT), "UTF-8");

        return encrypted;
    } catch (IOException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }

    return null;

}

static byte[] readFully(InputStream inputStream) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        byte[] tmp = new byte[1024];
        int readLen, size = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((readLen = in.read(tmp)) != -1) {
            if (((long) size + readLen) > Integer.MAX_VALUE) {
                // woah! did we just ship an app of 2GB?
                throw new IllegalStateException("Invalid file. File size exceeds expected "
                        + "maximum of 2GB");
            }
            size += readLen;
            out.write(tmp, 0, readLen);
        }
        return out.toByteArray();
    }
