package goodchan.fam_map_client.MainActivity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import java.util.logging.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import goodchan.fam_map_client.R;
import goodchan.fam_map_client.Requests.LoginRequest;

public class MainActivity extends AppCompatActivity {

    private Fragment mainFragment;
    private static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("Client");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //singlefragment map activity TODO erase this
        //build this fragmentManager
         //manager smaller scale each activity has one fragment manager
//        mainFragment = fragmentManager.findFragmentById(R.id.mainFrameLayout);
//        if (mainFragment == null) { //how to bass data from fragment to activity, main activity on login,
//            mainFragment = new MainMapFragment();
//            //grab nested fragment's manager and start it
//            fragmentManager.beginTransaction()
//                    .add(R.id.mainFrameLayout, mainFragment)
//                    .commit();
//        }



        Fragment loginFragment = fragmentManager.findFragmentById(R.id.mainFrameLayout);
        if (loginFragment == null) { //how to bass data from fragment to activity, main activity on login,
            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.mainFrameLayout, loginFragment)
                    .commit();
        }
    }
}
