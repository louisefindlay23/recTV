package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

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

    // Constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "http://www.omdbapi.com/?t=%s&apikey=4c1aac0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blindspot);

        // Get all the elements that should be made clickable
        Button btnAppName = findViewById(R.id.btnAppName);
        ImageView ivDownArrow = findViewById(R.id.ivBlindspotDownArrow);

        // Set click listeners to all clickable elements
        btnAppName.setOnClickListener(this);
        ivDownArrow.setOnClickListener(this);

        // When the activity has been started, run the ShowDescription method to populate the show description
        downloadShowDescription();

        // When the activity has been started, run the ShowStats method to populate the show's statistics
        downloadShowStats();

        // Create same number of list entries as episodes in Season 1
        List<ShowName> shownames = createShowNames(24);

        // Set up RecyclerView for Show Names
        RecyclerView recyclerView = findViewById(R.id.rvShowName);
        ShowNameRecyclerViewAdapter adapter = new ShowNameRecyclerViewAdapter(getApplicationContext(), shownames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    // Create an ArrayList and for every list item add Episode string and a number increasing by one each time
    private List<ShowName> createShowNames(int number){
        List<ShowName> shownames = new ArrayList<ShowName>(number);
        for (int i = 1; i < number; i++){
            ShowName showname = createShowNames("Episode " + i);
            shownames.add(showname);
        }
        return shownames;
    }
    // Set and return the ShowName and EpisodeName
    private ShowName createShowNames(String EpisodeName){
        ShowName m = new ShowName();
        m.setEpisodeName(EpisodeName);
        return m;
    }

    public void downloadShowDescription(){
        // Downloads TV show metadata using Volley from the Open Movie Database and parses the returning JSON to display the show description as a string

        // Store the show name in order to download metadata
        String getShowNameForShowMetadata = (getString(R.string.blindspot_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show name to download details for then gracefully exit
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder blindspotDescription = new StringBuilder();
                        TextView tvBlindspotDescriptionDisplay = findViewById(R.id.tvBlindspotShowDescription);

                        // Build JSONObjects to parse the JSON response to a more human-readable format
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String plotObj = responseObj.getString("Plot");
                            // Add the returned show description to the display
                            blindspotDescription.append("\n")
                                    .append(responseObj.getString("Plot"));

                        // If there are any JSONException errors, print them in the log so the error can be diagnosed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // If there was an error in parsing the JSON, tell the user
                        if (blindspotDescription.length() == 0){
                            tvBlindspotDescriptionDisplay.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvBlindspotDescriptionDisplay.setText(blindspotDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user
                TextView tvShowDescriptionDisplay = findViewById(R.id.tvBlindspotShowDescription);
                tvShowDescriptionDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void downloadShowStats(){
        // Downloads TV show metadata using Volley from the Open Movie Database and parses the returning JSON to display strings of year, episode runtime and age rating

        // Store the show's name in order to download metadata
        String getShowNameForShowMetadata = (getString(R.string.blindspot_name));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show ID to download details for then gracefully exit
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder showStats = new StringBuilder();
                        TextView tvShowStats = findViewById(R.id.tvBlindspotShowStats);

                        // Build JSONObjects to parse the JSON response to a more human-readable format
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String yearObj = responseObj.getString("Year");
                            // Add the year, episode runtime and age rating returned to the display
                            showStats.append("\n")
                                    .append(responseObj.getString("Year"))
                                    .append(" / ").append(responseObj.getString("Runtime"))
                                    .append(" / ").append(responseObj.getString("Rated"));
                        // If there are any JSONException errors, print them in the log so the error can be diagnosed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // If there was an error in parsing the JSON, tell the user
                        if (showStats.length() == 0){
                            tvShowStats.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowStats.setText(showStats.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user
                TextView tvShowStats = findViewById(R.id.tvBlindspotShowStats);
                tvShowStats.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity
        if (view.getId() == R.id.btnAppName) {
            Intent intent = new Intent(getApplicationContext(), RecommendationsActivity.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.ivBlindspotDownArrow) {
            Intent intent = new Intent(getApplicationContext(), ShowBlindspot2Activity.class);
            startActivity(intent);
        }
    }
}
