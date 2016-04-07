package asia.tatsujin.ptr;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.vlonjatg.progressactivity.ProgressActivity;

import asia.tatsujin.ptr.graptt.GrapttClient;

public class MainActivity extends AppCompatActivity implements OnTransactListener {

    public static GrapttClient grapttClient;
    private ProgressActivity progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        progress = (ProgressActivity) findViewById(R.id.progress);

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

    public void onStartTransaction() {
        progress.showLoading();
    }

    public void onFinishTransaction(String title) {
        setTitle(title);
        progress.showContent();
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
}
