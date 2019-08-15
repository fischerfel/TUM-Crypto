public final class PubKeyManager implements X509TrustManager
{
  private static String PUB_KEY = "30820122300d06092a864886f70d0101...";

  public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
  {
    if (chain == null) {
      throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
    }

    if (!(chain.length > 0)) {
      throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
    }

    if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
      throw new CertificateException("checkServerTrusted: AuthType is not RSA");
    }

    // Perform customary SSL/TLS checks
    try {
      TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
      tmf.init((KeyStore) null);

      for (TrustManager trustManager : tmf.getTrustManagers()) {
        ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
      }
    } catch (Exception e) {
      throw new CertificateException(e);
    }

    // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
    // with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0x00 to drop.
    RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
    String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);

    // Pin it!
    final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
    if (!expected) {
      throw new CertificateException("checkServerTrusted: Expected public key: "
                + PUB_KEY + ", got public key:" + encoded);
      }
    }
  }
}
