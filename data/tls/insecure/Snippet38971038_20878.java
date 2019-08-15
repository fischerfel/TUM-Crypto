public class RouteAssistantActivity extends Activity implements OnMapReadyCallback{

public GoogleMapsDirectionsResponse dirRes;
public GoogleMapsDistanceResponse disRes;

public String jsonString;
private String mapsAPIKey;
private String directionsBaseURL;
private String distanceBaseURL;

MapFragment mapFragment;
private ProgressDialog progress;

public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ra_route_assisstant);

    mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ra_map);
    progress = new ProgressDialog(RouteAssistantActivity.this);
    progress.setTitle("Please Wait");
    progress.setMessage("Retrieving Data from the Server");
    progress.setIndeterminate(true);

    try {
        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

        if (appInfo.metaData != null) {
            mapsAPIKey = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
            directionsBaseURL = appInfo.metaData.getString("com.google.android.maps.directions.baseURL");
            distanceBaseURL = appInfo.metaData.getString("com.google.android.maps.distance.baseURL");
        }

    } catch (PackageManager.NameNotFoundException e) {
        Log.e("Meta Error", "Meta Data not found. Please check the Manifest and the Meta Data Package Names");
        e.printStackTrace();
    }

    //Test
    String directionsURL = directionsBaseURL+"origin=6.948109,79.858191&destination=6.910176,79.894347&key="+mapsAPIKey;
    String distanceURL = distanceBaseURL+"units=metric&origins=6.948109,79.858191&destinations=6.910176,79.894347&key="+mapsAPIKey;

    Log.e("CA Debug","URL : " + directionsURL);
    Log.e("CA Debug","URL : " + distanceURL);

    new configurationSyncTask().execute(distanceURL,"distance");
    new configurationSyncTask().execute(directionsURL, "direction");

    mapFragment.getMapAsync(this);

}

@Override
public void onMapReady(GoogleMap googleMap) {
    LatLng rajagiriya = new LatLng(6.910176, 79.894347);

    String points = dirRes.getRoutes().get(0).getOverviewPolyline();
    List<LatLng> list = PolyUtil.decode(points);

    googleMap.setMyLocationEnabled(true);
    googleMap.getUiSettings().setRotateGesturesEnabled(true);
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rajagiriya, 13));

    googleMap.addMarker(new MarkerOptions()
            .title("Rajagiriya")
            .snippet("My Place")
            .position(rajagiriya));

    googleMap.addPolyline(new PolylineOptions()
            .geodesic(false)
            .addAll(list)
            .color(Color.RED)
            .width(25));
}

private class configurationSyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        String type = params[1];

        Log.d("CA Debug", getClass().getSimpleName() + " --> Real URL : " + url);
        Log.d("CA Debug", getClass().getSimpleName() + " --> doInBackground requesting content");

        jsonString = requestContent(url);

        // if the output is null, stop the current task
        if (jsonString == null) {
            Log.d("CA Debug", getClass().getSimpleName() + " --> Stopping Async Task");
            this.cancel(true);
            Log.d("CA Debug", getClass().getSimpleName() + " --> Async Task Stopped");
        }

        return type;
    }

    @Override
    protected void onPostExecute(String types) {

        if (types.equalsIgnoreCase("distance")) {
            disRes = GMapsDistanceResponseJSONDeserializer.deserialize(jsonString);
        } if (types.equalsIgnoreCase("directions")) {
            dirRes = GMapsDirectionsResponseJSONDeserializer.deserialize(jsonString);
        }

        progress.dismiss();
    }


}

public String requestContent(String url) {

    Log.d("CA Debug",getClass().getSimpleName()+" --> URL : "+url);

    try {
        URL urlObj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
        con.setChunkedStreamingMode(0);
        con.setRequestMethod("GET");

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null,null, new SecureRandom());
        con.setSSLSocketFactory(sc.getSocketFactory());

        InputStream clientResponse;
        String jsonString;
        int status = con.getResponseCode();

        if(status >= HttpURLConnection.HTTP_BAD_REQUEST){
            Log.d("CA Debug", getClass().getSimpleName()+" --> Bad Request");
            jsonString = null;
        } else {
            Log.d("CA Debug", getClass().getSimpleName()+" --> converting Stream To String");
            clientResponse = con.getInputStream();
            jsonString = convertStreamToString(clientResponse);
        }

        Log.d("CA Debug", getClass().getSimpleName()+" --> JSON STRING : " + jsonString);

        return jsonString;
    } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {

        Log.d("CA Debug", getClass().getSimpleName()+" --> Error when creating an Input Stream");
        e.printStackTrace();

    }
    return null;
}

public String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;

    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
    } catch (IOException e) {
    } finally {
        try {
            is.close();
        } catch (IOException e) {
        }
    }

    return sb.toString();
}

}
