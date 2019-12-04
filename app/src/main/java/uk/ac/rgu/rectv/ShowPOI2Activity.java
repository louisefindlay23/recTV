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

public class ShowPOI2Activity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    private boolean personofinterest_liked;

    // TAG to be used when logging
    private static final String TAG = ShowPOI2Activity.class.getCanonicalName();

    // Constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poi2);

        // Get all the elements that should be made clickable
        ImageView ivBackArrow = findViewById(R.id.ivBackArrow);
        ImageView ivPrimeVideo = findViewById(R.id.ivPrimeVideo);
        ImageView ivLikeOutline = findViewById(R.id.ivLikeOutline);
        ImageView ivDislikeOutline = findViewById(R.id.ivDislikeOutline);

        // Set click listeners to all clickable elements
        ivBackArrow.setOnClickListener(this);
        ivPrimeVideo.setOnClickListener(this);
        ivLikeOutline.setOnClickListener(this);
        ivDislikeOutline.setOnClickListener(this);

        // Instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // Restore the value in sharedPreferences if the TV Show was liked or not
        personofinterest_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_personofinterest_liked), true);

        // When the activity has been started, run the LikeorDislike method to populate the recommendations
        personofinterest_LikeorDislike();

    }

    public void personofinterest_LikeorDislike() {
        // If the show was liked or disliked, change the strings and icons accordingly
        if (personofinterest_liked == true) {
            ImageView ivLikeOutline = findViewById(R.id.ivLikeOutline);
            ImageView ivDislikeOutline = findViewById(R.id.ivDislikeOutline);
            ivLikeOutline.setImageResource(R.drawable.like_icon);
            ivDislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText(getString(R.string.liked));
            // If the show was liked, run the downloadRelatedShows method to get similar shows
            downloadRelatedShows();
        } else if (personofinterest_liked == false) {
            ImageView ivLikeOutline = findViewById(R.id.ivLikeOutline);
            ImageView ivDislikeOutline = findViewById(R.id.ivDislikeOutline);
            ivLikeOutline.setImageResource(R.drawable.like_outline);
            ivDislikeOutline.setImageResource(R.drawable.dislike_icon);
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText(getString(R.string.disliked));
            TextView tvPersonofIntereststatus = findViewById(R.id.tvPersonofInterestLoved);
            tvPersonofIntereststatus.setText(getString(R.string.hated_personofinterest));
            TextView tvRecommendations = findViewById(R.id.tvRecommendations);
            tvRecommendations.setText(getString(R.string.dislikedrecommendations_personofinterest));
        }
    }

    public void downloadRelatedShows(){
        /// Downloads TV show metadata using Volley from the Movie Database and parses the returning JSON to display strings of similar shows

        // Store the show's Movie Database ID number in order to download metadata
        String getShowNameForShowMetadata = (getString(R.string.personofinterest_tmbdid));

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
                        StringBuilder showRecommendations = new StringBuilder();
                        TextView tvRecommendations = findViewById(R.id.tvRecommendations);

                        // Build JSONObjects to parse the JSON response to a more human-readable format
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                // Add the shownames returned to the display
                                showRecommendations.append(resultsObj.getString("name")).append("\n");
                            }

                        // If there are any JSONException errors, print them in the log so the error can be diagnosed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // If there was an error in parsing the JSON, tell the user
                        if (showRecommendations.length() == 0){
                            tvRecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvRecommendations.setText(showRecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user
                TextView tvRelatedShowDisplay = findViewById(R.id.tvRecommendations);
                tvRelatedShowDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity or method
        if (view.getId() == R.id.ivBackArrow) {
            Intent intent = new Intent(getApplicationContext(), ShowPOIActivity.class);
            startActivity(intent);
            // Run method to launch web browser to Amazon website
        } else if (view.getId() == R.id.ivPrimeVideo) {
            launchWeb();
    } else if (view.getId() == R.id.ivLikeOutline) {
            // If the show's like or dislike status has been changed, update the boolean and run the LikeorDislike method again
            personofinterest_liked = true;
            personofinterest_LikeorDislike();
    } else if (view.getId() == R.id.ivDislikeOutline) {
        personofinterest_liked = false;
            personofinterest_LikeorDislike();
    }
    }

    // Method to launch the web browser to Prime Video streaming service
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
        // Get the sharedPreferences editor
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if the show was liked or disliked in a boolean in sharedPreferences
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_personofinterest_liked), personofinterest_liked);

        // Apply the edits to sharedPreferences
        sharedPrefsEditor.apply();
    }
}
