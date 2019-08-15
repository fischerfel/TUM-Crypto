AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(signatureAlgorithm);
AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
String digestAlgOID = digAlgId.getAlgorithm().getId();
MessageDigest.getInstance(digestAlgOID);
