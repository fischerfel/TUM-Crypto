package pl.wrweb.reikoapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ButtonsActivity extends ActionBarActivity implements View.OnClickListener {

public static final String CLOSE = "close";
public static final String OPTION = "option";

private Button firstButton, secondButton, oBlondynkach, thirdButton, forthButton, fifthButton, sixthButton, seventhButton, closeButton;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buttons);

    firstButton = (Button) findViewById(R.id.firstButton);
    secondButton = (Button) findViewById(R.id.secondButton);
    oBlondynkach = (Button) findViewById(R.id.blondynki);
    thirdButton = (Button) findViewById(R.id.thirdButton);
    forthButton = (Button) findViewById(R.id.forthButton);
    fifthButton = (Button) findViewById(R.id.fifthButton);
    sixthButton = (Button) findViewById(R.id.sixthButton);
    seventhButton = (Button) findViewById(R.id.seventhButton);
    closeButton = (Button) findViewById(R.id.closeButton);

    firstButton.setOnClickListener(this);
    secondButton.setOnClickListener(this);
    oBlondynkach.setOnClickListener(this);
    thirdButton.setOnClickListener(this);
    forthButton.setOnClickListener(this);
    fifthButton.setOnClickListener(this);
    sixthButton.setOnClickListener(this);
    seventhButton.setOnClickListener(this);
    closeButton.setOnClickListener(this);


    String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    AdView adView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()
            //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice(md5(androidId).toUpperCase())
            .build();
    adView.loadAd(adRequest);
}

@Override
public void onClick(View v) {
    String idAsString = v.getResources().getResourceName(v.getId());
    String option = idAsString.replace("pl.wrweb.reikoapp:id/", "").replace("Button", "");
    if (option.equals(CLOSE)) {
        finish();
    } else {
        Intent intent = new Intent(ButtonsActivity.this, TextActivity.class);
        intent.putExtra(OPTION, option);
        startActivity(intent);
    }
}

public String md5(String s) {
    try {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

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
