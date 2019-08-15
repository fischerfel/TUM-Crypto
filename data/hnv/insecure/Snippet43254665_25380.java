public class Frnt_mapActivity extends Activity {


JSONArray jsonarray3;
 // Google Map
private static GoogleMap googlemap;
private static String id;

private Intent intent;
HashMap<Marker, Integer> hashMap=new HashMap<Marker, Integer>();
public static LatLng latlong ;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.frnt_map_activity);


    googlemap=((MapFragment)getFragmentManager().findFragmentById(R.id.places_map)).getMap();
    googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    googlemap.setMyLocationEnabled(true);
    googlemap.getUiSettings().setZoomControlsEnabled(true);
    googlemap.getUiSettings().setMyLocationButtonEnabled(true);
    googlemap.getUiSettings().setCompassEnabled(true);
    googlemap.getUiSettings().setRotateGesturesEnabled(true);
    googlemap.getUiSettings().setZoomGesturesEnabled(true);
    googlemap.setMyLocationEnabled(true);
    LatLng coordinate = new LatLng(50.85514, 0.58382);
    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
    googlemap.animateCamera(yourLocation);

    new Frnt_mIcons_Activity().execute();
    new LocationList().execute();
    new LookingForList().execute();
    CreateProductListTask responseDownloadTask = new CreateProductListTask("https://www.towncitycards.com/webservice_action.php?action=all_products");
    responseDownloadTask.execute();
    intent=new Intent(Frnt_mapActivity.this,MainActivity.class);
    googlemap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {

            intent.putExtra("PRODUCT_ID",id);
            System.out.println(id);
            startActivity(intent);
        }
    });


protected class CreateProductListTask extends AsyncTask<Void, Void, List<Product>> {

    private String serverUrl;

    public CreateProductListTask(String url) {
        super();
        this.serverUrl = url;
    }

        @Override
    protected List<Product> doInBackground(Void... params)
    {

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("http", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


        HttpPost httpPost = new HttpPost("http://towncitycards.com/webservice_action.php?action=all_products");

        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try
        {
            URL url = new URL(this.serverUrl);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            JSONObject response = new JSONObject(stringBuffer.toString());

            List<Product> products = new ArrayList<>();
            HashMap<String, Bitmap> iconsMap = new HashMap<>();
            try {
                JSONArray productsJSON = response.getJSONArray("all_products");
                for (int ixProduct=0; ixProduct<productsJSON.length(); ixProduct++) {
                    JSONObject productJSON = productsJSON.getJSONObject(ixProduct);
                    String mapIconStr = productJSON.getString("map_icon");
                    URI uri = new URI(mapIconStr);
                    String[] segments = uri.getPath().split("/");
                    String iconName = segments[segments.length-1];

                    // percetn-encode URL
                    String mapIconPath = mapIconStr.substring(0, mapIconStr.indexOf(iconName));
                    String iconUrlString = mapIconPath + URLEncoder.encode(iconName, "UTF-8");

                    // replace "http:" with "https:"
                    iconUrlString = iconUrlString.replace("http:", "https:");

                    Bitmap bmp;
                    if (!iconsMap.containsKey(iconUrlString)) {
                        bmp = getBitmapFromURL(iconUrlString);
                        // populate map with unique icons
                        iconsMap.put(iconUrlString, bmp);
                    } else {
                        bmp = iconsMap.get(iconUrlString);
                    }

                    if (bmp != null) {
                        try {
                            Product product = new Product();
                            product.id=productJSON.getString("ID");
                            product.name = productJSON.getString("post_title");
                            product.lat = productJSON.getDouble("latitude");
                            product.lon = productJSON.getDouble("longitude");
                            id=product.id;
                            bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*3), (int)(bmp.getHeight()*3), true);
                            product.icon = bmp;
                            products.add(product);

                        } catch (Exception ignore) {
                        }
                    }

                }

            } catch (JSONException ex) {
                Log.e("App", "Failure", ex);
            }

            return products;
        }
        catch(Exception ex)
        {
            Log.e("App", "yourDataTask", ex);
            return null;
        }
        finally
        {
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(List<Product> products)
    {
        if(products != null) {
            for (final Product product : products) {

                googlemap.addMarker(new MarkerOptions()
                        .position(new LatLng(product.lat, product.lon))
                        .title(product.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(product.icon))
                );
            }
        }
    }
}

public static Bitmap getBitmapFromURL(String src) {
    try {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        bmOptions.inJustDecodeBounds = false;
        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, bmOptions);
        return myBitmap;
    } catch (IOException e) {
        return null;
    }
}

}
