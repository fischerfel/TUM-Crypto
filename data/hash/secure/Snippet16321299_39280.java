/**
 * Gets Hash of file using java.nio File Channels and ByteBuffer 
 * <br/>for native system calls where possible. This may improve <br/>
 * performance in some circumstances.
 * 
 * @param fileStr String path + filename of file to get hash.
 * @param hashAlgo Hash algorithm to use. <br/>
 *     Supported algorithms are: <br/>
 *     MD2, MD5 <br/>
 *     SHA-1 <br/>
 *     SHA-256, SHA-384, SHA-512
 * @param BUFFER Buffer size in bytes. Recommended to stay in<br/>
 *          multiples of 2 such as 1024, 2048, <br/>
 *          4096, 8192, 16384, 32768, 65536, etc.
 * @return String value of hash. (Variable length dependent on hash algorithm used)
 * @throws IOException If file is invalid.
 * @throws HashTypeException If no supported or valid hash algorithm was found.
 */
public String getHashNIO(String fileStr, String hashAlgo, int BUFFER) throws IOException, HasherException {

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
        bbf = ByteBuffer.allocateDirect(BUFFER); // allocation in bytes - 1024, 2048, 4096, 8192

        int b;

        b = fc.read(bbf);

        while ((b != -1) && (b != 0)) {
            bbf.flip();

            byte[] bytes = new byte[b];
            bbf.get(bytes);

            md.update(bytes, 0, b);

            bbf.clear();
            b = fc.read(bbf);
        }

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
