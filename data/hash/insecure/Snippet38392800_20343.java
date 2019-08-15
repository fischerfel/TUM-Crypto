KeyStore ks = KeyStore.getInstance("pkcs12");
ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
String alias = (String)ks.aliases().nextElement();
PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
Certificate[] chain = ks.getCertificateChain(alias);
PdfReader reader = new PdfReader("original.pdf");
FileOutputStream fout = new FileOutputStream("signed.pdf");
PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
PdfSignatureAppearance sap = stp.getSignatureAppearance();
sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
sap.setReason("I'm the author");
sap.setLocation("Lisbon");
// comment next line to have an invisible signature
sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
sap.setExternalDigest(new byte[128], new byte[20], "RSA");
sap.preClose();
MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
byte buf[] = new byte[8192];
int n;
InputStream inp = sap.getRangeStream();
while ((n = inp.read(buf)) > 0) {
   messageDigest.update(buf, 0, n);
}
byte hash[] = messageDigest.digest();
PdfSigGenericPKCS sg = sap.getSigStandard();
PdfLiteral slit = (PdfLiteral)sg.get(PdfName.CONTENTS);
byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
PdfPKCS7 sig = sg.getSigner();
Signature sign = Signature.getInstance("SHA1withRSA");
sign.initSign(key);
sign.update(hash);
sig.setExternalDigest(sign.sign(), hash, "RSA");
PdfDictionary dic = new PdfDictionary();
byte[] ssig = sig.getEncodedPKCS7();
System.arraycopy(ssig, 0, outc, 0, ssig.length);
dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
sap.close(dic);
