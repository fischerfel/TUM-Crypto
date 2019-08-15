SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, null, new java.security.SecureRandom());

String[] scs = sc.getSocketFactory().getSupportedCipherSuites();
Arrays.sort(scs);

for(String s : scs) {
   System.out.println(s);
}
