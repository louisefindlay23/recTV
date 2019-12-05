package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowNCISLA2Activity extends AppCompatActivity implements View.OnClickListener {

    // Adapted from Course Materials Week 6
    private SharedPreferences sharedPrefs;
    private boolean ncisla_liked;

    // TAG to be used when logging (Adapted from Course Materials Week 7)
    private static final String TAG = ShowNCISLA2Activity.class.getCanonicalName();

    // Constant for downloading show data (Adapted from Course Materials Week 7)
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ncisla2);

        // Get all the elements that should be made clickable (Adapted code from Course Materials Week 3)
        ImageView ivNCISLABackArrow = findViewById(R.id.ivNCISLABackArrow);
        ImageView ivNCISLAPrimeVideo = findViewById(R.id.ivNCISLAPrimeVideo);
        ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);
        ImageView ivNCISLADislikeOutline = findViewById(R.id.ivDislikeOutline);

        // Set click listeners to all clickable elements (Adapted code from Course Materials Week 3)
        ivNCISLABackArrow.setOnClickListener(this);
        ivNCISLAPrimeVideo.setOnClickListener(this);
        ivNCISLALikeOutline.setOnClickListener(this);
        ivNCISLADislikeOutline.setOnClickListener(this);

        // Instantiate sharedPreferences (Adapted code from Course Materials Week 6)
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // Restore the value in sharedPreferences if the TV Show was liked or not (Adapted code from Course Materials Week 6)
        ncisla_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_ncisla_liked), true);

        // When the activity has been started, run the LikeorDislike method to populate the recommendations
        ncislaLikeorDislike();
    }

    public void ncislaLikeorDislike() {
        // If the show was liked or disliked, change the strings and icons accordingly
        if (ncisla_liked == true) {
            ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);
            ImageView ivNCISLADislikeOutline = findViewById(R.id.ivDislikeOutline);
            ivNCISLALikeOutline.setImageResource(R.drawable.like_icon);
            ivNCISLADislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvNCISLAor = findViewById(R.id.tvNCISLAor);
            tvNCISLAor.setText(getString(R.string.liked));
            TextView tvNCISLAstatus = findViewById(R.id.tvNCISLALoved);
            tvNCISLAstatus.setText(getString(R.string.loved_ncisla));
            // If the show was liked, run the downloadRelatedShows method to get similar shows
            downloadRelatedShows();
        } else if (ncisla_liked == false) {
            ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);
            ImageView ivNCISLADislikeOutline = findViewById(R.id.ivDislikeOutline);
            ivNCISLALikeOutline.setImageResource(R.drawable.like_outline);
            ivNCISLADislikeOutline.setImageResource(R.drawable.dislike_icon);
            TextView tvNCISLAor = findViewById(R.id.tvNCISLAor);
            tvNCISLAor.setText(getString(R.string.disliked));
            TextView tvNCISLAstatus = findViewById(R.id.tvNCISLALoved);
            tvNCISLAstatus.setText(getString(R.string.hated_ncisla));
            TextView tvNCISLARecommendations = findViewById(R.id.tvNCISLARecommendations);
            tvNCISLARecommendations.setText(getString(R.string.dislikedrecommendations_ncisla));
        }
    }

    public void downloadRelatedShows(){
        // Downloads TV show metadata using Volley from the Movie Database and parses the returning JSON to display strings of similar shows (Adapted from Course Materials Week 7)

        // Store the show's Movie Database ID number in order to download metadata (Adapted from Course Materials Week 7)
        String getShowNameForShowMetadata = (getString(R.string.ncisla_tmbdid));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show ID to download details for then gracefully exit (Adapted from Course Materials Week 7)
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build the string for the URL to get the show details from (Adapted from Course Materials Week 7)
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL (Adapted from Course Materials Week 7)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder NCISLARecommendations = new StringBuilder();
                        TextView tvNCISLARecommendations = findViewById(R.id.tvNCISLARecommendations);

                        // Build JSONObjects to parse the JSON response to a more human-readable format (Adapted from Course Materials Week 7)
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                // Add the recommendations returned to the display
                                NCISLARecommendations.append(resultsObj.getString("name")).append("\n");
                            }
                            // If there are any JSONException errors, print them in the log so the error can be diagnosed (Adapted from Course Materials Week 7)
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // If there was an error in parsing the JSON, tell the user (Adapted from Course Materials Week 7)
                        if (NCISLARecommendations.length() == 0){
                            tvNCISLARecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvNCISLARecommendations.setText(NCISLARecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user (Adapted from Course Materials Week 7)
                TextView tvNCISLARecommendationsDisplay = findViewById(R.id.tvNCISLARecommendations);
                tvNCISLARecommendationsDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details (Adapted from Course Materials Week 7)
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity or method (Adapted from Course Materials Week 3)
        if (view.getId() == R.id.ivNCISLABackArrow) {
            Intent intent = new Intent(getApplicationContext(), ShowNCISLAActivity.class);
            startActivity(intent);
            // Run method to launch web browser to Amazon website
        } else if (view.getId() == R.id.ivNCISLAPrimeVideo) {
            launchWeb();
            // If the show's like or dislike status has been changed, update the boolean and run the LikeorDislike method again
        } else if (view.getId() == R.id.ivNCISLALikeOutline) {
            ncisla_liked = true;
            ncislaLikeorDislike();
        } else if (view.getId() == R.id.ivDislikeOutline) {
            ncisla_liked = false;
            ncislaLikeorDislike();
        }
    }

    // Method to launch the web browser to Prime Video streaming service (Adapted from Android Developer Guide Common Intents)
    private void launchWeb() {
        Uri webpage = Uri.parse("https://www.amazon.co.uk/gp/product/B07G5SZPK6");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Get the sharedPreferences editor (Adapted from Course Materials Week 6)
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if the show was liked or disliked in a boolean in sharedPreferences (Adapted from Course Materials Week 6)
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_ncisla_liked), ncisla_liked);

        // Apply the edits to SharedPreferences (Adapted from Course Materials Week 6)
        sharedPrefsEditor.apply();
    }
}
