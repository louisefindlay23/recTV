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

public class ShowBlindspot2Activity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    private boolean blindspot_liked;

    // TAG to be used when logging
    private static final String TAG = ShowBlindspot2Activity.class.getCanonicalName();

    // Constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blindspot2);

        // Get all the elements that should be made clickable
        ImageView ivBlindspotBackArrow = findViewById(R.id.ivBlindspotBackArrow);
        ImageView ivBlindspotPrimeVideo = findViewById(R.id.ivBlindspotAmazonVideo);
        ImageView ivBlindspotGoogleVideo = findViewById(R.id.ivBlindspotGooglePlay);
        ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);
        ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);

        // Set click listeners to all clickable elements
        ivBlindspotBackArrow.setOnClickListener(this);
        ivBlindspotPrimeVideo.setOnClickListener(this);
        ivBlindspotGoogleVideo.setOnClickListener(this);
        ivBlindspotLikeOutline.setOnClickListener(this);
        ivBlindspotDislikeOutline.setOnClickListener(this);

        // Instantiate sharedPreferences
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // Restore the value in sharedPreferences if the TV Show was liked or not
        blindspot_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_blindspot_liked), true);

        // When the activity has been started, run the LikeorDislike method to populate the recommendations
        blindspotLikeorDislike();
    }

    public void blindspotLikeorDislike() {
        // If the show was liked or disliked, change the strings and icons accordingly
        if(blindspot_liked == true){
            ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);
            ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);
            ivBlindspotLikeOutline.setImageResource(R.drawable.like_icon);
            ivBlindspotDislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvBlindspotor = findViewById(R.id.tvBlindspotor);
            tvBlindspotor.setText(getString(R.string.liked));
            // If the show was liked, run the downloadRelatedShows method to get similar shows
            downloadRelatedShows();
        } else if (blindspot_liked == false) {
            ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);
            ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);
            ivBlindspotLikeOutline.setImageResource(R.drawable.like_outline);
            ivBlindspotDislikeOutline.setImageResource(R.drawable.dislike_icon);
            TextView tvBlindspotor = findViewById(R.id.tvBlindspotor);
            tvBlindspotor.setText(getString(R.string.disliked));
            TextView tvBlindspotstatus = findViewById(R.id.tvBlindspotLoved);
            tvBlindspotstatus.setText(getString(R.string.hated_blindspot));
            TextView tvBlindspotRecommendations = findViewById(R.id.tvBlindspotRecommendations);
            tvBlindspotRecommendations.setText(getString(R.string.dislikedrecommendations_blindspot));
        }
    }

    public void downloadRelatedShows(){
        // Downloads TV show metadata using Volley from the Movie Database and parses the returning JSON to display strings of similar shows

        // Store the show's Movie Database ID number in order to download metadata
        String getShowNameForShowMetadata = (getString(R.string.blindspot_tmbdid));

        Log.d(TAG, "getting the show metadata for" + getShowNameForShowMetadata);

        // If there's no show ID to download details for then gracefully exit
        if (getShowNameForShowMetadata == null) {
            return;
        }

        // Build the string for the URL to get the show details from
        String url = String.format(SHOW_URL_TEMPLATE, getShowNameForShowMetadata);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder BlindspotRecommendations = new StringBuilder();
                        TextView tvBlindspotRecommendations = findViewById(R.id.tvBlindspotRecommendations);

                        // Build JSONObjects to parse the JSON response to a more human-readable format
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                // Add the recommendations returned to the display
                                BlindspotRecommendations.append(resultsObj.getString("name")).append("\n");
                            }

                        // If there are any JSONException errors, print them in the log so the error can be diagnosed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // If there was an error in parsing the JSON, tell the user
                        if (BlindspotRecommendations.length() == 0){
                            tvBlindspotRecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvBlindspotRecommendations.setText(BlindspotRecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If there was a VolleyError downloading the show metadata, inform the user
                TextView tvBlindspotRecommendationsDisplay = findViewById(R.id.tvBlindspotRecommendations);
                tvBlindspotRecommendationsDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
            }
        });

        // Make the request to download the show details
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        // If a clickable element was clicked, start the appropriate activity or method
        if (view.getId() == R.id.ivBlindspotBackArrow) {
            Intent intent = new Intent(getApplicationContext(), ShowBlindspotActivity.class);
            startActivity(intent);
            // Run method to launch web browser to Amazon website and Google Play
        } else if (view.getId() == R.id.ivBlindspotAmazonVideo) {
            launchWeb("https://www.amazon.co.uk/gp/video/detail/B07S7QWV27");
        } else if (view.getId() == R.id.ivBlindspotGooglePlay) {
            launchWeb("https://play.google.com/store/tv/show/Blindspot?id=OtezusYkH1Y&hl=en&gl=GB");
            // If the show's like or dislike status has been changed, update the boolean and run the LikeorDislike method again
        } else if (view.getId() == R.id.ivBlindspotLikeOutline) {
            blindspot_liked = true;
            blindspotLikeorDislike();
        } else if (view.getId() == R.id.ivBlindspotDislikeOutline) {
            blindspot_liked = false;
            blindspotLikeorDislike();
        }
    }

    // Methods to launch the web browser to streaming and on demand services
    public void launchWeb(String url) {
        Uri webpage = Uri.parse(url);
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
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_blindspot_liked), blindspot_liked);

        // Apply the edits to SharedPreferences
        sharedPrefsEditor.apply();
    }
}
