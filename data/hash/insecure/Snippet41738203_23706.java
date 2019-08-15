public static void signWithTimeStampToken() throws InvalidPasswordException, NoSuchAlgorithmException, IOException, TSPException {
    final File inFile = new File("test.pdf");
    final File outFile = new File("test_signed.pdf");
    final MessageDigest digest = MessageDigest.getInstance("SHA-1");
    final TSAClient tsaClient = new TSAClient(new URL("your service"), null, null, digest);
    PdfTimeStampUtils.signPdf(inFile, outFile, tsaClient);
}

private static void signPdf(final File inFile, final File outFile, final TSAClient tsaClient) throws InvalidPasswordException, IOException, NoSuchAlgorithmException,
        TSPException {
    final PDSignature signature = new PDSignature();
    signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
    signature.setSubFilter(COSName.getPDFName("ETSI.RFC3161"));
    signature.setSignDate(Calendar.getInstance());
    final PDDocument pdf = PDDocument.load(inFile);

    final TimestampSignatureImpl sig = new TimestampSignatureImpl(tsaClient);
    pdf.addSignature(signature, sig);
    pdf.saveIncremental(new FileOutputStream(outFile));
    pdf.close();
}
