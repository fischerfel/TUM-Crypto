    DataContainer doc = DataContainerFactory.instance().newContainer();
    MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
    DigestInputStream digestInputStream = new java.security.DigestInputStream(new DecodingInputStream(in), md);

    doc.deserialize(digestInputStream);
    info.setDocumentHash(md.digest());
