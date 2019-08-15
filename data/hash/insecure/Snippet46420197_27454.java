public class Home_fragment extends Fragment implements MyRecylerEventListener {

    RecyclerView recylerView,spotlight_recyclerview,mostViews_recyclerview,recentViews_recyclerview;
    Context context;
    ArrayList<CompaignDetails> arrayList = new ArrayList<>();
    ArrayList<CompaignDetails> mostViewsArrayList = new ArrayList<>();
    ArrayList<CompaignDetails> recentViewsArrayList = new ArrayList<>();
    ArrayList<CompaignDetails> spotlightArrayList = new ArrayList<>();
    List<Objects> newSpotlightArray = new ArrayList<>();

    // List of Native Express ads and MenuItems that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();
    private List<Object> adds = new ArrayList<>();
    private List<Object> myItems = new ArrayList<>();

    private CompaignsAdapter adapter;
    private SpotlightCampaignAdpater spotlightAdapter;
    private SpotlightAdapter newSpotlightAdapter;
    private RecentViewsAdapter recentViewAdapter;
    private MoreViewsAdapter moreViewsAdapter;
    private AdView mAdView;
    private ImageView imgLine1,imgLine2,imgLine3,imgLine4,imgLine5,imgLine6;
    private LinearLayout llRoot;
    MainDashboardActivity activity;
    public Home_fragment() {
        // Required empty public constructor
    }

    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 2;

    // The Native Express ad height.
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 180;

    // The Native Express ad unit ID.
    private static final String AD_UNIT_ID = "ca-app-pub-357743493454351/4564545454";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_new_layout, container, false);

        mostViews_recyclerview = (RecyclerView) view.findViewById(R.id.mostViews_recyclerview);
        spotlight_recyclerview = (RecyclerView) view.findViewById(R.id.spotlight_recyclerview);
        recentViews_recyclerview= (RecyclerView) view.findViewById(R.id.recentViews_recyclerview);
        imgLine1 = (ImageView)view.findViewById(R.id.imgLine1);
        imgLine2 = (ImageView)view.findViewById(R.id.imgLine2);
        imgLine3 = (ImageView)view.findViewById(R.id.imgLine3);
        imgLine4 = (ImageView)view.findViewById(R.id.imgLine4);
        imgLine5 = (ImageView)view.findViewById(R.id.imgLine5);
        imgLine6 = (ImageView)view.findViewById(R.id.imgLine6);

        imgLine1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imgLine2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        imgLine3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imgLine4.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        imgLine5.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imgLine6.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        context = getActivity();

        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);



        final LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager linearLayoutManager1
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager linearLayoutManager2
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        spotlight_recyclerview.setLayoutManager(linearLayoutManager);
        mostViews_recyclerview.setLayoutManager(linearLayoutManager1);
        recentViews_recyclerview.setLayoutManager(linearLayoutManager2);



        getCampaigns();




        final FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if(MyApplication.refreshCampaigns){
            MyApplication.refreshCampaigns = false;
            getCampaigns();

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (MainDashboardActivity)activity;
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }





    private void parseResponse() {

        try {


           String jsonDataString = readJsonDataFromFile();
            JSONArray menuItemsJsonArray = new JSONArray(jsonDataString);
            mRecyclerViewItems.clear();

            if (!response.isNull("CAMPAIGN_SPOT_LIGHT_VIEWS")) {
               // CompaignDetails item = new CompaignDetails();
               // item.setViewType(CompaignsAdapter.VIEW_TYPE_HEADER);
                //item.setName("Spotlight");
                List<CompaignDetails> spotList = gson.fromJson("" + response.getJSONArray("CAMPAIGN_SPOT_LIGHT_VIEWS"), listType);
                for (CompaignDetails spot : spotList) {
                    spot.setViewType(CompaignsAdapter.VIEW_TYPE_SINGLE);
                }

       try {

                    JSONArray menuItemsJsonArray = new JSONArray(""+response.getJSONArray("CAMPAIGN_SPOT_LIGHT_VIEWS"));

                    for (int i = 0; i < menuItemsJsonArray.length(); ++i) {

                        JSONObject menuItemObject = menuItemsJsonArray.getJSONObject(i);

                        int showAnswerType = menuItemObject.getInt("showAnswerType");
                        String description = menuItemObject.getString("description");
                        String language = menuItemObject.getString("language");
                        Integer availablePoints = menuItemObject.getInt("availablePoints");
                        String type = menuItemObject.getString("type");
                        Integer points = menuItemObject.getInt("points");
                        String expiryDate = menuItemObject.getString("expiryDate");
                        String videoPlayerType = menuItemObject.getString("videoPlayerType");
                        Byte showSkipButton = Byte.valueOf(menuItemObject.getString("showSkipButton"));
                        String logo = menuItemObject.getString("logo");
                        int showType = menuItemObject.getInt("showType");
                        long serverTime = menuItemObject.getLong("serverTime");
                        int id = menuItemObject.getInt("id");
                        int minimumPoints = menuItemObject.getInt("minimumPoints");
                        int mediaType = menuItemObject.getInt("mediaType");
                        String serverTimeZone = menuItemObject.getString("serverTimeZone");
                        int maximumPoints = menuItemObject.getInt("maximumPoints");
                        String name = menuItemObject.getString("name");
                        String channelName = menuItemObject.getString("channelName");
                        long totalViews = menuItemObject.getLong("totalViews");
                        String mediaPath = menuItemObject.getString("mediaPath");
                        Integer numberOfQuestions = menuItemObject.getInt("numberOfQuestions");

                         CompaignDetails menuItem = new CompaignDetails(showAnswerType, description,language, availablePoints, type, points,expiryDate, videoPlayerType, showSkipButton, logo, showType, id,  minimumPoints, mediaType, serverTimeZone, maximumPoints,name, channelName,totalViews,mediaPath,numberOfQuestions);
                         myItems.add(menuItem);
                    }
                } catch (JSONException exception) {
                    Log.e("Home Fragment***", "Unable to parse JSON file.", exception);
                }

                addNativeExpressAds();
                setUpAndLoadNativeExpressAds();

                prepareEntireList();




                newSpotlightAdapter = new SpotlightAdapter(context,mRecyclerViewItems,Home_fragment.this);
                spotlight_recyclerview.setAdapter(newSpotlightAdapter);

                newSpotlightAdapter.notifyDataSetChanged();



            }

        }
    }


    private void prepareEntireList(){
        int size = adds.size()+myItems.size();
        int addsCount = 0;
        int myItemsCount = 0;
        for(int i=0;i<size;i++){
            if(i%2 == 0 && myItemsCount < myItems.size()){
                mRecyclerViewItems.add(myItems.get(myItemsCount));
                myItemsCount++;
            }else{
                if(addsCount < adds.size()) {
                    mRecyclerViewItems.add(adds.get(addsCount));
                    addsCount++;
                }
            }
        }
    }

    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 0; i <= myItems.size(); i++) {

                final NativeExpressAdView adView = new NativeExpressAdView(getActivity());
                adds.add(i, adView);



        }
    }


    /**
     * Sets up and loads the Native Express ads.
     */
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.



        spotlight_recyclerview.post(new Runnable() {
            @Override
            public void run() {
                final float scale = getActivity().getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 1; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) mRecyclerViewItems.get(i);
                    final CardView cardView = (CardView) getActivity().findViewById(R.id.ad_card_view);
                    final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                            - cardView.getPaddingRight();
                    AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(AD_UNIT_ID);
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(1);
            }
        });
    }


    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.i("device id=",deviceId);
        adView.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());


    }



    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
