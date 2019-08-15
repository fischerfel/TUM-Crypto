//I do this three times cause each keystore will have one different
//certificate   
String keyStoreType = KeyStore.getDefaultType();
try {
keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", InterfaceClass.certificate);

//creating a trustmanagerfactory
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = null;
try {
tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//initializing the keystore with the tmf
try {
tmf.init(keyStore); //do this three times for keystore1, keystore2
TrustManager[] trust = tmf.getTrustManagers();
int size = trust.length;
Log.d("trusti","first: "+size);// i expect to get size = 3 but size =1 WHY?
//CREATING AN SSLCONTEXT FOR MY SMACK CONFIGURATIONBUILDER OBJECT
SSLContext context = null;
try {
context = SSLContext.getInstance("TLS");
