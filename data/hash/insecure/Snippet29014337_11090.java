UUID idPDF = UUID.randomUUID();

InputStream input = new ByteArrayInputStream(pdfOriginal);
OutputStream output = new ByteArrayOutputStream();
PdfReader reader = new PdfReader(input);
PdfStamper stamper = PdfStamper.createSignature(reader, output, '\0');

PdfSignatureAppearance sap = stamper.getSignatureAppearance();

String urx = String.valueOf(Integer.parseInt(FIRMA_POSICION_X) + 250);
String lly = String.valueOf(Integer.parseInt(FIRMA_POSICION_Y) - 50);

stamper.addSignature("Signature" + idPDF.toString(), 1, 0, 0, 0, 0);        
sap.setVisibleSignature(new Rectangle(
                Float.parseFloat(FIRMA_POSICION_X), 
                Float.parseFloat(FIRMA_POSICION_Y), 
                Float.parseFloat(urx), 
                Float.parseFloat(lly)),1,"Signature" + idPDF.toString());

// I define the sign and date
sap.setSignatureGraphic(getSello());
sap.setSignDate(new GregorianCalendar());

sap.setReason(razon);
sap.setLocation(ubicacion);
sap.setContact(contacto);
sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);


HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>(); 
exc.put(PdfName.CONTENTS, new Integer(0x2502));     
sap.preClose(exc);

MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
byte buf[] = new byte[8192];
int n;
InputStream inp = sap.getRangeStream();
while ((n = inp.read(buf)) > 0) {
 messageDigest.update(buf, 0, n);
}
byte hash[] = messageDigest.digest();
PdfLiteral slit = new PdfLiteral(buf);
byte[] outc = new byte[(slit.getPosLength() - 2) / 2];          
Signature sign = Signature.getInstance("SHA1withRSA");
sign.initSign(key);
sign.update(hash);          
PdfDictionary dic = new PdfDictionary();
dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));     
sap.close(dic); 

stamper.close();
reader.close();     
output.flush();
output.close();
input.close();
