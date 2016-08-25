package yamba.com.example.netanel.yamba;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.TwitterException;

public class StatusActivity1 extends BaseActivity implements View.OnClickListener, LocationListener {

    public static final String TAG = StatusActivity1.class.getName().toString();
    private EditText editText;
    private Button updateButton;
    private LocationManager locationManager;
    private Location location;
    String provider;
    private static final long LOCATION_MIN_TIME = 3600000; // One hour
    private static final float LOCATION_MIN_DISTANCE = 1000; // One kilometer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status1);

        editText = (EditText)findViewById(R.id.editText);
        updateButton = (Button)findViewById(R.id.button);
        updateButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy");
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG,"On clicked");
        String text = editText.getText().toString();
        new PostToTwitter().execute(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //
        inflater.inflate(R.menu.menu, menu); //
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
        provider = yambaApplication.getProvider();
        Log.d(TAG, "Provider = " + provider);
        if (! "NONE".equals(provider)){
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        }

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    // Location manager methods.

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d(TAG, "onLocationChanged");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(this.provider)){
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(this.provider)) {
            locationManager.removeUpdates(this);
        }
    }

    class PostToTwitter extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "do in background");

            try {
                if (location != null) {
                    double latlong[] = {location.getLatitude(), location.getLongitude()};
                    yambaApplication.getTwitter().setMyLocation(latlong);
                }
                winterwell.jtwitter.Status status  = ((YambaApplication)getApplication()).getTwitter().updateStatus(strings[0]);
                return status.toString();
            } catch (TwitterException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return "Failed to post";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(StatusActivity1.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer...values) {
            super.onProgressUpdate(values);
        }
    }



}
