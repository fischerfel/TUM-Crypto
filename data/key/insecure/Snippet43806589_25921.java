JAXBContext jaxbContext = JAXBContext.newInstance(DataPDU.class);
DataPDU myDataPDU = new DataPDU();
myDataPDU.setRevision("2.0.6");

// marshall the file
Marshaller marshaller = jaxbContext.createMarshaller();

DOMResult domResult = new DOMResult();
marshaller.marshal(myDataPDU, domResult);

// get the document list
Document document = (Document) domResult.getNode();

// signing process
XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");

SignatureMethod signatureMethod =
    factory.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256", null);

CanonicalizationMethod canonicalizationMethod =
    factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null);

List<Transform> transforms = new ArrayList<Transform>();
transforms.add(factory.newTransform(Transform.ENVELOPED, (XMLStructure) null));
transforms.add(factory.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", (XMLStructure) null));

DigestMethod digestMethod = factory.newDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", null);

Reference reference = factory.newReference("", digestMethod, transforms, null, null);

SignedInfo signedInfo =
    factory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(reference));

String secretKey = "Abcd1234abcd1234Abcd1234abcd1234";
SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

Element LAUElement = document.createElementNS("urn:swift:saa:xsd:saa.2.0", "Saa:LAU");
Element rootElement = document.getDocumentElement();
rootElement.appendChild(LAUElement);

DOMSignContext domSignContext = new DOMSignContext(secret_key, LAUElement);
domSignContext.setDefaultNamespacePrefix("ds");

XMLSignature signature = factory.newXMLSignature(signedInfo, null);
signature.sign(domSignContext);