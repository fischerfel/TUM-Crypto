import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TestDynamicSSLCert {

    public static void main(String[] args)throws NamingException,IOException {
        DataInputStream din = new DataInputStream (System.in);
        String yes = "yes";

        String certpath = "C:\\cert.cer";
        String ldappath1 = "C:\\ldap.jks";
        String ldappath2 = "C:\\ldap.jks";    // setting valid key store path    

        while("yes".equalsIgnoreCase(yes.trim())){            
            System.out.println(" ldappath2 : "+ldappath2);
            Hashtable env = new Hashtable();
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL,"uid=admin,ou=system");
            env.put(Context.SECURITY_CREDENTIALS, "secret");
            env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://172.16.12.4:636/ou=system");
            try {

                java.io.InputStream in = new java.io.FileInputStream(certpath);
                java.security.cert.Certificate c = java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(in);
                java.security.KeyStore ks = java.security.KeyStore.getInstance("JKS");
                ks.load(null);
                if (!ks.containsAlias("alias ldap")) {
                    ks.setCertificateEntry("alias ldap", c);
                }
                java.io.OutputStream out = new java.io.FileOutputStream(ldappath1);
                char[] kspass = "changeit".toCharArray();
                ks.store(out, kspass);
                out.close();
                System.setProperty("javax.net.ssl.trustStore", ldappath2);
                System.setProperty("javax.net.ssl.trustStorePassword", "changeit");              
                // Custorm trust manager 
                MyX509TrustManager reload = new MyX509TrustManager(ldappath2,c);
                TrustManager[] tms = new TrustManager[] { reload };
                javax.net.ssl.SSLContext sslCtx = javax.net.ssl.SSLContext.getInstance("SSL");
                sslCtx.init(null, tms, null);  
                // Custom trust manager
            } catch (Exception e) {
                e.printStackTrace();
            }
            DirContext ctx = new InitialDirContext(env);
            NamingEnumeration enm = ctx.list("");
            while (enm.hasMore()) {
                System.out.println(enm.next());
            }                    
            ctx.close();
            System.out.println(" Go again by yes/no :");
            yes = din.readLine();
            ldappath2 = "C:\\invalidldap.jks"; // setting invalid keystore path

        }
    }
}

class MyX509TrustManager implements X509TrustManager {

    private final String trustStorePath;
    private X509TrustManager trustManager;
    private List<Certificate> tempCertList = new ArrayList<Certificate>();

    public MyX509TrustManager(String tspath,Certificate cert)throws Exception{
        this.trustStorePath = tspath;        
        tempCertList.add(cert);
        reloadTrustManager();
    }

    public MyX509TrustManager(String tspath)
            throws Exception {
        this.trustStorePath = tspath;
        reloadTrustManager();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        try {
            trustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException cx) {
            addServerCertAndReload(chain[0], true);
            trustManager.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] issuers = trustManager.getAcceptedIssuers();
        return issuers;
    }

    private void reloadTrustManager() throws Exception {

        // load keystore from specified cert store (or default)
        KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = new FileInputStream(trustStorePath);
        try {
            ts.load(in, null);
        } finally {
            in.close();
        }

        // add all temporary certs to KeyStore (ts)
        for (Certificate cert : tempCertList) {         
            ts.setCertificateEntry(UUID.randomUUID().toString(), cert);
        }

        // initialize a new TMF with the ts we just loaded
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        // acquire X509 trust manager from factory
        TrustManager tms[] = tmf.getTrustManagers();

        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {                
                trustManager = (X509TrustManager) tms[i];
                return;
            }
        }

        throw new NoSuchAlgorithmException("No X509TrustManager in TrustManagerFactory");
    }

    private void addServerCertAndReload(Certificate cert,
            boolean permanent) {
        try {
            if (permanent) {
                // import the cert into file trust store
                // Google "java keytool source" or just ...
                Runtime.getRuntime().exec("keytool -importcert ...");
            } else {
                tempCertList.add(cert);
            }
            reloadTrustManager();
        } catch (Exception ex) { /* ... */ }
    }
}
