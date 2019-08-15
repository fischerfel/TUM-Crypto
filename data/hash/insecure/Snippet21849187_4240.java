public Attribute createTSToken(byte[] data) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
    // Generate timestamp
    MessageDigest digest = MessageDigest.getInstance("SHA1", "BC");
    TimeStampResponse response = timestampData(digest.digest(data));
    TimeStampToken timestampToken = response.getTimeStampToken();
    // Create timestamp attribute

    Attribute a = new Attribute(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken, new DERSet(ASN1Primitive.fromByteArray(timestampToken.getEncoded())));
    return a;
}
