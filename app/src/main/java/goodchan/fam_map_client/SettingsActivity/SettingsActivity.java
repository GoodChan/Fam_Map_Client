package goodchan.fam_map_client.SettingsActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import goodchan.fam_map_client.MainActivity.LoginFragment;
import goodchan.fam_map_client.MainActivity.MainActivity;
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.R;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout logout;
    private Switch lifeStorySwitch;
    private Switch familyTreeSwitch;
    private Switch spouseSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logout = findViewById(R.id.logout_layout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        lifeStorySwitch = findViewById(R.id.life_story_switch);
        lifeStorySwitch.setChecked(Data.lifeStoryLines);
        lifeStorySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Data.lifeStoryLines == true) {
                    Data.lifeStoryLines = false;
                }
                else {
                    Data.lifeStoryLines = true;
                }
            }
        });

        familyTreeSwitch = findViewById(R.id.tree_lines_switch);
        familyTreeSwitch.setChecked(Data.familyTreeLines);
        familyTreeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Data.familyTreeLines == true) {
                    Data.familyTreeLines = false;
                }
                else {
                    Data.familyTreeLines = true;
                }
            }
        });

        spouseSwitch = findViewById(R.id.spouse_lines_switch);
        spouseSwitch.setChecked(Data.spouseLines);
        spouseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Data.spouseLines == true) {
                    Data.spouseLines = false;
                }
                else {
                    Data.spouseLines = true;
                }
            }
        });


    }
}
