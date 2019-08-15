public class Frnt_mapActivity extends Activity {

public String Shop_title;
public String Shop_address;
public String Shop_icons;
ProgressDialog mDialog;
JSONObject jsonobject3;
JSONArray jsonarray3;
// Google Map
private static GoogleMap googlemap;
public static ArrayList<SearchBeams> searchdata_list;
public static ArrayList<NormalSearchBeams> normlSearchList;
ArrayList<All_products_lat_long> venueList;

private int i;
private Intent intent;
HashMap<Marker, Integer> hashMap=new HashMap<Marker, Integer>();
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
    intent=new Intent(Frnt_mapActivity.this,MainActivity.class);
    googlemap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {
            i=hashMap.get(arg0);
            intent.putExtra("PRODUCT_ID", venueList.get(i).getId());
            startActivity(intent);
            //overridePendingTransition( R.anim.slide_right, R.anim.slide_left );
        }
    });


    new Frnt_mIcons_Activity().execute();


}

class AddMarkerAsyncTask extends AsyncTask<String,String,String>{

    private All_products_lat_long venue;
    private int k;
    BitmapDescriptor bd=null;

    AddMarkerAsyncTask(All_products_lat_long venue,int k){
        this.venue=venue;
        this.k=k;
    }




    @Override
    protected String doInBackground(String... strings) {
        try{
            bd=BitmapDescriptorFactory.fromBitmap(Glide.
                    with(Frnt_mapActivity.this).
                    load(venue.getMap_icon()).
                    asBitmap().
                    into(100, 100). // Width and height
                    get());
        }catch (Exception e){
            bd=null;
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            if(bd!=null){
                Marker marker=googlemap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(venue.getLatitude()), Double.parseDouble(venue.getLongitude())))
                        .title(venue.getPost_title())
                        .icon(bd));
                hashMap.put(marker, k);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}


private void addMarkers() {




    for(int k=0; k<venueList.size(); k += 1){

        //nirmal
        try{
            Log.e("nirmal",venueList.get(k).getMap_icon());

            //new AddMarkerAsyncTask(venueList.get(k),k).execute();

            double ln = Double.parseDouble(venueList.get(k).getLongitude());
            double la = Double.parseDouble(venueList.get(k).getLatitude());
                /*String title=venueList.get(k).getPost_title()==null?"":venueList.get(k).getPost_title();
                URL url = new URL(venueList.get(k).getMap_icon());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                BitmapDescriptor bd=BitmapDescriptorFactory.fromBitmap(image);*/


                /*Marker marker=googlemap.addMarker(new MarkerOptions()
                        .position(new LatLng(la, ln))
                        .title(title)
                        .icon(bd));
                hashMap.put(marker, k);*/

            switch (Integer.parseInt(venueList.get(k).getId()))
            {
                case 5689 :
                    Marker marker=googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.fruitandveg)));
                    hashMap.put(marker, k);
                    break;
                case 5779 :
                    Marker marker1=googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee)));
                    hashMap.put(marker1, k);
                    break;
                case 5798 :
                    Marker marker3 = googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.takeaway)));
                    hashMap.put(marker3, k);
                    break;
                case 5810 :
                    Marker marker4 = googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_steakhouse)));
                    hashMap.put(marker4, k);
                    break;
                case 6005 :
                    Marker marker5 = googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.clambers_playcentre)));
                    hashMap.put(marker5, k);
                    break;
                case 6011 :
                    Marker marker6 = googlemap.addMarker(new MarkerOptions()
                            .position(new LatLng(la, ln))
                            .title(venueList.get(k).getPost_title())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.thetruecrimemuseum)));
                    hashMap.put(marker6, k);

                    break;
                .........
                ...........
                ............
                default :
                    break;
            }

        }catch (Exception e){
            Log.e("addMarkers",k+"");
            e.printStackTrace();
        }


    }
}

/******************FOR MAP******************/
class Frnt_mIcons_Activity extends AsyncTask<String, String, String> {

    public ArrayList<All_products_lat_long> maplist;


    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(Frnt_mapActivity.this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
        super.onPreExecute();
    }




    // Slow Implementation
    private String inputStreamToString(InputStream is) {
        String s = "";
        String line = "";
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Return full string
        return s;
    }


    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub  
        maplist = new ArrayList<All_products_lat_long>();

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

        try{

            HttpResponse response = httpClient.execute(httpPost);
            String data = inputStreamToString(response.getEntity().getContent());
            jsonobject3 = new JSONObject(data);
            jsonarray3 = new JSONArray(jsonobject3.getString("all_products"));
            venueList =new ArrayList<All_products_lat_long>();
            for(int j=0; j<jsonarray3.length();j++){
                JSONObject itemobj = jsonarray3.getJSONObject(j);
                //SHOW SEARCH RESULT
                All_products_lat_long searchItems = new All_products_lat_long();

                searchItems.setId(itemobj.getString("ID"));
                searchItems.setPost_title(itemobj.getString("post_title"));
                searchItems.setMap_icon(itemobj.getString("map_icon"));
                searchItems.setLongitude(itemobj.getString("longitude"));
                searchItems.setLatitude(itemobj.getString("latitude"));
                //          System.out.println("######Ashish is object >>"+itemobj);
                venueList.add(searchItems);
                //      System.out.println("SEARCHLISTSS@@@@@@@"+venueList.get(j).getLongitude());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);

        //System.out.println("SEARCHLISTSS@@@@@@@"+venueList);
        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA"+venueList);
        try{
            if(venueList.size()>0)
            {
                addMarkers();
//              Toast.makeText(Frnt_mapActivity.this,"Search for the venue11.",Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(Frnt_mapActivity.this,"Search for the venue.",Toast.LENGTH_SHORT).show();
            //LatLng coordinate = new LatLng(22.75852160, 75.8911550);
            LatLng coordinate = new LatLng(50.85514, 0.58382);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
            googlemap.animateCamera(yourLocation);
            for (int i = 0; i < jsonarray3.length(); i++) {

                jsonobject3 = jsonarray3.getJSONObject(i);

                Shop_title = jsonobject3.optString("post_title");
                Shop_address = jsonobject3.optString("map_icon");
                Shop_icons = jsonobject3.optString("map_icon");
                //}
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        if(mDialog!=null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
}
