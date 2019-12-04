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

        // Get all the elements that should be made clickable
        Button btnAppName = findViewById(R.id.btnAppName);
        ImageView ivPersonofInterestPoster = findViewById(R.id.ivPersonofInterestPoster);
        ImageView ivBlindspotPoster = findViewById(R.id.ivBlindspotPoster);
        ImageView ivNCISLAPoster = findViewById(R.id.ivNCISLAPoster);

        // Set click listeners to all clickable elements
        btnAppName.setOnClickListener(this);
        ivPersonofInterestPoster.setOnClickListener(this);
        ivBlindspotPoster.setOnClickListener(this);
        ivNCISLAPoster.setOnClickListener(this);

        // Instantiate sharedPreferences
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // Restore the value in sharedPreferences if the TV Show was liked or not
        personofinterest_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_personofinterest_liked), true);

        // If the show was liked or disliked, change the text and posters in that row to match recommendations
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

    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity
        if (view.getId() == R.id.ivPersonofInterestPoster) {
            Intent intent = new Intent(getApplicationContext(), ShowPOIActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnAppName) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ivBlindspotPoster) {
            Intent intent = new Intent(getApplicationContext(), ShowBlindspotActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ivNCISLAPoster) {
            Intent intent = new Intent(getApplicationContext(), ShowNCISLAActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Get the sharedPreferences editor
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if the show was liked or disliked in a boolean in sharedPreferences
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_personofinterest_liked), personofinterest_liked);

        // Apply the edits to sharedPreferences
        sharedPrefsEditor.apply();
    }

}
