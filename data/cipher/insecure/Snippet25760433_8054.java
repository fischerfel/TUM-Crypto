try
    {
        // Get the URL for the servlet.
        URL url = new URL(getCodeBase(), "editCriteriaServlet");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionID);

        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
        out.writeObject("Request criteria Object");
        out.flush();
        out.close();

        // Read in the search criteria object.
        ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        SealedObject sealedObject = (SealedObject)in.readObject();
        in.close();

        // Decrypt the sealed object and get the zipped data.
        SecretKey key = buildSecretKey(crypKeyString);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] baos = (byte[]) sealedObject.getObject(cipher);
        ByteArrayInputStream gis = new ByteArrayInputStream(baos);

        // Unzip and recover the original object.
        GZIPInputStream unzipped = new GZIPInputStream(gis);
        ObjectInputStream ois = new ObjectInputStream(unzipped);
        tempMultipleSlideDataObject = (MultipleSlideDataObject15) ois.readObject();            
    }
    catch (MalformedURLException ex)
    {
        errorMessage = "Submit criteria file Malformed URL." + ex.toString();
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "showErrorMessageDialog_"));
        System.out.println("Model_CriteriaInterface: loadCriteriaObject: MalformedURLException occurred");
    }
    catch (Exception e)
    {
        errorMessage = "Submit criteria file ERROR exception:" + e.toString();
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "showErrorMessageDialog_"));
        System.out.println("Model_CriteriaInterface: loadCriteriaObject: Submit criteria file ERROR exception: " + e.toString());
    }
