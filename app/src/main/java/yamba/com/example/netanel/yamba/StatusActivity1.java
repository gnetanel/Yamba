package yamba.com.example.netanel.yamba;

import android.content.Intent;
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

public class StatusActivity1 extends BaseActivity implements View.OnClickListener{

    public static final String TAG = StatusActivity1.class.getName().toString();
    private EditText editText;
    private Button updateButton;


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

    class PostToTwitter extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "do in background");
            try {
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

    // Called when an options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"Option selected, value = " + item.getItemId());
        switch (item.getItemId()) { //
            case R.id.itemPrefs:
                Log.d(TAG,"Selected item is itemPrefs, launching activity...");
                startActivity(new Intent(this, PrefsActivity.class)); //
                break;
            case R.id.itemServiceStart:
                Log.d(TAG,"Start service from option menu");
                startService(new Intent(this,ServiceUpdate.class));
                break;
            case R.id.itemServiceStop:
                Log.d(TAG,"Stop service from option menu");
                stopService(new Intent(this,ServiceUpdate.class));
                break;
            case R.id.itemGoTotimeline:
                Log.d(TAG,"Open timeline activity");
                startActivity(new Intent(this, TimelineActivity.class));
                break;
            default:
                Log.d(TAG,"Unknown option");
            break;
        }
        return true; //
    }

}
