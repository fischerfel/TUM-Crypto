final DigestOutputStream dos = new DigestOutputStream(baosb, MessageDigest.getInstance("md5"));
ByteStreams.copy(getInputStream(), dos);
dos.close(); // <-- this fixed the problem!
final String hb = DatatypeConverter.printHexBinary(dos.getMessageDigest().digest());
