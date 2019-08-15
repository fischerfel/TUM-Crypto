 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        value1 = (EditText) findViewById(R.id.value1);
        value2 = (EditText) findViewById(R.id.value2);

        computeSha = (Button) findViewById(R.id.btn1);

        result = (TextView) findViewById(R.id.textView2);

        //get username and password entered
        username = userName.getText().toString();
        passwd = passWord.getText().toString();

        //check if username or passwd is not null

        if ((username != null && username.equals("")) || (passwd != null && passwd.equals(""))) {

            computeSha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //call method to compute SHA hash
                    computeSHAHash(value1);
                    computeSHAHash(value2);
                    if (computeSHAHash(value1).equals(computeSHAHash(value2))) {
                        Toast.makeText(MainActivity.this, "matched", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private String convertToHex(byte[] data) throws java.io.IOException
    {

        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }


    public String computeSHAHash(String password)
    {
        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash=convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result.setText("SHA-1 hash generated is: " + " " + SHAHash);
        return SHAHash;
    }   
