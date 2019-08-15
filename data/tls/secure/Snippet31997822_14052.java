URL urlX = null;
HttpsURLConnection connection;
try {
    KeyStore ks = KeyStore.getInstance("PKCS12");
    FileInputStream fis = new FileInputStream(Configuracoes.CAMINHO_CERTIFICADO);
    ks.load(fis, Configuracoes.SENHA_CERTIFICADO.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, Configuracoes.SENHA_CERTIFICADO.toCharArray());
    SSLContext sc = SSLContext.getInstance("TLSv1.2");
    sc.init(kmf.getKeyManagers(), null, null);

    System.setProperty("https.protocols", "TLSv1.2");
    urlX = new URL("https://www.nfse.erechim.rs.gov.br:8182/NfseService_Homolog/NfseService_Homolog");
    connection = (HttpsURLConnection)urlX.openConnection();
    connection.setRequestMethod("POST");
    connection.setSSLSocketFactory(sc.getSocketFactory());
    connection.connect();
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String strTemp;
    while (null != (strTemp = br.readLine())) {
        System.out.println(strTemp);
    }
} catch (Exception e) {
    e.printStackTrace();
}
