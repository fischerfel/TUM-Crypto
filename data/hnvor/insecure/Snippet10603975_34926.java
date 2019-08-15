<imports go here>
public class HttpPoster
{
    public static final String s_target = "https://<url goes here>";
    private static final String s_parameters = "a=100&b=200";

    public void runTest()
    {
        HttpURLConnection connection = createHttpConnection(s_target);

        String response = null;
        int     statusCode = -1;
        String responseMessage = null;

        try
        {
            response = sendRequest(connection, s_parameters);
            statusCode = connection.getResponseCode();
            responseMessage = connection.getResponseMessage();

            System.out.println("Response = " +response);
            System.out.println("Status Code = " +statusCode);
            System.out.println("Response Message = " +responseMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }
    }

    private String sendRequest(HttpURLConnection connection, String parameters)
    {
        StringBuffer buffer = new StringBuffer();

        try
        {
            // Calling getOutputStream() does an implicit connect()
            OutputStreamWriter outputWriter = new OutputStreamWriter(connection.getOutputStream());

            outputWriter.write(parameters);
            outputWriter.flush();

            // Get the response
            String line = null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((line = bufferedReader.readLine()) != null)
            {
                buffer.append(line);
            }

            if (outputWriter != null) {
                outputWriter.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        return buffer.toString();
    }

    private HttpURLConnection createHttpConnection(String url)
    {
        HttpURLConnection connection = null;

        try
        {
            connection = (HttpURLConnection)(new URL(url)).openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);

            setHostNameVerifier(connection);

        }
        catch (MalformedURLException murlException)
        {
            murlException.printStackTrace();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

        return connection;
    }

    private void setHostNameVerifier(HttpURLConnection c)
    {
        HostnameVerifier hv = null;

        if (c instanceof HttpsURLConnection)
        {
            hv = new HostnameVerifier()
            {
                public boolean verify(String urlHostName, SSLSession session)
                {
                    // Ignore the domain mismatch
                    return true;
                }
             };

            ((HttpsURLConnection)c).setHostnameVerifier(hv);
        }
    }

    public static void main(String[] args)
    {
            new HttpPoster().runTest();
    }
}
