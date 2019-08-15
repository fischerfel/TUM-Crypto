public class Test {

    static ReadableByteChannel readChannel = null;
    static WritableByteChannel writeChannel = null;
    static SecretKey key = makeKeyFromPassword("abcdefghi".getBytes()); 
    static byte b[];

    public static SecretKey makeKeyFromPassword(byte[] password) {
        try {
            DESKeySpec dks = new DESKeySpec(password);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            return skf.generateSecret(dks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void run(int mode) throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        // initializing cipher...
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, key);

        while (readChannel.read(readBuffer) != -1) {
            readBuffer.flip();
            cipher.doFinal(readBuffer, writeBuffer);
            writeBuffer.flip();
            writeChannel.write(writeBuffer);
            readBuffer.clear();
            writeBuffer.clear();
        }
    }

    static void f(String inFileString, String fileString, int mode) {
        FileOutputStream fos = null;
        FileSystem fs = FileSystems.getDefault();
        Path fp = fs.getPath(inFileString);

        try {
            readChannel = FileChannel.open(fp,
                    EnumSet.of(StandardOpenOption.READ));
            fos = new FileOutputStream(fileString);
            writeChannel = Channels.newChannel(fos);
            run(mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        f("C:\\test.txt", "C:\\des.txt", Cipher.ENCRYPT_MODE);
        System.out.println("Encrypted.");
        f("C:\\des.txt", "C:\\undes.txt", Cipher.DECRYPT_MODE);
        System.out.println("Decrypted.");
    }

}
