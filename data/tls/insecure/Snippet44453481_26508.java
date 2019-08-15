public class MainActivity extends ActionBarActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new 
 PlaceholderFragment()).commit();
    }
    Thread thread =  new Thread()
    {
        @Override
            public void run(){
            char[] keyPassphrase = "MySecretPassword".toCharArray();
            KeyStore ks = null;
            try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream("/sdcard2/keycert.p12"), keyPassphrase);

            KeyManagerFactory kmf =  KeyManagerFactory.getInstance("X509");
            kmf.init(ks, keyPassphrase);

            char[] trustPassphrase = "MySecretPassword".toCharArray();              
            KeyStore tks =  KeyStore.getInstance("BKS");
            tks.load(new FileInputStream("/sdcard2/clienttruststore.bks"), trustPassphrase);
            TrustManagerFactory tmf =  TrustManagerFactory.getInstance("X509");
            tmf.init(tks);

            SSLContext c = SSLContext.getInstance("TLS");
            c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.10.1");
            factory.setPort(5671);        
            factory.setVirtualHost("/");
            factory.setUsername("admin");
            factory.setPassword("123456");
            factory.useSslProtocol(c);
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
         channel.queueDeclare("rabbitmq-java-test", false, true, true, null);

            channel.basicPublish("", "rabbitmq-java-test", null, "Hello, World".getBytes());


            GetResponse chResponse = null;
                chResponse = channel.basicGet("rabbitmq-java-test", false);

            if(chResponse == null) {
                System.out.println("No message retrieved");
            } else {
                byte[] body = chResponse.getBody();
                System.out.println("Recieved: " + new String(body));
            }
            System.out.println("message 8");
            System.out.println("## message retrieved");
            System.out.println("## message retrieved");
            }catch(IOException e){
                e.printStackTrace();
                //e.getCause().getMessage();
                Log.i("test","IO error message");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();Log.i("test","message4");
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();Log.i("test","message5");
            } catch (CertificateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();Log.i("test","message6");
            } catch (UnrecoverableKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();Log.i("test","message7");
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();Log.i("test","message8");
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }             
    };    
    thread.start();     
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
}

/**
 * A placeholder fragment containing a simple view.
 */
public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container,
                false);
        return rootView;
    }
  }

}
