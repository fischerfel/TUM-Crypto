PdfReader reader = new PdfReader(Base64.decode(pdfB64));
reader.setAppendable(true);
baos = new ByteArrayOutputStream();

PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\0');
appearance = stamper.getSignatureAppearance();

appearance.setReason("Test");
appearance.setLocation("A casa de la caputeta");
appearance.setVisibleSignature("TMAQ-TSR[0].Pagina1[0].DadesSignatura[0].Representant[0]");
appearance.setCertificate(chain[0]);

PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
dic.setReason(appearance.getReason());
dic.setLocation(appearance.getLocation());
dic.setContact(appearance.getContact());
dic.setDate(new PdfDate(appearance.getSignDate()));
appearance.setCryptoDictionary(dic);

HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
exc.put(PdfName.CONTENTS, new Integer(reservedSpace.intValue() * 2 + 2));
appearance.preClose(exc);

ExternalDigest externalDigest = new ExternalDigest()
{
    public MessageDigest getMessageDigest(String hashAlgorithm) throws GeneralSecurityException
    {
        return DigestAlgorithms.getMessageDigest(hashAlgorithm, null);
    }
};

sgn = new PdfPKCS7(null, chain, "SHA256", null, externalDigest, false);
InputStream data = appearance.getRangeStream();
hash = DigestAlgorithms.digest(data, externalDigest.getMessageDigest("SHA256"));
cal = Calendar.getInstance();

byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, cal, null, null, CryptoStandard.CMS);
sh = MessageDigest.getInstance("SHA256", "BC").digest(sh);

hashPdf = new String(Base64.encode(sh));
