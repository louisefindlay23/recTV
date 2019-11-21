package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShowPart2Activity extends AppCompatActivity implements View.OnClickListener {

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
        } else if (view.getId() == R.id.ivPrimeVideo) {
            launchWeb();
        }
    }

    // Method to launch Implicit Intent to load the web browser

    private void launchWeb(String url) {
        Uri webpage = Uri.parse("https://www.amazon.co.uk/gp/product/B071VSVFW2");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
