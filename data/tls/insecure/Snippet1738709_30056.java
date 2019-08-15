import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.*;
import javax.security.auth.x500.X500Principal;

public class CertChecker implements X509TrustManager {

    private final X509TrustManager defaultTM;

    public CertChecker() throws GeneralSecurityException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore)null);
        defaultTM = (X509TrustManager) tmf.getTrustManagers()[0];
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        if (defaultTM != null) {
            try {
                defaultTM.checkServerTrusted(certs, authType);
                if (isEVCertificate(certs))
                    System.out.println("EV Certificate: "+ certs[0].getSubjectX500Principal().getName() + " issued by " + certs[0].getIssuerX500Principal().getName());                    
                System.out.println("Certificate valid");
            } catch (CertificateException ex) {
                System.out.println("Certificate invalid: " + ex.getMessage());
            }
        }
    }

    private boolean isEVCertificate(X509Certificate[] certs) {
        try {
            // load keystore with trusted CA certificates
            KeyStore cacerts = KeyStore.getInstance("JKS");
            cacerts.load(new FileInputStream(new File(System.getProperty("java.home"), "lib/security/cacerts")), null);

            // build a cert selector that selects the first certificate of the certificate chain
            // TODO we should verify this against the hostname...
            X509CertSelector targetConstraints = new X509CertSelector();
            targetConstraints.setSubject(certs[0].getSubjectX500Principal());

            // build a cert path from our selected cert to a CA cert
            PKIXBuilderParameters params = new PKIXBuilderParameters(cacerts, targetConstraints);        
            params.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(certs))));
            params.setRevocationEnabled(false);
            CertPath cp = CertPathBuilder.getInstance("PKIX").build(params).getCertPath();

            // validate the cert path
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) CertPathValidator.getInstance("PKIX").validate(cp, params);
            return isEV(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    public X509Certificate[] getAcceptedIssuers() { return null;}

    public static void main(String[] args) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] {new CertChecker()}, new SecureRandom());
        SSLSocketFactory ssf = (SSLSocketFactory) sc.getSocketFactory();
        ((SSLSocket)ssf.createSocket(args[0], 443)).startHandshake();
    }

    private static final Map<X500Principal, String> policies = new HashMap<X500Principal, String>();

    static {
        // It would make sense to populate this map from Properties loaded through 
        // Class.getResourceAsStream().
        policies.put(
                new X500Principal("OU=Class 3 Public Primary Certification Authority,O=VeriSign\\, Inc.,C=US"), 
                "2.16.840.1.113733.1.7.23.6"
        );
        // TODO add more certificates here
    }

    // based on http://stackoverflow.com/questions/1694466/1694720#1694720
    static boolean isEV(PKIXCertPathValidatorResult result)
    {
        // Determine the policy to look for.
        X500Principal root = result.getTrustAnchor().getTrustedCert().getSubjectX500Principal();
        System.out.println("[Debug] Found root DN: "+root.getName());
        String policy = policies.get(root);
        if (policy != null)
            System.out.println("[Debug] EV Policy should be: "+policy);

        // Traverse the tree, looking at its "leaves" to see if the end-entity 
        // certificate was issued under the corresponding EV policy.
        PolicyNode tree = result.getPolicyTree();
        if (tree == null)
            return false;
        Deque<PolicyNode> stack = new ArrayDeque<PolicyNode>();
        stack.push(tree);
        while (!stack.isEmpty()) {
            PolicyNode current = stack.pop();
            Iterator<? extends PolicyNode> children = current.getChildren();
            int leaf = stack.size();
            while (children.hasNext())
                stack.push(children.next());
            if (stack.size() == leaf) {
                System.out.println("[Debug] Found policy: " + current.getValidPolicy());
                // If the stack didn't grow, there were no "children". I.e., the 
                // current node is a "leaf" node of the policy tree.
                if (current.getValidPolicy().equals(policy))
                    return true;
            }
        }
        // The certificate wasn't issued under the authority's EV policy.
        return false;
    }
}
