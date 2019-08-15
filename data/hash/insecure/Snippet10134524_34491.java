    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progress = (ProgressBar) findViewById(R.id.progressBar1);
        text = (TextView) findViewById(R.id.tv1);
        Button btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                recursiveVideoFinder(new File("/sdcard"));
                String strDB3File = getFilesDir().getPath()
                        + "/VideoHashes.db3";
                sql3 = SQLiteDatabase.openDatabase(strDB3File, null,
                        SQLiteDatabase.CREATE_IF_NECESSARY);
                try {
                    String mysql = "CREATE TABLE videohashes (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL, hash TEXT NOT NULL, date TEXT NOT NULL, size INTEGER)";
                    sql3.execSQL(mysql);
                } catch (SQLiteException e) {
                    // TODO: handle exception
                }

                progress.setProgress(0);
                progress.setMax(myFiles.size());

                for (String path : myFiles) {
                    try {
                        String hash = getMD5Checksum(path);
                        ContentValues myInsertData = new ContentValues();
                        myInsertData.put("path", path);
                        myInsertData.put("hash", hash);
                        Date date = new Date();
                        myInsertData.put("date", dateFormat.format(date));
                        myInsertData.put("size", getFileSize(path));
                        sql3.insert("videohashes", null, myInsertData);
                        currVid++;
                        updateProgress();
                        Log.i("Progress", "CurrVid:" + currVid + "  Max:"
                                + progress.getMax());
                        text.append(currVid + " ");

                    } catch (Exception e) {
                        Log.i("File", "File not Found");
                    }
                }

                Cursor cur = sql3.query("videohashes", null, null, null, null,
                        null, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    /*
                     * ((TextView) findViewById(R.id.tv1)).append("\n" +
                     * cur.getString(1) + "\n" + cur.getString(2) + "\n" +
                     * cur.getString(3) + "\n" + cur.getString(4) + "\n");
                     */
                    cur.moveToNext();
                }
                cur.close();
            }
        });

    }

    public long getFileSize(String path) {
        File file = new File(path);
        return file.length();

    }

    private void recursiveVideoFinder(File _dir) {

        File[] files = _dir.listFiles();
        if (files == null) {
            return;
        }

        for (File currentFile : files) {
            if (currentFile.isDirectory()) {
                    recursiveVideoFinder(currentFile);
                continue;
            } else {
                for (String aEnd : getResources().getStringArray(
                        R.array.VideoExtensions)) {
                    if (currentFile.getName().endsWith(aEnd)) {

                        Log.d("PS:", currentFile.getPath().toString());

                        String videoFileName = currentFile.getPath().toString();
                        myFiles.add(videoFileName);

                    }
                }

            }
        }
    }

    // Code based off this example:
    // http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
    public static byte[] createCheckSum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        // Return MD5 Hash
        return complete.digest();
    }

    public String getMD5Checksum(String filename) throws Exception {
        byte[] b = createCheckSum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void updateProgress() {
        progress.setProgress(currVid);
        progress.invalidate();
        Log.i("Updating", "Updating Progress Bar");
    }
}
