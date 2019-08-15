protected HashMap<String, PdfFileSpecification> cache = new HashMap<>();
private final byte[] BUFFER = new byte[1024];

public PdfFileSpecification getPdfFileSpecification(final PdfWriter pdfWriter, final String name, final byte[] data) throws IOException {

    String hash = createMD5Hash(data);
    PdfFileSpecification pdfFileSpecification = cache.get(hash);

    if (pdfFileSpecification == null) {
        pdfFileSpecification = PdfFileSpecification.fileEmbedded(pdfWriter, null, name, data);
        cache.put(hash, pdfFileSpecification);
        return pdfFileSpecification;
    }
    System.out.println(String.format("Name: %s Hash: %s", name, hash));
    return pdfFileSpecification;
}

private String createMD5Hash(final byte[] data) {

MessageDigest messageDigest;

    try {
        messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        return null;
    }

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

    try {
        int i;
        while ((i = byteArrayInputStream.read(BUFFER)) != -1) {
            messageDigest.update(BUFFER, 0, i);
        }
        byteArrayInputStream.close();
    } catch (IOException e) {
        return null;
    }
    byte[] mdbytes = messageDigest.digest();

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
}
