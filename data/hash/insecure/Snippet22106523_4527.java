/**
 * Se firma el documento
 *
 * @return Se retorna un valor solamente para indicar que el proceso
 * concluyo.
 * @throws Exception Se dispara una excepcion en caso de error.
 */
private String firmarPDF() throws Exception {
    String clave = "2967";
    JOptionPane.showMessageDialog(null, "Entra a firmarPDF()", "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    //Se lee el KeyStore del Smart Card
    KeyStore keyStore;
    keyStore = KeyStore.getInstance("PKCS11");
    keyStore.load(null, clave.toCharArray());
    this.clave.setText(null);
    //Se selecciona el certificado para firma

    Enumeration aliasesEnum = keyStore.aliases();
    PrivateKey key = null;
    X509Certificate cert;
    String alias = null;
    while (aliasesEnum.hasMoreElements()) {
        alias = (String) aliasesEnum.nextElement();
        cert = (X509Certificate) keyStore.getCertificate(alias);
        if (cert.getSubjectDN().getName().indexOf("(FIRMA)") > 0) {
            key = (PrivateKey) keyStore.getKey(alias, clave.toCharArray());
            break;
        }
    }

    //Se carga el PDF original Ahora desde la web!
    URL sisdoc = new URL(host + servletIN + fullpar);
    //Se conecta al servlet que recibe el documento
    URLConnection yc = sisdoc.openConnection();
    //Se carga el pdf
    PdfReader reader = new PdfReader(yc.getInputStream());

    URL sisdocRet = new URL(host + servletOUT + fullpar);
    //Se conecta al servlet que enviar el documento firmado
    URLConnection respuesta = sisdocRet.openConnection();

    respuesta.setRequestProperty("Content-Type", "application/pdf");
    respuesta.setDoOutput(true);
    respuesta.setDoInput(true);
    respuesta.setUseCaches(false);
    OutputStream oe = respuesta.getOutputStream();

    JOptionPane.showMessageDialog(null, "Va a crear el estampado", "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    //FileOutputStream oe = new FileOutputStream("C:\\Sistema de documentacion SISDOC\\Firma2.pdf");

    PdfStamper stp = PdfStamper.createSignature(reader, oe, '\0');
    //Se crea el objeto que define la apariencia del cuadrito que muestra lainformacion de la firma
    PdfSignatureAppearance sap = stp.getSignatureAppearance();
    //Se establece la informaci�n a utilizar para encriptar
    sap.setCrypto(key, keyStore.getCertificateChain(alias), null, PdfSignatureAppearance.WINCER_SIGNED);
    //Se agregan los datos del motivo y ubicaci�n de la firma
    Calendar cal = Calendar.getInstance();
    sap.setSignDate(cal);
    sap.setReason("Validez a documento");
    sap.setLocation("Costa Rica");

    //Esta linea muestra un cuadro dentro del PDF con los datos de la firma. Si no se quiere mostrar se debe comentar la
    //siguiente linea de codigo           
    //  if (this.visibleFirma.isSelected()) {
    sap.setVisibleSignature(new com.lowagie.text.Rectangle(0, 0, 80, 80), 1, null);
    // }
    sap.setExternalDigest(null, new byte[20], null);
    sap.preClose();
    //hasta aqui funciona correctamente
    //Se crea el objeto que contendr� la firma a a�adir al PDF
    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

    byte buf[] = new byte[8192];
    int n;
    //Se obtiene el Stream con los datos de la llave privada y el certificado los cuales se
    //agregan al Digest
    InputStream inp = sap.getRangeStream();
    while ((n = inp.read(buf)) > 0) {
        messageDigest.update(buf, 0, n);
    }
    //Se obtiene los bytes del Digest
    byte hash[] = messageDigest.digest();
    JOptionPane.showMessageDialog(null, "hash[] tamaño: " + hash.length, "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    //Se establecen los est�ndares a utilizar para firmar
    PdfSigGenericPKCS sg = sap.getSigStandard();
    JOptionPane.showMessageDialog(null, "sg tamaño: " + (sg == null ? "nulo el sg" : "no nulo el sg"), "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    PdfLiteral slit = (PdfLiteral) sg.get(PdfName.CONTENTS);
    JOptionPane.showMessageDialog(null, "slit tamaño: " + slit.length(), "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    //Se crea un buffer donde se pasar�n los datos firmado
    byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
    //Se crea le objeto que se encarga de firmar el PDF y se firma
    PdfPKCS7 sig = sg.getSigner();
    JOptionPane.showMessageDialog(null, "sig tamaño nombre: " + (sig == null ? "nulo el sig" : "no nulo el sig"), "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    sig.setExternalDigest(null, hash, null);
    PdfDictionary dic = new PdfDictionary();

    byte[] ssig = sig.getEncodedPKCS7();
    JOptionPane.showMessageDialog(null, "Cantidad ssig: " + (ssig == null ? "nulo" : ssig.length), "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    System.arraycopy(ssig, 0, outc, 0, ssig.length);
    JOptionPane.showMessageDialog(null, "Cantidad outc: " + (outc == null ? "nulo" : ssig.length), "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    JOptionPane.showMessageDialog(null, "Se ejecuta la linea!!!!", "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    dic.put(PdfName.CONTENT, new PdfString(outc).setHexWriting(true));
    JOptionPane.showMessageDialog(null, "Se ejecuto la linea !!!!", "Firmar documento PDF", JOptionPane.ERROR_MESSAGE);
    sap.close(dic);
    oe.flush();
    oe.close();
    respuesta.connect();
    return "Servidor";

}
