// Digest generation step
MessageDigest md = MessageDigest.getInstance("SHA256", "BC");
byte[] digest = md.digest(IOUtils.toByteArray(content));

// Separate signature container creation step
List<Certificate> certList = Arrays.asList(chain);
JcaCertStore certs = new JcaCertStore(certList);

CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

Attribute attr = new Attribute(CMSAttributes.messageDigest,
        new DERSet(new DEROctetString(digest)));

ASN1EncodableVector v = new ASN1EncodableVector();

v.add(attr);

SignerInfoGeneratorBuilder builder = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider())
        .setSignedAttributeGenerator(new DefaultSignedAttributeTableGenerator(new AttributeTable(v)));

AlgorithmIdentifier sha256withRSA = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");

CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
InputStream in = new ByteArrayInputStream(chain[0].getEncoded());
X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);

gen.addSignerInfoGenerator(builder.build(
        new BcRSAContentSignerBuilder(sha256withRSA,
                new DefaultDigestAlgorithmIdentifierFinder().find(sha256withRSA))
                        .build(PrivateKeyFactory.createKey(pk.getEncoded())),
        new JcaX509CertificateHolder(cert)));

gen.addCertificates(certs);

CMSSignedData s = gen.generate(new CMSAbsentContent(), false);
return s.getEncoded();
