public class MyHttpsClient extends DefaultHttpClient{

final Context context;

public MyHttpsClient(Context context) {
    System.out.println("context client http");
    this.context = context;
}

@Override
protected ClientConnectionManager createClientConnectionManager(){
    SchemeRegistry registry = new SchemeRegistry();
    System.out.println("schema");
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", newSslSocketFactory(), 8080));
    return new SingleClientConnManager(getParams(), registry);
}

private SSLSocketFactory newSslSocketFactory(){
    try{
        KeyStore trusted = KeyStore.getInstance("BKS");
        System.out.println("context client getInstance..");
        InputStream in = context.getResources().openRawResource(R.raw.server);
        System.out.println("context client input" + in);
        try{
        System.out.println("try");
            trusted.load(in, "changeit".toCharArray());
        }finally{
            in.close();
        }
        System.out.println("finally");
        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return sf;
    }catch(Exception e){
        throw new AssertionError(e);
    }
}
}
