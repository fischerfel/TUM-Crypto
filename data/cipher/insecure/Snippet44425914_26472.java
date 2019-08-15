@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expListView = (ExpandableListView) findViewById(R.id.rules);

        prepareListData();

        listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TextView az = (TextView) v.findViewById(R.id.l_child);
                String link = az.getText().toString() + ".pdf";
                PATH = Environment.getExternalStorageDirectory() + "/BKI Rules/" + link;
                File filecheck = new File(PATH);
                if (filecheck.exists()) {

                    fileList.add(filecheck);
                    Intent intent = new Intent(getApplicationContext(), PDFreader.class);
                    intent.putExtra("nameOfFile",link);
                    intent.putExtra("position", PATH);
                    startActivity(intent);

                } else {
                    String link1 = link.replace(" ", "%20");
                    String linkdownload = myHTTPUrl + link1;

                    new DownloadFileFromURL().execute(linkdownload);

        });
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                String link = url.toString();
                URLConnection conection = url.openConnection();
                conection.connect();

                try {
                    String nameOfFile = URLUtil.guessFileName(link, null, MimeTypeMap.getFileExtensionFromUrl(link));

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream
                    FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
                            + "BKI Rules" + "/" + nameOfFile);

                    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");

                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.ENCRYPT_MODE, sks);

                    CipherOutputStream cos = new CipherOutputStream(output, cipher);

                    int b;
                    byte[] d = new byte[8];
                    while ((b = input.read(d)) != -1) {
                        cos.write(d, 0, b);
                    }
                    // Flush and close streams.
                    cos.flush();
                    cos.close();
                    input.close();

                }catch (InvalidKeyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
    }