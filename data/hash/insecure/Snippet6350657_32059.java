MessageDigest md = MessageDigest.getInstance("SHA1");
FileInputStream fis = new FileInputStream("path/to/file.exe");
ByteArrayInputStream byteArrayInputStream =
    new ByteArrayInputStream(fis.toString().getBytes());
DigestInputStream dis = new DigestInputStream(byteArrayInputStream, md);
BufferedInputStream bis = new BufferedInputStream(fis);
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

int ch;
while ((ch = dis.read()) != -1) {
    byteArrayOutputStream.write(ch);
}

byte[] newInput = byteArrayOutputStream.toByteArray();
System.out.println("in digest : " +
    byteArray2Hex(dis.getMessageDigest().digest()));

byteArrayOutputStream = new ByteArrayOutputStream();
DigestOutputStream digestOutputStream =
    new DigestOutputStream(byteArrayOutputStream, md);
digestOutputStream.write(newInput);

System.out.println("out digest: " +
    byteArray2Hex(digestOutputStream.getMessageDigest().digest()));
System.out.println("length: " + 
    new String(
        byteArray2Hex(digestOutputStream.getMessageDigest().digest())).length());

digestOutputStream.close();
byteArrayOutputStream.close();
dis.close();
