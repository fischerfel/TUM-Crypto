ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
ObjectOutputStream outputstream = new ObjectOutputStream(bytestream);

nu.xom.Builder builder = new nu.xom.Builder(false, new nu.xom.samples.MinimalNodeFactory()); //The false parameter is for avoid a ValidationException that trows XOM

try {
nu.xom.canonical.Canonicalizer outputter = new nu.xom.canonical.Canonicalizer(outputstream);
nu.xom.Document input = builder.build(file);
outputter.write(input);
 }
catch (Exception ex) {
System.err.println(ex);
ex.printStackTrace();
}

outputstream.close();

MessageDigest sha1 = MessageDigest.getInstance("SHA1");
sha1.reset();
sha1.update(java.nio.ByteBuffer.wrap(bytestream.toByteArray()));
salidasha1=sha1.digest();
String tagDigestValue=new String(Base64.encodeBase64(salidasha1));
