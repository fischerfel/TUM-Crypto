public static void getMD5(String fileName) throws Exception{
    InputStream input =  new FileInputStream(fileName);
    byte[] buffer = new byte[1024];

    MessageDigest hash = MessageDigest.getInstance("MD5");
    int read;
    do {
        read = input.read(buffer);
        if (read > 0) {
            hash.update(buffer, 0, read);
        }
    } while (read != -1);
    input.close();
}
