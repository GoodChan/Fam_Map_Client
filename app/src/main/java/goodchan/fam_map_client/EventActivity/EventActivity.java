package goodchan.fam_map_client.EventActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import java.util.logging.Logger;
import goodchan.fam_map_client.MainActivity.MainMapFragment;
import goodchan.fam_map_client.R;

public class EventActivity extends AppCompatActivity {
    private static Logger logger;
    private Fragment eventMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        eventMapFragment = fragmentManager.findFragmentById(R.id.eventActivityFrameLayout);
        if (eventMapFragment == null) { //how to pass data from fragment to activity, main activity on login,
            eventMapFragment = new MainMapFragment();
            //grab nested fragment's manager and start it

            eventMapFragment.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction()
                    .add(R.id.eventActivityFrameLayout, eventMapFragment)
                    .commit();
        }
    }
}
