public class MainActivity extends Activity
{
private ListView mList;
private ArrayList<String> arrayList;
private MyCustomAdapter mAdapter;
private TCPClient mTcpClient;

@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    arrayList = new ArrayList<String>();

    final EditText editText = (EditText) findViewById(R.id.editText);
    Button send = (Button)findViewById(R.id.send_button);

    //relate the listView from java to the one created in xml
    mList = (ListView)findViewById(R.id.list);
    mAdapter = new MyCustomAdapter(this, arrayList);
    mList.setAdapter(mAdapter);

    // connect to the server
    new connectTask().execute("");

    send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //String message = editText.getText().toString();
            String message = "mall: ABC Mall date: 08/08/2013 time: 13:45 md5 f6151b2cdc256be4971bcc30629bbc7\r\n";
           /* String str =md5Digest(message);
            String s=message+str;*/

            //add the text in the arrayList
            arrayList.add("c: " + message);

            //sends the message to the server
            if (mTcpClient != null) {


                mTcpClient.sendMessage(message);

                /*mTcpClient.sendMessage(s);*/
            }

            //refresh the list
            mAdapter.notifyDataSetChanged();
            editText.setText("");
        }
    });

}


public class connectTask extends AsyncTask<String,String,TCPClient> {

    @Override
    protected TCPClient doInBackground(String... message) {

        //we create a TCPClient object and
        mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                publishProgress(message);
            }
        });
        mTcpClient.run();

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        //in the arrayList we add the messaged received from server
        arrayList.add(values[0]);
        // notify the adapter that the data set has changed. This means that new message received
        // from server was added to the list
        mAdapter.notifyDataSetChanged();
    }
}
public static final String md5Digest(final String text)
{
     try
     {
           // Create MD5 Hash
           MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
           digest.update(text.getBytes());
           byte messageDigest[] = digest.digest();

           // Create Hex String
           StringBuffer hexString = new StringBuffer();
           int messageDigestLenght = messageDigest.length;
           for (int i = 0; i < messageDigestLenght; i++)
           {
                String hashedData = Integer.toHexString(0xFF & messageDigest[i]);
                while (hashedData.length() < 2)
                     hashedData = "0" + hashedData;
                hexString.append(hashedData);
           }
           return hexString.toString();

     } catch (NoSuchAlgorithmException e)
     {
           e.printStackTrace();
     }
     return ""; // if text is null then return nothing
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}
}
