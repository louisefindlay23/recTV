package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowPart2Activity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    private boolean personofinterest_liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_part2);

        // get the Back Arrow button
        ImageView ivBackArrow = findViewById(R.id.ivBackArrow);

        // set the click listener to the back arrow
        ivBackArrow.setOnClickListener(this);

        // get the Prime Video image
        ImageView ivPrimeVideo = findViewById(R.id.ivPrimeVideo);

        // set the click listener to the Prime Video image
        ivPrimeVideo.setOnClickListener(this);

        // instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // restore the value of if a TV Show was liked or not
        personofinterest_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_personofinterest_liked), true);

        // If the show is liked or disliked
        if(personofinterest_liked == true){
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText("Liked");
        } else if (personofinterest_liked == false) {
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText("Disliked");
        }
    }

    // Changing Activity
    @Override
    public void onClick(View view) {
        // view is the View (Button, ExitText, TextView, etc) that was clicked
        if (view.getId() == R.id.ivBackArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
            // start the Activity
            startActivity(intent);
            // To launch web browser when Amazon Logo is clicked
        } else if (view.getId() == R.id.ivPrimeVideo) {
            launchWeb();
    } else if (view.getId() == R.id.ivLikeOutline) {
        // Set Shared Preferences variable to true
        personofinterest_liked = true;
            // Change image to LikeIcon
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText("Liked");
    } else if (view.getId() == R.id.ivDislikeOutline) {
        // Set Shared Preferences variable to false
        personofinterest_liked = false;
        // Change image to LikeIcon
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText("Disliked");
    }
    }

    // Method to launch Implicit Intent to load the web browser

    private void launchWeb() {
        Uri webpage = Uri.parse("https://www.amazon.co.uk/gp/product/B071VSVFW2");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
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
