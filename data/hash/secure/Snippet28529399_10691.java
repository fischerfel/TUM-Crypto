public String getDG() throws IOException {
    String service = "accessControl";
    try {
        JSONObject paramsMethods =
                new JSONObject().put("developerName","");
        paramsMethods.put("sg","");
        paramsMethods.put("methods","");
       paramsMethods.put("developerID","");

        JSONObject requestJson =
                new JSONObject().put("method", "actEnableMethods") //
                        .put("params", new JSONArray().put(paramsMethods)) //
                        .put("id", id()).put("version", "1.0");

        String url = findActionListUrl(service) + "/" + service;

        log("Request:  " + requestJson.toString());
        String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
        log("Response: " + responseJson);
        JSONArray resultsObj = new JSONObject(responseJson).getJSONArray("result");

        String dg = null;
        JSONObject dgobj = resultsObj.getJSONObject(0);
        dg = dgobj.getString("dg");


        return dg;

    } catch (JSONException e) {
        throw new IOException(e);
    }

}

public String getSG(String dg){
    MessageDigest md;
    String keydg = "90adc8515a40558968fe8318b5b023fdd48d3828a2dda8905f3b93a3cd8e58dc" + dg;
    try {
        md = MessageDigest.getInstance("SHA-256");
        md.update(keydg.getBytes());
        String SG = new String(Base64.encode(md.digest(), 0));

        return SG;
    }catch(Exception e){

    }
    return null;
}

public JSONObject actEnableMethods() throws IOException {
    String DG = getDG();
    String SG = getSG(DG);
    String service = "accessControl";
    try {
    JSONObject paramsMethods =
    new JSONObject().put("developerName","Sony Corporation");
    paramsMethods.put("sg",SG);
    paramsMethods.put("methods","camera/setFlashMode:camera/getFlashMode:camera/getSupportedFlashMode:camera/getAvailableFlashMode:camera/setExposureCompensation:camera/getExposureCompensation:camera/getSupportedExposureCompensation:camera/getAvailableExposureCompensation:camera/setSteadyMode:camera/getSteadyMode:camera/getSupportedSteadyMode:camera/getAvailableSteadyMode:camera/setViewAngle:camera/getViewAngle:camera/getSupportedViewAngle:camera/getAvailableViewAngle:camera/setMovieQuality:camera/getMovieQuality:camera/getSupportedMovieQuality:camera/getAvailableMovieQuality:camera/setFocusMode:camera/getFocusMode:camera/getSupportedFocusMode:camera/getAvailableFocusMode:camera/setStillSize:camera/getStillSize:camera/getSupportedStillSize:camera/getAvailableStillSize:camera/setBeepMode:camera/getBeepMode:camera/getSupportedBeepMode:camera/getAvailableBeepMode:camera/setCameraFunction:camera/getCameraFunction:camera/getSupportedCameraFunction:camera/getAvailableCameraFunction:camera/setLiveviewSize:camera/getLiveviewSize:camera/getSupportedLiveviewSize:camera/getAvailableLiveviewSize:camera/setTouchAFPosition:camera/getTouchAFPosition:camera/cancelTouchAFPosition:camera/setFNumber:camera/getFNumber:camera/getSupportedFNumber:camera/getAvailableFNumber:camera/setShutterSpeed:camera/getShutterSpeed:camera/getSupportedShutterSpeed:camera/getAvailableShutterSpeed:camera/setIsoSpeedRate:camera/getIsoSpeedRate:camera/getSupportedIsoSpeedRate:camera/getAvailableIsoSpeedRate:camera/setExposureMode:camera/getExposureMode:camera/getSupportedExposureMode:camera/getAvailableExposureMode:camera/setWhiteBalance:camera/getWhiteBalance:camera/getSupportedWhiteBalance:camera/getAvailableWhiteBalance:camera/setProgramShift:camera/getSupportedProgramShift:camera/getStorageInformation:camera/startLiveviewWithSize:camera/startIntervalStillRec:camera/stopIntervalStillRec:camera/actFormatStorage:system/setCurrentTime");
    paramsMethods.put("developerID","7DED695E-75AC-4ea9-8A85-E5F8CA0AF2F3");

        JSONObject requestJson =
                new JSONObject().put("method", "actEnableMethods") //
                        .put("params", new JSONArray().put(paramsMethods)) //
                        .put("id", id()).put("version", "1.0");
        String url = findActionListUrl(service) + "/" + service;

        log("Request:  " + requestJson.toString());
        String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
        log("Response: " + responseJson);
        return new JSONObject(responseJson);
    } catch (JSONException e) {
        throw new IOException(e);
    }
}
