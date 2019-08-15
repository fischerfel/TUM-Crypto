package com.priyank.checkin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.widget.PlacePickerFragment;
import com.facebook.Session;

public class PlacePickerSampleActivity extends FragmentActivity implements LocationListener {
    private static final int PLACE_ACTIVITY = 1;

    private static final Location SEATTLE_LOCATION = new Location("") {
        {
            setLatitude(47.6097);
            setLongitude(-122.3331);

        }
    };
    private static final Location SAN_FRANCISCO_LOCATION = new Location("") {
        {
            setLatitude(37.7750);
            setLongitude(-122.4183);
        }
    };
    private static final Location PARIS_LOCATION = new Location("") {
        {
            setLatitude(48.857875);
            setLongitude(2.294635);
        }
    };

    private TextView resultsTextView;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private String NameEditTextValue;
    private EditText NameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main7);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        Button b=(Button) findViewById(R.id.button1);

        b.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        NameEditTextValue = NameEditText.getText().toString();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Alert Message");
                        i.putExtra(Intent.EXTRA_TEXT, NameEditTextValue +"- via Security Android App");
                        startActivity(Intent.createChooser(i, "Alert Status"));
                       }

                    });

        NameEditText = (EditText) findViewById(R.id.editText1);

        resultsTextView = (TextView) findViewById(R.id.resultsTextView);
        Button button = (Button) findViewById(R.id.seattleButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickSeattle();
            }
        });

        button = (Button) findViewById(R.id.sanFranciscoButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickSanFrancisco();
            }
        });

        button = (Button) findViewById(R.id.gpsButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickGPS();
            }
        });

        if (Session.getActiveSession() == null ||
                Session.getActiveSession().isClosed()) {
            Session.openActiveSession(this, true, null);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update the display every time we are started (this will be "no place selected" on first
        // run, or possibly details of a place if the activity is being re-created).
        displaySelectedPlace(RESULT_OK);
    }

    private void onError(Exception exception) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error").setMessage(exception.getMessage()).setPositiveButton("OK", null);
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_ACTIVITY:
                displaySelectedPlace(resultCode);
                break;
            default:
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                break;
        }
    }

    private void displaySelectedPlace(int resultCode) {
        String results = "";
        PlacePickerApplication application = (PlacePickerApplication) getApplication();

        GraphPlace selection = application.getSelectedPlace();
        if (selection != null) {
            GraphLocation location = selection.getLocation();

            results = String.format("Name: %s\nCategory: %s\nLocation: (%f,%f)\nStreet: %s, %s, %s, %s, %s",
                    selection.getName(), selection.getCategory(),
                    location.getLatitude(), location.getLongitude(),
                    location.getStreet(), location.getCity(), location.getState(), location.getZip(),
                    location.getCountry());
        } else {
            results = "<No place selected>";
        }

        resultsTextView.setText(results);
        NameEditText.setText(results);
    }

    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void startPickPlaceActivity(Location location) {
        PlacePickerApplication application = (PlacePickerApplication) getApplication();
        application.setSelectedPlace(null);

        Intent intent = new Intent(this, PickPlaceActivity.class);
        PickPlaceActivity.populateParameters(intent, location, null);

        startActivityForResult(intent, PLACE_ACTIVITY);
    }

    private void onClickSeattle() {
        try {
            startPickPlaceActivity(SEATTLE_LOCATION);
        } catch (Exception ex) {
            onError(ex);
        }
    }

    private void onClickSanFrancisco() {
        try {
            startPickPlaceActivity(SAN_FRANCISCO_LOCATION);
        } catch (Exception ex) {
            onError(ex);
        }
    }

    private void onClickGPS() {
        try {
            if (lastKnownLocation == null) {
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, false);
                if (bestProvider != null) {
                    lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                }
            }
            if (lastKnownLocation == null) {
                String model = android.os.Build.MODEL;
                if (model.equals("sdk") || model.equals("google_sdk") || model.contains("x86")) {
                    // Looks like they are on an emulator, pretend we're in Paris if we don't have a
                    // location set.
                    lastKnownLocation = PARIS_LOCATION;
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.error_dialog_title)
                            .setMessage(R.string.no_location)
                            .setPositiveButton(R.string.ok_button, null)
                            .show();
                    return;
                }
            }
            startPickPlaceActivity(lastKnownLocation);
        } catch (Exception ex) {
            onError(ex);
        }
    }

}
