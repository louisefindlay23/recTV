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

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blindspot2);

        // get the Back Arrow button
        ImageView ivBlindspotBackArrow = findViewById(R.id.ivBlindspotBackArrow);

        // set the click listener to the back arrow
        ivBlindspotBackArrow.setOnClickListener(this);

        // get the Prime Video image
        ImageView ivBlindspotPrimeVideo = findViewById(R.id.ivBlindspotAmazonVideo);

        // set the click listener to the Prime Video image
        ivBlindspotPrimeVideo.setOnClickListener(this);

        // get the Google Play image
        ImageView ivBlindspotGoogleVideo = findViewById(R.id.ivBlindspotGooglePlay);

        // set the click listener to the Google Play image
        ivBlindspotGoogleVideo.setOnClickListener(this);

        // get the Like Outline image
        ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);

        // set the click listener to the like outline
        ivBlindspotLikeOutline.setOnClickListener(this);

        // get the Dislike Outline image
        ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);

        // set the click listener to the dislike outline
        ivBlindspotDislikeOutline.setOnClickListener(this);

        // instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // restore the value of if a TV Show was liked or not
        blindspot_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_blindspot_liked), true);

        // If the show is liked or disliked
        if(blindspot_liked == true){
            ivBlindspotLikeOutline.setImageResource(R.drawable.like_icon);
            ivBlindspotDislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvBlindspotor = findViewById(R.id.tvBlindspotor);
            tvBlindspotor.setText(getString(R.string.liked));
            // Download related shows
            downloadRelatedShows();
        } else if (blindspot_liked == false) {
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
        // Downloads and displays a show description from the OMDB API
        String getShowNameForShowMetadata = (getString(R.string.blindspot_tmbdid));

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
                        StringBuilder BlindspotRecommendations = new StringBuilder();
                        TextView tvBlindspotRecommendations = findViewById(R.id.tvBlindspotRecommendations);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                BlindspotRecommendations.append(resultsObj.getString("name")).append("\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (BlindspotRecommendations.length() == 0){
                            tvBlindspotRecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvBlindspotRecommendations.setText(BlindspotRecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvBlindspotRecommendationsDisplay = findViewById(R.id.tvBlindspotRecommendations);
                tvBlindspotRecommendationsDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
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
        if (view.getId() == R.id.ivBlindspotBackArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowBlindspotActivity.class);
            // start the Activity
            startActivity(intent);
            // To launch web browser when Amazon Logo is clicked
        } else if (view.getId() == R.id.ivBlindspotAmazonVideo) {
            launchAmazonWeb();
        } else if (view.getId() == R.id.ivBlindspotGooglePlay) {
            launchGooglePlayWeb();
        } else if (view.getId() == R.id.ivBlindspotLikeOutline) {
            // Set Shared Preferences variable to true
            blindspot_liked = true;
            // Change image to LikeIcon
            ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);
            ivBlindspotLikeOutline.setImageResource(R.drawable.like_icon);
            ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);
            ivBlindspotDislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvBlindspotor = findViewById(R.id.tvBlindspotor);
            tvBlindspotor.setText(getString(R.string.liked));
            TextView tvBlindspotstatus = findViewById(R.id.tvBlindspotLoved);
            tvBlindspotstatus.setText(getString(R.string.loved_blindspot));
            // Download related shows
            downloadRelatedShows();
        } else if (view.getId() == R.id.ivBlindspotDislikeOutline) {
            // Set Shared Preferences variable to false
            blindspot_liked = false;
            // Change image to LikeIcon
            ImageView ivBlindspotLikeOutline = findViewById(R.id.ivBlindspotLikeOutline);
            ivBlindspotLikeOutline.setImageResource(R.drawable.like_outline);
            ImageView ivBlindspotDislikeOutline = findViewById(R.id.ivBlindspotDislikeOutline);
            ivBlindspotDislikeOutline.setImageResource(R.drawable.dislike_icon);
            TextView tvBlindspotor = findViewById(R.id.tvBlindspotor);
            tvBlindspotor.setText(R.string.disliked);
            TextView tvBlindspotstatus = findViewById(R.id.tvBlindspotLoved);
            tvBlindspotstatus.setText(getString(R.string.hated_blindspot));
            TextView tvBlindspotRecommendations = findViewById(R.id.tvBlindspotRecommendations);
            tvBlindspotRecommendations.setText(getString(R.string.dislikedrecommendations_blindspot));
        }
    }

    // Method to launch Implicit Intent to load the web browser

    private void launchAmazonWeb() {
        Uri webpage = Uri.parse("https://www.amazon.co.uk/gp/product/B07S7QWV27");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void launchGooglePlayWeb() {
        Uri webpage = Uri.parse("https://play.google.com/store/tv/show/Blindspot?id=OtezusYkH1Y&hl=en&gl=GB");
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
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_blindspot_liked), blindspot_liked);

        // Apply the edits to Shared Preferences
        sharedPrefsEditor.apply();
    }
}
