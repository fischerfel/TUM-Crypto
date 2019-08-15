PdfReader reader = new PdfReader(PATH_TO_PDF);
AcroFields fields = reader.getAcroFields();
ArrayList<String> signatures = fields.getSignatureNames();
for (String signature : signatures)
{
    // Start revision extraction
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte bb[] = new byte[8192];
    InputStream ip = fields.extractRevision(signature);
    int n = 0;
    while ((n = ip.read(bb)) > 0)
        out.write(bb, 0, n);
    out.close();
    ip.close();
    MessageDigest md = MessageDigest.getInstance("SHA1");
    byte[] resum = md.digest(out.toByteArray());
    // End revision extraction        
}
