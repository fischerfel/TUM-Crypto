try {
        URL url;
        // get URL content
        url = new URL(apiURL);
        trustAllHosts();
        conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(DO_NOT_VERIFY);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("some values here");
        conn.setConnectTimeout(10000);

        in=conn.getInputStream();

        // open the stream and put it into BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        while ((line=br.readLine())!= null) {
            builder.append(line);
        }

        result=builder.toString();
        //System.out.print(result);
        br.close();


    } catch (MalformedURLException e) {
        result=null;
    } catch (java.net.SocketTimeoutException e) {
        result=null;
    } catch (IOException e) {
        result=null;
    }
    catch (Exception e) {
        result=null;
    }
    finally {
        try {
            in.close();
        }catch(Exception e){}
        try {
            conn.disconnect();
        }catch(Exception e){}

        return result;
    }
