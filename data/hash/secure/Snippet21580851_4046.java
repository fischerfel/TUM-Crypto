byte[] document = /* ... */
byte[] digest = MessageDigest.getInstance("SHA256").digest(document);
TimeStampRequestGenerator tsReqGen = new TimeStampRequestGenerator();
TimeStampRequest tsReq = tsReqGen.generate(CMSAlgorithm.SHA256, digest);
