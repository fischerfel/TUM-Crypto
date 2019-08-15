package jms.cxmessenger;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SecureTrustManager implements X509TrustManager {
    private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];
    private CustomDefaultHostnameVerifier verifier = new CustomDefaultHostnameVerifier();

    private TrustManager[] trustManagers;

    {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            trustManagers = trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void check(X509Certificate[] chain, String authType) throws CertificateException {
        boolean trusted = false;
        if (chain.length > 0) {
            for (TrustManager trustManager : trustManagers) {
                try {
                    if (trustManager instanceof X509TrustManager) {
    /* line 43 */       ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);//line 43
                        trusted = true;
                    }
                } catch (CertificateException e) {
                }
            }
        }
        if (!trusted && !Boolean.getBoolean("DEACTIVATE_HOSTNAME_VALIDATION")) {
            checkCN(chain);
        }
    }

    public X509Certificate[] getValidCertificates(X509Certificate[] chain, String peerHost) {
        return verifier.getValidCertificates(chain, peerHost);
    }

    private void checkCN(X509Certificate[] chains) throws CertificateException {
        if (Boolean.getBoolean("DEBUG")) {
            System.out.println("checkCN(X509Certificate[] chains) : " + Arrays.toString(chains));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chains.length; i++) {
            String cn = extractCN(chains[i].getIssuerX500Principal().getName());
            if (cn == null) {
                sb.append("\n\tFailed to authenticate Server CA : Name = "
                        + chains[i].getIssuerX500Principal().getName());
            } else {
                return;
            }
        }
        if (Boolean.getBoolean("DEBUG")) {
            System.out.println("sb.toString : " + sb.toString());
        }
        throw new CertificateException(sb.toString());
    }

    private String extractCN(final String subjectPrincipal) {
        if (subjectPrincipal == null) {
            return null;
        }
        try {
            final LdapName subjectDN = new LdapName(subjectPrincipal);
            final List<Rdn> rdns = subjectDN.getRdns();
            for (int i = rdns.size() - 1; i >= 0; i--) {
                final Rdn rds = rdns.get(i);
                final Attributes attributes = rds.toAttributes();
                final Attribute cn = attributes.get("cn");
                if (cn != null) {
                    try {
                        final Object value = cn.get();
                        if (value != null) {
                            return value.toString();
                        }
                    } catch (final NoSuchElementException ignore) {
                    } catch (final NamingException ignore) {
                    }
                }
            }
        } catch (final InvalidNameException e) {
        }
        return null;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String paramString) throws CertificateException {
        for (X509Certificate certificate : certificates) {
            certificate.checkValidity();
        }
        check(certificates, paramString);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String paramString) throws CertificateException {
        for (X509Certificate certificate : certificates) {
            certificate.checkValidity();
        }
        check(certificates, paramString);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return trustManagers != null && trustManagers.length > 0 && trustManagers[0] instanceof X509TrustManager
                ? ((X509TrustManager) trustManagers[0]).getAcceptedIssuers() : EMPTY_X509CERTIFICATE_ARRAY;
    }

}
