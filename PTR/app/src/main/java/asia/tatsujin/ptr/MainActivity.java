package asia.tatsujin.ptr;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;

import asia.tatsujin.ptr.graptt.GrapttClient;

public class MainActivity extends AppCompatActivity {

    public static GrapttClient grapttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (BuildConfig.DEBUG)
            ActiveAndroid.setLoggingEnabled(true);
        grapttClient = new GrapttClient(this, getString(R.string.api_url), new GrapttClient.OnConnectListener() {
            @Override
            public void onConnect(String status) {
                Log.d("GrapttClient", status);
            }
            @Override
            public void onError(String message) {
                Log.d("GrapttClient", message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        grapttClient.close(new GrapttClient.OnDisconnectListener() {
            @Override
            public void onDisconnect() {
                Log.d("GrapttClient", "Disconnected");
            }
        });
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
