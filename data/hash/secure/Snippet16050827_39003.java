    /**
     * Gets Hash of file.
     * 
     * @param file String path + filename of file to get hash.
     * @param hashAlgo Hash algorithm to use. <br/>
     *     Supported algorithms are: <br/>
     *     MD2, MD5 <br/>
     *     SHA-1 <br/>
     *     SHA-256, SHA-384, SHA-512
     * @return String value of hash. (Variable length dependent on hash algorithm used)
     * @throws IOException If file is invalid.
     * @throws HashTypeException If no supported or valid hash algorithm was found.
     */
    public String getHash(String fileStr, String hashAlgo) throws IOException, HasherException {

        File file = new File(fileStr);

        MessageDigest md = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        ByteBuffer bbf = null;
        StringBuilder hexString = null;

        try {
            md = MessageDigest.getInstance(hashAlgo);
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            bbf = ByteBuffer.allocate(1024); // allocation in bytes

            int bytes;

            while ((bytes = fc.read(bbf)) != -1) {
                md.update(bbf.array(), 0, bytes);
            }

            fc.close();
            fis.close();

            byte[] mdbytes = md.digest();

            hexString = new StringBuilder();

            for (int i = 0; i < mdbytes.length; i++) {
                hexString.append(Integer.toHexString((0xFF & mdbytes[i])));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new HasherException("Unsupported Hash Algorithm.", e);
        }
    }
