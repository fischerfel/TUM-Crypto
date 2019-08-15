PDDocument pdf = PDDocument.load(inputFile);
MessageDigest digest = MessageDigest.getInstance("SHA-256");
TSAClient tsaClient = new TSAClient(new URL("your time stamp authority"), null, null, digest);
pdf.addSignature(signature, new TimestampSignatureImpl(tsaClient));
pdf.saveIncremental(new FileOutputStream(outputFile));
pdf.close();
