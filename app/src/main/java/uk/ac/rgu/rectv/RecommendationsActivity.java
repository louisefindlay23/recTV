package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendationsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    private boolean personofinterest_liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        // get the AppName button
        Button btnAppName = findViewById(R.id.btnAppName);

        // set the click listener to the btnAppName burtton
        btnAppName.setOnClickListener(this);

        // get the PersonofInterest Poster image
        ImageView ivPersonofInterestPoster = findViewById(R.id.ivPersonofInterestPoster);

        // set the click listener to the PersonofInterest Poster image
        ivPersonofInterestPoster.setOnClickListener(this);

        // get the Blindspot Poster image
        ImageView ivBlindspotPoster = findViewById(R.id.ivBlindspotPoster);

        // set the click listener to the Blindspot Poster image
        ivBlindspotPoster.setOnClickListener(this);

        // get the NCIS LA Poster image
        ImageView ivNCISLAPoster = findViewById(R.id.ivNCISLAPoster);

        // set the click listener to the NCIS LA Poster image
        ivNCISLAPoster.setOnClickListener(this);

        // instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // restore the value of if a TV Show was liked or not
        personofinterest_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_personofinterest_liked), true);

        if(personofinterest_liked == true){
            ImageView ivAgentXPoster = findViewById(R.id.ivPoster4);
            ivAgentXPoster.setImageResource(R.drawable.xenawarriorprincess_poster);
            ImageView ivLoomingTowerPoster = findViewById(R.id.ivPoster5);
            ivLoomingTowerPoster.setImageResource(R.drawable.buffyvampireslayer_poster);
            ImageView ivTakenPoster = findViewById(R.id.ivPoster6);
            ivTakenPoster.setImageResource(R.drawable.humans_poster);
            TextView tvFansofPersonofInterest = findViewById(R.id.tvFansofPersonofInterest);
            tvFansofPersonofInterest.setText(getString(R.string.fansofpersonofinterest));
        } else if (personofinterest_liked == false) {
            ImageView ivAgentXPoster = findViewById(R.id.ivPoster4);
            ivAgentXPoster.setImageResource(R.drawable.numbers_poster);
            ImageView ivLoomingTowerPoster = findViewById(R.id.ivPoster5);
            ivLoomingTowerPoster.setImageResource(R.drawable.chuck_poster);
            ImageView ivTakenPoster = findViewById(R.id.ivPoster6);
            ivTakenPoster.setImageResource(R.drawable.scorpion_poster);
            TextView tvFansofPersonofInterest = findViewById(R.id.tvFansofPersonofInterest);
            tvFansofPersonofInterest.setText(getString(R.string.hatersofpersonofinterest));
        }
    }

    // Changing Activity
    @Override
    public void onClick(View view) {
        // view is the View (Button, ExitText, TextView, etc) that was clicked
        if (view.getId() == R.id.ivPersonofInterestPoster) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowPOIActivity.class);
            // start the Activity
            startActivity(intent);
        } else if (view.getId() == R.id.btnAppName) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // start the Activity
            startActivity(intent);
        } else if (view.getId() == R.id.ivBlindspotPoster) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowBlindspotActivity.class);
            // start the Activity
            startActivity(intent);
        } else if (view.getId() == R.id.ivNCISLAPoster) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowNCISLAActivity.class);
            // start the Activity
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Get the Shared Preferences editor
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if Like icon has been clicked or not
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_personofinterest_liked), personofinterest_liked);

        // Apply the edits to Shared Preferences
        sharedPrefsEditor.apply();
    }

}
