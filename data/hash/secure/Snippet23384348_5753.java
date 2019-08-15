InputStream data = sap.getRangeStream();

X509Certificate[] chain = new X509Certificate[1];
chain[0] = userCert;

PdfPKCS7 sgn = new PdfPKCS7(null, chain, null, algorithm, null, false);

MessageDigest digest = MessageDigest.getInstance("SHA256", "BC");
byte[] buf = new byte[8192];
int n;
while ((n = data.read(buf, 0, buf.length)) > 0) {
    digest.update(buf, 0, n);
}
byte hash[] = digest.digest();
logger.info("PDF hash  created");
Calendar cal = Calendar.getInstance();

byte[] ocsp = null;
byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
sh = MessageDigest.getInstance("SHA256", "BC").digest(sh);

final String encode = Utils.base64Encode(sh);
SignatureService service = new SignatureService();

logger.info("PDF hash  sended to sign for web service");
MobileSignResponse signResponse = service.mobileSign(mobilePhone, signText, encode, timeout, algorithm, username, password, "profile2#sha256", signWsdl);

if (!signResponse.getStatusCode().equals("0")) {
    throw new Exception("Signing fails.\n" + signResponse.getStatusMessage());
}

byte[] signedHashValue = Utils.base64Decode(signResponse.getSignature());
sgn.setExternalDigest(signedHashValue, null, "RSA");

byte[] paddedSig = new byte[csize];


byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, null, ocsp);
System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
if (csize + 2 < encodedSig.length) {
    throw new Exception("Not enough space for signature");
}

PdfDictionary dic2 = new PdfDictionary();
dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
sap.close(dic2);
logger.info("Signing successful");
