    @Override
public byte[] sign(InputStream content) throws IOException
{
    try
    {
        List<Certificate> certList = new ArrayList<Certificate>();
        certList.addAll(Arrays.asList(certificateChain));
        certList.add(certificate);
        Store certs = new JcaCertStore(certList);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

        byte[] sh = MessageDigest.getInstance("SHA-256").digest(IOUtils.toByteArray(content));

        String hexencodedDigest = new BigInteger(1, sh).toString(16);
        hexencodedDigest = hexencodedDigest.toUpperCase();
        // Send digest to signing service
        final String signedHash = certProvider.signPdfDigest(hexencodedDigest);

        ContentSigner nonSigner = new ContentSigner() {

            @Override
            public byte[] getSignature() {
                try {
                    return Hex.decodeHex(signedHash.toCharArray());
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }

            @Override
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256WithRSAEncryption");
            }
        };

        org.bouncycastle.asn1.x509.Certificate cert = org.bouncycastle.asn1.x509.Certificate.getInstance(certificateChain[0].getEncoded());
        JcaSignerInfoGeneratorBuilder sigb = new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build());

        //ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
        gen.addSignerInfoGenerator(sigb.build(nonSigner, new X509CertificateHolder(cert)));
        gen.addCertificates(certs);
        CMSTypedDataInputStream msg = new CMSTypedDataInputStream(new ByteArrayInputStream("useless_data".getBytes())); // this is never used.

        //CMSProcessableInputStream msg = new CMSProcessableInputStream(content);
        CMSSignedData signedData = gen.generate(msg, false);
        if (tsaClient != null)
        {
            signedData = signTimeStamps(signedData);
        }

        byte[] pkcs7 = signedData.getEncoded();

        content.close();

        return pkcs7;
    }
    catch (GeneralSecurityException | CMSException | TSPException | OperatorCreationException e)
    {
        throw new IOException(e);
    } catch (SessionExpiredException | TokenExpiredException | CertProviderException e) {
        throw new IOException(e);
    }
}
