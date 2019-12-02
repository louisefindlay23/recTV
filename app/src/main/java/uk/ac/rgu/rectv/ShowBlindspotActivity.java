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

public class ShowBlindspotActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG to be used when logging
    private static final String TAG = RecommendationsActivity.class.getCanonicalName();

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "http://www.omdbapi.com/?t=%s&apikey=4c1aac0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blindspot);

        // get the AppName button
        Button btnAppName = findViewById(R.id.btnAppName);

        // set the click listener to the btnAppName burtton
        btnAppName.setOnClickListener(this);

        // get the Down Arrow image
        ImageView ivDownArrow = findViewById(R.id.ivBlindspotDownArrow);

        // set the click listener to the ivDownArrow
        ivDownArrow.setOnClickListener(this);

        // Download show description from OMDB API
        downloadShowDescription();

        // Download show stats from OMDB API
        downloadShowStats();
    }

    public void downloadShowDescription(){
        // Downloads and displays a show description from the OMDB API
        String getShowNameForShowMetadata = (getString(R.string.blindspot_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // if there's no show name to download details for then exit
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // build string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder showDescription = new StringBuilder();
                        TextView tvShowDescriptionDisplay = findViewById(R.id.tvBlindspotShowDescription);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String plotObj = responseObj.getString("Plot");
                            // add the plot to the display
                            showDescription.append("\n")
                                    .append(responseObj.getString("Plot"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (showDescription.length() == 0){
                            tvShowDescriptionDisplay.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowDescriptionDisplay.setText(showDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvShowDescriptionDisplay = findViewById(R.id.tvBlindspotShowDescription);
                tvShowDescriptionDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void downloadShowStats(){
        // Downloads and displays the Show Stats from OMDB API
        String getShowNameForShowMetadata = (getString(R.string.blindspot_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // if there's no show name to download details for then exit
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // build string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder showDescription = new StringBuilder();
                        TextView tvShowStats = findViewById(R.id.tvBlindspotShowStats);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String yearObj = responseObj.getString("Year");
                            // add the title to the display
                            showDescription.append("\n")
                                    .append(responseObj.getString("Year"))
                                    .append(" / ").append(responseObj.getString("Runtime"))
                                    .append(" / ").append(responseObj.getString("Rated"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (showDescription.length() == 0){
                            tvShowStats.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowStats.setText(showDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvShowStats = findViewById(R.id.tvBlindspotShowStats);
                tvShowStats.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
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
        if (view.getId() == R.id.btnAppName) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), RecommendationsActivity.class);
            // start the Activity
            startActivity(intent);
        }

        else if (view.getId() == R.id.ivBlindspotDownArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowBlindspot2Activity.class);
            // start the Activity
            startActivity(intent);
        }
    }
}
