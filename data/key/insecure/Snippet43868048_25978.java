XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");

NodeList sigList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
if (sigList.getLength() == 0) {
    throw new Exception("Cannot find Signature element");
}
String secretKey = "Abcd1234abcd1234Abcd1234abcd1234";
SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
DOMValidateContext valContext = new DOMValidateContext(secret_key, sigList.item(0));
XMLSignature signature = factory.unmarshalXMLSignature(valContext);

System.out.println("Core validity: " + signature.validate(valContext));
