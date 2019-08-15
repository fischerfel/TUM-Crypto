String test = "1234567891";

InputStream inputStream = new ByteArrayInputStream(test.getBytes());
try (ChunkedDigestStream shaStream = new ChunkedDigestStream(inputStream, MessageDigest.getInstance("MD5"),
        5)) {

    // Create byte array to read data in chunks
    byte[] byteArray = new byte[10];

    // Read file data and update in message digest
    while (shaStream.read(byteArray) != -1) {
        System.out.println(new String(byteArray));
    }

    shaStream.close();
    System.out.println(shaStream.getChunkDigests());
}
