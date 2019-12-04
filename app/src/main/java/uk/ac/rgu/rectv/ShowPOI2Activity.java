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

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poi2);

        // get the Back Arrow button
        ImageView ivBackArrow = findViewById(R.id.ivBackArrow);

        // set the click listener to the back arrow
        ivBackArrow.setOnClickListener(this);

        // get the Prime Video image
        ImageView ivPrimeVideo = findViewById(R.id.ivPrimeVideo);

        // set the click listener to the Prime Video image
        ivPrimeVideo.setOnClickListener(this);

        // get the Like Outline image
        ImageView ivLikeOutline = findViewById(R.id.ivLikeOutline);

        // set the click listener to the like outline
        ivLikeOutline.setOnClickListener(this);

        // get the Dislike Outline image
        ImageView ivDislikeOutline = findViewById(R.id.ivDislikeOutline);

        // set the click listener to the dislike outline
        ivDislikeOutline.setOnClickListener(this);

        // instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // restore the value of if a TV Show was liked or not
        personofinterest_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_personofinterest_liked), true);

        personofinterest_LikeorDislike();

    }

    public void personofinterest_LikeorDislike() {
        // If the show is liked or disliked
        if (personofinterest_liked == true) {
            ImageView ivLikeOutline = findViewById(R.id.ivLikeOutline);
            ImageView ivDislikeOutline = findViewById(R.id.ivDislikeOutline);
            ivLikeOutline.setImageResource(R.drawable.like_icon);
            ivDislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvPOIor = findViewById(R.id.tvPOIor);
            tvPOIor.setText(getString(R.string.liked));
            // Download related shows
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
        // Downloads and displays a show description from the OMDB API
        String getShowNameForShowMetadata = (getString(R.string.personofinterest_tmbdid));

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
                        StringBuilder showRecommendations = new StringBuilder();
                        TextView tvRecommendations = findViewById(R.id.tvRecommendations);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                showRecommendations.append(resultsObj.getString("name")).append("\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (showRecommendations.length() == 0){
                            tvRecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvRecommendations.setText(showRecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvRelatedShowDisplay = findViewById(R.id.tvRecommendations);
                tvRelatedShowDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
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
        if (view.getId() == R.id.ivBackArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowPOIActivity.class);
            // start the Activity
            startActivity(intent);
            // To launch web browser when Amazon Logo is clicked
        } else if (view.getId() == R.id.ivPrimeVideo) {
            launchWeb();
    } else if (view.getId() == R.id.ivLikeOutline) {
        // Set Shared Preferences variable to true
        personofinterest_liked = true;
            // Change image to LikeIcon
            personofinterest_LikeorDislike();
    } else if (view.getId() == R.id.ivDislikeOutline) {
        // Set Shared Preferences variable to false
        personofinterest_liked = false;
        // Change image to LikeIcon
            personofinterest_LikeorDislike();
    }
    }

    // Method to launch Implicit Intent to load the web browser

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
        // Get the Shared Preferences editor
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if Like icon has been clicked or not
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_personofinterest_liked), personofinterest_liked);

        // Apply the edits to Shared Preferences
        sharedPrefsEditor.apply();
    }
}
