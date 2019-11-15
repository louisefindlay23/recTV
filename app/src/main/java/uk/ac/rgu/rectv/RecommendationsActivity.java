package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Set;

public class RecommendationsActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG to be used when logging
    private static final String TAG = RecommendationsActivity.class.getCanonicalName();

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://private-anon-b6a956c00a-trakt.apiary-mock.com/shows/%s";

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

        // Download show name from Trakt API
        downloadShowNames();
    }

    public void downloadShowNames(){
        // Downloads and displays a show name from Trakt API
        String getShowDetailsForShowNames = (getString(R.string.personofinterest_name));

        Log.d(TAG, "getting the show details for" + getShowDetailsForShowNames);

        // if there's no show name to download details for then exit
        if (getShowDetailsForShowNames == null) {
            return;
        }

        // build string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowDetailsForShowNames);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView tvShowNameDisplay = findViewById(R.id.tvShowNameDisplay);
                        tvShowNameDisplay.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvShowNameDisplay = findViewById(R.id.tvShowNameDisplay);
                tvShowNameDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    // Changing Activity
    @Override
    public void onClick(View view) {
        // view is the View (Button, ExitText, TextView, etc) that was clicked
        if (view.getId() == R.id.ivPersonofInterestPoster) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
            // start the Activity
            startActivity(intent);
        }

        else if (view.getId() == R.id.btnAppName) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // start the Activity
            startActivity(intent);
        }
    }
}
