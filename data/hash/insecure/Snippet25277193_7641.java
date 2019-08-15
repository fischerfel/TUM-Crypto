// the .jpg is 32394 bytes

final MessageDigest md5 = MessageDigest.getInstance("md5");
md5.update(ByteStreams.toByteArray(getInputStream()));
final String h0 = DatatypeConverter.printHexBinary(md5.digest());
System.out.println("MD5 = " + h0); // MD5 = 98BE96C5B9C8D0E41723BBA6E508AADC

// raw MessageDigest from InputStream to ByteArrayOutputStream
final MessageDigest md5a = MessageDigest.getInstance("md5");
final ByteArrayOutputStream baosa = new ByteArrayOutputStream(32394);
ByteStreams.copy(getInputStream(), baosa);
md5a.update(baosa.toByteArray());
final String ha = DatatypeConverter.printHexBinary(md5a.digest());
assertArrayEquals(ByteStreams.toByteArray(getInputStream()),baosa.toByteArray());
assertEquals(h0,ha);

// raw DigestOutputStream to ByteArrayOutputStream
final ByteArrayOutputStream baosb = new ByteArrayOutputStream(32394);
final DigestOutputStream dos = new DigestOutputStream(baosb, MessageDigest.getInstance("md5"));
ByteStreams.copy(getInputStream(), dos);
final String hb = DatatypeConverter.printHexBinary(dos.getMessageDigest().digest());
assertArrayEquals(baosa.toByteArray(), baosb.toByteArray());
assertEquals(h0,hb);  // <-- this is where it fails
