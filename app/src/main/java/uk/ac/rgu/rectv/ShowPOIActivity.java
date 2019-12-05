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

import org.json.JSONException;
import org.json.JSONObject;

public class ShowPOIActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG to be used when logging (Adapted from Course Materials Week 7)
    private static final String TAG = ShowPOIActivity.class.getCanonicalName();

    // Constant for downloading show data (Adapted from Course Materials Week 7)
    private static final String SHOW_URL_TEMPLATE = "http://www.omdbapi.com/?t=%s&apikey=4c1aac0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poi);

        // Get all the elements that should be made clickable (Adapted from Course Materials Week 3)
        Button btnAppName = findViewById(R.id.btnAppName);
        ImageView ivDownArrow = findViewById(R.id.ivDownArrow);

        // Set click listeners to all clickable elements (Adapted from Course Materials Week 3)
        btnAppName.setOnClickListener(this);
        ivDownArrow.setOnClickListener(this);

        // When the activity has been started, run the ShowDescription method to populate the show description
        downloadShowDescription();

        // When the activity has been started, run the ShowStats method to populate the show's statistics
        downloadShowStats();
    }

    public void downloadShowDescription(){
        // Downloads TV show metadata using Volley from the Open Movie Database and parses the returning JSON to display the show description as a string (Adapted from Course Materials Week 7)

        // Store the show name in order to download metadata (Adapted from Course Materials Week 7)
        String getShowNameForShowMetadata = (getString(R.string.personofinterest_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show name to download details for then exit (Adapted from Course Materials Week 7)
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build string for the URL to get the show details from (Adapted from Course Materials Week 7)
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL (Adapted from Course Materials Week 7)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder showDescription = new StringBuilder();
                        TextView tvShowDescriptionDisplay = findViewById(R.id.tvShowDescription);

                        // Build JSONObjects to parse the JSON response to a more human-readable format (Adapted from Course Materials Week 7)
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String plotObj = responseObj.getString("Plot");
                            // Add the returned show description to the display (Adapted from Course Materials Week 7)
                            showDescription.append("\n")
                                    .append(responseObj.getString("Plot"));
                        // If there are any JSONException errors, print them in the log so the error can be diagnosed (Adapted from Course Materials Week 7)
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // If there was an error in parsing the JSON, tell the user (Adapted from Course Materials Week 7)
                        if (showDescription.length() == 0){
                            tvShowDescriptionDisplay.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowDescriptionDisplay.setText(showDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user (Adapted from Course Materials Week 7)
                TextView tvShowDescriptionDisplay = findViewById(R.id.tvShowDescription);
                tvShowDescriptionDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // make the request to download the show details (Adapted from Course Materials Week 7)
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void downloadShowStats(){
        // Downloads TV show metadata using Volley from the Open Movie Database and parses the returning JSON to display strings of year, episode runtime and age rating (Adapted from Course Materials Week 7)

        // Store the show's name in order to download metadata (Adapted from Course Materials Week 7)
        String getShowNameForShowMetadata = (getString(R.string.personofinterest_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show name to download details for then gracefully exit (Adapted from Course Materials Week 7)
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build string for the URL to get the show details from (Adapted from Course Materials Week 7)
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL (Adapted from Course Materials Week 7)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder showDescription = new StringBuilder();
                        TextView tvShowStats = findViewById(R.id.tvShowStats);

                        // Build JSONObjects to parse the JSON response to a more human-readable format (Adapted from Course Materials Week 7)
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String yearObj = responseObj.getString("Year");
                            // Add the year, episode runtime and age rating returned to the display (Adapted from Course Materials Week 7)
                            showDescription.append("\n")
                                    .append(responseObj.getString("Year"))
                                    .append(" / ").append(responseObj.getString("Runtime"))
                                    .append(" / ").append(responseObj.getString("Rated"));

                        // If there are any JSONException errors, print them in the log so the error can be diagnosed (Adapted from Course Materials Week 7)
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // If there was an error in parsing the JSON, tell the user (Adapted from Course Materials Week 7)
                        if (showDescription.length() == 0){
                            tvShowStats.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowStats.setText(showDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user (Adapted from Course Materials Week 7)
                TextView tvShowStats = findViewById(R.id.tvShowStats);
                tvShowStats.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details (Adapted from Course Materials Week 7)
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity (Adapted from Course Materials Week 3)
        if (view.getId() == R.id.btnAppName) {
            Intent intent = new Intent(getApplicationContext(), RecommendationsActivity.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.ivDownArrow) {
            Intent intent = new Intent(getApplicationContext(), ShowPOI2Activity.class);
            startActivity(intent);
        }
    }
}
