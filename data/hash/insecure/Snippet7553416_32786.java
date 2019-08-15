try {
        long time = System.currentTimeMillis() / 1000;
        String signatureUrl = "/WSLogin/V1/wspwtoken_login?appid=" +
                URLEncoder.encode(this.appid, "UTF-8") + "&token=" + URLEncoder.encode(this.token, "UTF-8") +
                "&ts=" + time;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        String signature = new BigInteger(1, digest.digest((signatureUrl + this.secret).getBytes())).toString(16);

        String requestUrl = "https://api.login.yahoo.com" + signatureUrl + "&sig=" + URLEncoder.encode(signature, "UTF-8");

        Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(requestUrl).openStream());
        if (response.getElementsByTagName("Error").getLength() == 0) {
            this.cookie = response.getElementsByTagName("Cookie").item(0).getTextContent();
            this.wssid = response.getElementsByTagName("WSSID").item(0).getTextContent();
        } else {
            throw new AuthException(response.getElementsByTagName("ErrorDescription").item(0).getTextContent());
        }
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    } catch (SAXException e) {
        throw new AuthException("Error parsing XML", e);
    } catch (IOException e) {
        throw new AuthException("Communication failure", e);
    } catch (ParserConfigurationException e) {
        throw new RuntimeException(e);
    }
}
