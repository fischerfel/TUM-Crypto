public class MyHttpClient extends AsyncTask<String, Void, String> {

private String username;
private String password;

//this is used if you need a password and username
//mainly for logins to a webserver
public MyHttpClient(String username, String password)
{
    this.username = username;
    this.password = password;
}

//used for image downloading
public MyHttpClient(){}



@Override
protected String doInBackground(String... params) {

    String url = params[0];
    return httpDownloadData(url);

}


//this is used for a simple http download of json files or xml file
//it must return a string fom the http request
private String httpDownloadData(String myUrl)
{
    String respone = null;
    HttpURLConnection urlConnection = null;

    try
    {
        URL url = new URL(myUrl);
        //put in the username and pssword for parmas to send to url
        //this is good for login
        if (username!=null)
        {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }

        urlConnection = (HttpURLConnection)url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        if(inputStream != null)
        {
            respone = streamToString(inputStream);
            inputStream.close();
        }

    }catch (IOException ie)
    {
        Log.d("IOExceptrion:", "In http downloader");
        ie.printStackTrace();
    }finally {
        if(urlConnection != null)
        {
            urlConnection.disconnect();
        }
    }
    return respone;
}

//this is to download images from HTTP connections
private Bitmap httpBitmapDownloader(String myUrl)
{
    HttpURLConnection urlConnection = null;

    try {
        URL url = new URL(myUrl);
        urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        if (statusCode != 200)
        {
            return null;
        }

        InputStream inputStream = urlConnection.getInputStream();
        if(inputStream != null)
        {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }


    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        if (urlConnection != null)
        {
            urlConnection.disconnect();
        }

    }

    return null;
}



//download strings via https
//Todo Add the certificate handler so turst server
// TODO: 4/4/16 BKS needed for this
private String httpsDownloadData(String myUrl)
{

    String respone = null;
    HttpsURLConnection urlConnection = null;
    //get the cert handler

    try
    {
        KeyStore keyStore = KeyStore.getInstance("");
        String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        URL url = new URL(myUrl);
        if (username!=null)
        {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
       //urlConnection.setSSLSocketFactory(context.getSocketFactory());

        int statusCode = urlConnection.getResponseCode();
        Log.d("Status code: " , Integer.toString(statusCode));


        InputStream inputStream = urlConnection.getInputStream();
        if(inputStream != null)
        {
            respone = streamToString(inputStream);
            inputStream.close();
        }


    }catch (IOException e)
    {
        Log.d("downloading data: " , "in https webape");
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        Log.d("Trustmanager issue:" , "Error");
        e.printStackTrace();
    } catch (KeyStoreException e) {
        Log.d("Keystore issues:", "Key needs att");
        e.printStackTrace();
    } catch (KeyManagementException e) {
        Log.d("Key management:" , "Key issue");
        e.printStackTrace();
    }

    return respone;

}

private Bitmap httpsBitmapDownloader(String myUrl)
{
    HttpsURLConnection urlConnection = null;

    try
    {
        KeyStore keyStore = KeyStore.getInstance("");
        String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        URL url = new URL(myUrl);
        urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        int statusCode = urlConnection.getResponseCode();
        if(statusCode != 200){return null;}

        InputStream inputStream = urlConnection.getInputStream();
        if(inputStream != null)
        {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        Log.d("bitmap download: " , "Https issues");
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } finally {
        if (urlConnection != null)
        {
            urlConnection.disconnect();
        }
    }
    return null;
}

//this is used for downloading strings from an http or https connection
private String streamToString(InputStream is) throws IOException {

    StringBuilder sb = new StringBuilder();
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    //add a fake parent to the line json data

    String line;
    while ((line = rd.readLine()) != null) {
        sb.append(line);
    }

    return sb.toString();
}
