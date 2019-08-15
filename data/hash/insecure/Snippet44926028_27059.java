public void getImageMD5(){
    driver.get("https://www.blognone.com/");        
    WebElement img = driver.findElement(By.cssSelector("img"));
    String imgUrl = img.getAttribute("src").trim();
    String script = "var callback = arguments[arguments.length - 1];"
                + "function _arrayBufferToBase64( buffer ) {"
                + "    var binary = '';"
                + "    var bytes = new Uint8Array( buffer );"
                + "    var len = bytes.byteLength;"
                + "    for (var i = 0; i < len; i++) {"
                + "        binary += String.fromCharCode( bytes[ i ] );"
                + "    }"
                + "    return window.btoa( binary );"
                + "}"
                + " fetch(' " + imgUrl + " ',{cache:'force-cache'})."
                + "then((response)=>{return response.arrayBuffer()})."
                + "then((response)=>{return _arrayBufferToBase64(response)})."
                + "then((response)=>{callback(response)});";
    driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
    Object response = ((JavascriptExecutor) driver).executeAsyncScript(script, imgUrl);
            byte[] data = Base64.getDecoder().decode((String) response);
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hash = md.digest(data);

    StringBuilder sb = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
        sb.append(String.format("%02x", b & 0xff));
    }

    String digest = sb.toString();
    System.out.println("MD5 of Image : " + digest);
}
