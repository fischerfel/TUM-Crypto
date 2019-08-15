public static InputStream multipartPOST(String urlStr, 
            Hashtable<String, String> stringParams, 
            Hashtable<String, File> fileParams) throws Exception {URL connectURL = new URL(urlStr);

            conn = (HttpURLConnection)connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //conn.setChunkedStreamingMode(1000*1024);
            int timeout = 10000; 
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "RRE");
            conn.setRequestProperty("Connection","Keep-Alive");
            conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            conn.setRequestProperty("Accept-Encoding","gzip");
            conn.connect();

            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(Constants.SALT1.getBytes());

            DataOutputStream dataStream = new DataOutputStream(conn.getOutputStream());
            //DataOutputStream dataStream =new DataOutputStream(new BufferedOutputStream(conn.getOutputStream(), 128000));


            ArrayList<String> stringParamsKeys = Collections.list(stringParams.keys());
            Collections.sort(stringParamsKeys);
            for (String stringParamsKey : stringParamsKeys) {
                String stringParamsValue = stringParams.get(stringParamsKey);
                if(stringParamsValue != null) {
                    writeFormField(dataStream, stringParamsKey, stringParamsValue, digester);
                }
            }

            if(fileParams != null) {
                ArrayList<String> fileParamsKeys = Collections.list(fileParams.keys());
                Collections.sort(fileParamsKeys);
                for (String fileParamsKey : fileParamsKeys) {
                    File fileParamsValue = fileParams.get(fileParamsKey);

                    FileInputStream fileInputStream = new FileInputStream(fileParamsValue);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                    writeFileField(dataStream, fileParamsKey, fileParamsValue.getName(), "", bufferedInputStream, 
                            fileParamsValue.length(), digester);

                    // final closing boundary line
                    dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);           
                    bufferedInputStream.close();
                    fileInputStream.close();
                }
            }
            System.out.println(dataStream);
            byte[] messageDigest = digester.digest();
            String md5Hash = Utils.byteArrayToHexString(messageDigest);
            writeFormField(dataStream, "md5Hash", md5Hash, null);

            dataStream.flush();
            dataStream.close();
            dataStream = null;
            try
            {
                InputStream in=conn.getInputStream();

            }
            catch (Exception e) {
                // TODO: handle exception
                Log.d("logexception",e.toString());
            }
            InputStream inputStream = conn.getInputStream();
            System.out.println(inputStream);
            try
            {
                 String contentEncoding = conn.getContentEncoding();
                 Log.d("logexceptio",contentEncoding.toString());
            }
            catch (Exception e) {
                // TODO: handle exception
                Log.d("logexceptionhhhhhhhh",e.toString());
            }
            Log.d("loghi","hiiiiii");
            String contentEncoding = conn.getContentEncoding();

            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                System.out.println("gggzzziiippppppp");
                inputStream = new GZIPInputStream(inputStream);
            }

            return inputStream;
            }
