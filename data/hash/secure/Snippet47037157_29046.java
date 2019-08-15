   public class Dummy(){   
//...
    private byte[] getSignature(final String applicationID, final String applicationSecret, final String certIdentifier,
                  final byte[] content)
                  throws IOException {


        try (final ClientSecretKeyVaultCredential credentials = new ClientSecretKeyVaultCredential(applicationID,
            applicationSecret, this.logger)) {
          final String salt = "mysalt";
          final MessageDigest md = MessageDigest.getInstance("SHA-512");
          md.update(salt.getBytes("UTF-8"));
          final byte[] digest = md.digest(content);

          final KeyVaultClient vc = new KeyVaultClient(credentials);

          final CertificateBundle certBundle = vc.getCertificate(certIdentifier);
           //url reference to signing key related to certificate.
          final String keyIdentifier = certBundle.kid();
          //the certificate
          final byte[] certBytes = certBundle.cer();
          // Get some Bouncy Castle objects things that may be useful
          final Certificate cert = org.bouncycastle.asn1.x509.Certificate
              .getInstance(ASN1Primitive.fromByteArray(certBytes));
          final List<Certificate> certList = new ArrayList<>();
          certList.add(cert);
          final JcaCertStore certs = new JcaCertStore(certList);
          //actual signing
          final KeyOperationResult signResult = vc.sign(keyIdentifier, JsonWebKeySignatureAlgorithm.RS512, digest);
          final byte[] signatureBytes = signResult.result();
          // More bouncy things
          final DERBitString signatureValue = new DERBitString(signatureBytes);
          final CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

          gen.addCertificates(certs);

          final AttributeCertificateInfo acinfo = AttributeCertificateInfo.getInstance(cert);

          final AttributeCertificate attrCert = new AttributeCertificate(acinfo, cert.getSignatureAlgorithm(),
              signatureValue);
          final X509AttributeCertificateHolder holder = new X509AttributeCertificateHolder(attrCert);
          gen.addAttributeCertificate(holder);

          // Now how to get a CMSSignedData object with a
          // certificate and a signature. I do not want to sign
          // locally.
          final CMSSignedData signedData = null;// todo: fix
          return signedData.getEncoded();

        } catch (Exception e) {
          this.logger.log(Level.SEVERE, e.getMessage(), e);
          throw new IllegalStateException(e.getMessage());
        }
      }
}
