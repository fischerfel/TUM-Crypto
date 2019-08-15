public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

public static FragmentManager fragmentManager;
public ViewPager viewPager;
private TabsPagerAdapter mAdapter;
private ActionBar actionBar;
// Tab titles
private Integer[] Icons = {R.drawable.homeg,R.drawable.placesg, R.drawable.mapg, R.drawable.reviewg,R.drawable.fbg};

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getSupportFragmentManager();
    // Initilization
    viewPager = (ViewPager) findViewById(R.id.pager);
    actionBar = getActionBar();
    mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

    viewPager.setAdapter(mAdapter);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

    // Adding Tabs
    for (Integer tab_icon : Icons) {
        actionBar.addTab(actionBar.newTab().setIcon(tab_icon)
                .setTabListener(this));



    }

    // Add code to print out the key hash
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.synergy88studios.quezoncityguide", 
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    /**
     * on swiping the viewpager make respective tab selected
     * */
    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // on changing the page
            // make respected tab selected
            actionBar.setSelectedNavigationItem(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    });
}

@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
    // on tab selected
    // show respected fragment view
    viewPager.setCurrentItem(tab.getPosition());
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
}


}
