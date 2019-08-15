public static int X509_NAME_hash(X509Certificate x509Cert) throws IOException, NoSuchAlgorithmException {
    // get the subject principal
    X500Principal x500Princ = x509Cert.getSubjectX500Principal();
    byte[] newPrincEnc = x500Princ.getEncoded();
    final ASN1Sequence asn1Sequence = (ASN1Sequence) ASN1Primitive.fromByteArray(newPrincEnc);

    Debugger.log(asn1Sequence);

    List<byte[]> terms = new ArrayList<>();
    int finalLen = 0;

    // hash the encodables for each term individually and accumulate them in a list
    for (ASN1Encodable asn1Set : asn1Sequence.toArray()) {
        byte[] term = ((ASN1Set) asn1Set).getEncoded();
        term[9] = 0x0c; // change tag from 0x13 (printable string) to 0x0c

        for (int i = 11; i < term.length; i++) {
            byte actual = term[i];
            //lowercase only if the character is not ASCCI Extended (below 126)
            if (actual < 127) {
                term[i] = (byte) Character.toLowerCase((char) actual);
            }
        }

        terms.add(term);
        finalLen += term.length;
    }

    // hash all terms together in order of appearance
    int j = 0;
    byte[] finalEncForw = new byte[finalLen];
    for (byte[] term : terms)
        for (byte b : term)
            finalEncForw[j++] = b;

    return peekInt(MessageDigest.getInstance("SHA1").digest(finalEncForw), 0, ByteOrder.LITTLE_ENDIAN);
}

public static X509Certificate readCertificate(File rootFile) throws CertificateException, IOException {
    CertificateFactory fact = CertificateFactory.getInstance("X.509");
    FileInputStream is = new FileInputStream(rootFile);
    return (X509Certificate) fact.generateCertificate(is);
}

public static int peekInt(byte[] src, int offset, ByteOrder order) {
    if (order == ByteOrder.BIG_ENDIAN) {
        return (((src[offset++] & 0xff) << 24) | ((src[offset++] & 0xff) << 16) | ((src[offset++] & 0xff) << 8)
                | ((src[offset] & 0xff) << 0));
    } else {
        return (((src[offset++] & 0xff) << 0) | ((src[offset++] & 0xff) << 8) | ((src[offset++] & 0xff) << 16)
                | ((src[offset] & 0xff) << 24));
    }
}
