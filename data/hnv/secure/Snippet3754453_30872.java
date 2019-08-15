SAXParserFactory spf = SAXParserFactory.newInstance();
SAXParser sp = spf.newSAXParser();
XMLReader xr = sp.getXMLReader();
GradeHandler gradeHandler = new GradeHandler();
xr.setContentHandler(gradeHandler);
URL url = new URL("https://url/to/xml/file");
HttpsURLConnection ucon = (HttpsURLConnection)url.openConnection();
ucon.setHostnameVerifier(new AllowAllHostnameVerifier());
xr.parse(new InputSource(new BufferedInputStream(ucon.getInputStream())));
