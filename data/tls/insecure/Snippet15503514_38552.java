// Configure the SunPkcs11 provider
String pkcs11config;
pkcs11config = "name = Cryptoki";
pkcs11config += "\nlibrary = /SCDriver/libbit4ipki.dylib";
InputStream confStream = new ByteArrayInputStream(pkcs11config.getBytes());
SunPKCS11 sunpkcs11 = new SunPKCS11(confStream);
Security.addProvider(sunpkcs11);

// Specify keystore builder parameters for PKCS#11 keystores
Builder scBuilder = Builder.newInstance("PKCS11", sunpkcs11, new KeyStore.CallbackHandlerProtection(new PasswordRetriever()));

// Create and init KeyManagerFactory
KeyManagerFactory factory = KeyManagerFactory.getInstance("NewSunX509");
factory.init(new KeyStoreBuilderParameters(scBuilder));

// create and init ssl context
m_ssl_context = SSLContext.getInstance("TLS");
m_ssl_context.init(factory.getKeyManagers(), new TrustManager[] {new PkTrustManager()}, null);      
SSLContext.setDefault(m_ssl_context);
