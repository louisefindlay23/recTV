package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class RecommendationsActivity extends AppCompatActivity implements View.OnClickListener {

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

}
