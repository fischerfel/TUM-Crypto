XmlPullParser getXpp(InputStream is, String keyString){
DESKeySpec ks = new DESKeySpec(keyString.getBytes("ASCII"));
SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
SecretKey key = skf.generateSecret(ks);

Cipher c = Cipher.getInstance("DES/CBC/PKCS7Padding");
c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyString.getBytes("ASCII")));
CipherInputStream cis = new CipherInputStream(is, c);

BufferedReader r = new BufferedReader(new InputStreamReader(cis, "UTF-8"));
StringBuilder total = new StringBuilder();
String line;
while ((line = r.readLine()) != null) {
    total.append(line).append('\n');
}

InputStream stream = new ByteArrayInputStream(total.toString().getBytes("UTF-8"));

XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
factory.setNamespaceAware(true);
XmlPullParser xpp = factory.newPullParser();
xpp.setInput(stream, "UTF-8");
return xpp; 
