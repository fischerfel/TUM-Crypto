boolean validateSignaturesImproved(byte[] pdfByte, String signatureFileName) throws IOException, CMSException, OperatorCreationException, GeneralSecurityException
{
    boolean result = true;
    try (PDDocument pdfDoc = PDDocument.load(pdfByte))
    {
        List<PDSignature> signatures = pdfDoc.getSignatureDictionaries();
        int index = 0;
        for (PDSignature signature : signatures)
        {
            String subFilter = signature.getSubFilter();
            byte[] signatureAsBytes = signature.getContents(pdfByte);
            byte[] signedContentAsBytes = signature.getSignedContent(pdfByte);
            System.out.printf("\nSignature # %s (%s)\n", ++index, subFilter);

            if (signatureFileName != null)
            {
                String fileName = String.format(signatureFileName, index);
                Files.write(new File(RESULT_FOLDER, fileName).toPath(), signatureAsBytes);
                System.out.printf("    Stored as '%s'.\n", fileName);
            }

            final CMSSignedData cms;
            if ("adbe.pkcs7.detached".equals(subFilter) || "ETSI.CAdES.detached".equals(subFilter))
            {
                cms = new CMSSignedData(new CMSProcessableByteArray(signedContentAsBytes), signatureAsBytes);
            }
            else if ("adbe.pkcs7.sha1".equals(subFilter))
            {
                cms = new CMSSignedData(new ByteArrayInputStream(signatureAsBytes));
            }
            else if ("adbe.x509.rsa.sha1".equals(subFilter) || "ETSI.RFC3161".equals(subFilter))
            {
                result = false;
                System.out.printf("!!! SubFilter %s not yet supported.\n", subFilter);
                continue;
            }
            else if (subFilter != null)
            {
                result = false;
                System.out.printf("!!! Unknown SubFilter %s.\n", subFilter);
                continue;
            }
            else
            {
                result = false;
                System.out.println("!!! Missing SubFilter.");
                continue;
            }

            SignerInformation signerInfo = (SignerInformation) cms.getSignerInfos().getSigners().iterator().next();
            X509CertificateHolder cert = (X509CertificateHolder) cms.getCertificates().getMatches(signerInfo.getSID())
                    .iterator().next();
            SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder().setProvider(new BouncyCastleProvider()).build(cert);

            boolean verifyResult = signerInfo.verify(verifier);
            if (verifyResult)
                System.out.println("    Signature verification successful.");
            else
            {
                result = false;
                System.out.println("!!! Signature verification failed!");

                if (signatureFileName != null)
                {
                    String fileName = String.format(signatureFileName + "-sigAttr.der", index);
                    Files.write(new File(RESULT_FOLDER, fileName).toPath(), signerInfo.getEncodedSignedAttributes());
                    System.out.printf("    Encoded signed attributes stored as '%s'.\n", fileName);
                }

            }

            if ("adbe.pkcs7.sha1".equals(subFilter))
            {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] calculatedDigest = md.digest(signedContentAsBytes);
                byte[] signedDigest = (byte[]) cms.getSignedContent().getContent();
                boolean digestsMatch = Arrays.equals(calculatedDigest, signedDigest);
                if (digestsMatch)
                    System.out.println("    Document SHA1 digest matches.");
                else
                {
                    result = false;
                    System.out.println("!!! Document SHA1 digest does not match!");
                }
            }
        }
    }
    return result;
}
