String tx = "Some message";

// generate message attachment
MimeBodyPart attachment = new MimeBodyPart();
attachment.setDisposition(MimeBodyPart.ATTACHMENT);
attachment.setFileName("myFileName.txt");
attachment.setDataHandler(
    new DataHandler(
        new ByteArrayDataSource(tx, "text/plain")));

// generate SHA1 hash
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
sha1.update(tx.getBytes("ISO-8859-1"));
String hashString = Hex.encodeHexString(sha1.digest()); // using Apache Commons

// generate SHA1 attachment
MimeBodyPart hash = new MimeBodyPart();
hash.setDisposition(MimeBodyPart.ATTACHMENT);
hash.setFileName("myFileName.sha1");
hash.setDataHandler(
    new DataHandler(
        new ByteArrayDataSource(hashString, "text/plain")));
