        PDDocument document = PDDocument.load(documentFile);
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        signature.setName("Example User");
        signature.setLocation("Los Angeles, CA");
        signature.setReason("Testing");
        Calendar date = Calendar.getInstance();
        signature.setSignDate(date);
        document.addSignature(signature);

        ExternalSigningSupport externalSigningSupport = document.saveIncrementalForExternalSigning(null);

        byte[] content = IOUtils.toByteArray(externalSigningSupport.getContent());
        MessageDigest md = MessageDigest.getInstance("SHA256", new BouncyCastleProvider());
        byte[] digest = md.digest(content); // this is sent to client
