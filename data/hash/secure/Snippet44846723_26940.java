public byte[] getPdfHash(@NotNull Calendar signDate, int estimatedSize, @NotNull String hashAlgorithm,
        boolean isTimestampOnly) throws Exception {

    pdfReader = new PdfReader(inputFilePath, pdfPassword != null ? pdfPassword.getBytes() : null);
    AcroFields acroFields = pdfReader.getAcroFields();
    boolean hasSignature = acroFields.getSignatureNames().size() > 0;

    byteArrayOutputStream = new ByteArrayOutputStream();
    pdfStamper = PdfStamper.createSignature(pdfReader, byteArrayOutputStream, '\0', null, hasSignature);
    pdfStamper.setXmpMetadata(pdfReader.getMetadata());

    pdfSignatureAppearance = pdfStamper.getSignatureAppearance();
    pdfSignature = new PdfSignature(PdfName.ADOBE_PPKLITE,
            isTimestampOnly ? PdfName.ETSI_RFC3161 : PdfName.ADBE_PKCS7_DETACHED);
    pdfSignature.setReason(signReason);
    pdfSignature.setLocation(signLocation);
    pdfSignature.setContact(signContact);
    pdfSignature.setDate(new PdfDate(signDate));
    pdfSignatureAppearance.setCryptoDictionary(pdfSignature);

    // certify the pdf, if requested
    if (certificationLevel > 0) {
        // check: at most one certification per pdf is allowed
        if (pdfReader.getCertificationLevel() != PdfSignatureAppearance.NOT_CERTIFIED)
            throw new Exception(
                    "Could not apply -certlevel option. At most one certification per pdf is allowed, but source pdf contained already a certification.");
        pdfSignatureAppearance.setCertificationLevel(certificationLevel);
    }

    HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
    exc.put(PdfName.CONTENTS, new Integer(estimatedSize * 2 + 2));

    pdfSignatureAppearance.preClose(exc);

    MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
    InputStream rangeStream = pdfSignatureAppearance.getRangeStream();
    int i;
    while ((i = rangeStream.read()) != -1) {
        messageDigest.update((byte) i);
    }

    return messageDigest.digest();
}
