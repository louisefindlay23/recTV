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

    private SharedPreferences sharedPrefs;
    private boolean ncisla_liked;

    // TAG to be used when logging
    private static final String TAG = ShowNCISLA2Activity.class.getCanonicalName();

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%s/similar?api_key=71fe3c36cb7df73b77feb2703d8c178c&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ncisla2);

        // get the Back Arrow button
        ImageView ivNCISLABackArrow = findViewById(R.id.ivNCISLABackArrow);

        // set the click listener to the back arrow
        ivNCISLABackArrow.setOnClickListener(this);

        // get the Prime Video image
        ImageView ivNCISLAPrimeVideo = findViewById(R.id.ivNCISLAPrimeVideo);

        // set the click listener to the Prime Video image
        ivNCISLAPrimeVideo.setOnClickListener(this);

        // get the Like Outline image
        ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);

        // set the click listener to the like outline
        ivNCISLALikeOutline.setOnClickListener(this);

        // get the Dislike Outline image
        ImageView ivNCISLADislikeOutline = findViewById(R.id.ivNCISLADislikeOutline);

        // set the click listener to the dislike outline
        ivNCISLADislikeOutline.setOnClickListener(this);

        // instantiate sharedPrefs
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);

        // restore the value of if a TV Show was liked or not
        ncisla_liked = sharedPrefs.getBoolean(getString(R.string.shared_pref_ncisla_liked), true);

        // If the show is liked or disliked
        if(ncisla_liked == true){
            ivNCISLALikeOutline.setImageResource(R.drawable.like_icon);
            ivNCISLADislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvNCISLAor = findViewById(R.id.tvNCISLAor);
            tvNCISLAor.setText(getString(R.string.liked));
            // Download related shows
            downloadRelatedShows();
        } else if (ncisla_liked == false) {
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
        // Downloads and displays a show description from the OMDB API
        String getShowNameForShowMetadata = (getString(R.string.ncisla_tmbdid));

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
                        StringBuilder NCISLARecommendations = new StringBuilder();
                        TextView tvNCISLARecommendations = findViewById(R.id.tvNCISLARecommendations);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray resultsArray = responseObj.getJSONArray("results");
                            for (int i = 0, j = resultsArray.length(); i < j ; i++){
                                JSONObject resultsObj = resultsArray.getJSONObject(i);
                                NCISLARecommendations.append(resultsObj.getString("name")).append("\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (NCISLARecommendations.length() == 0){
                            tvNCISLARecommendations.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvNCISLARecommendations.setText(NCISLARecommendations.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvNCISLADescriptionDisplay = findViewById(R.id.tvNCISLAShowDescription);
                tvNCISLADescriptionDisplay.setText(getString(R.string.showdetails_download_error, error.getLocalizedMessage()));
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
        if (view.getId() == R.id.ivNCISLABackArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowNCISLAActivity.class);
            // start the Activity
            startActivity(intent);
            // To launch web browser when Amazon Logo is clicked
        } else if (view.getId() == R.id.ivNCISLAPrimeVideo) {
            launchWeb();
        } else if (view.getId() == R.id.ivNCISLALikeOutline) {
            // Set Shared Preferences variable to true
            ncisla_liked = true;
            // Change image to LikeIcon
            ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);
            ivNCISLALikeOutline.setImageResource(R.drawable.like_icon);
            ImageView ivNCISLADislikeOutline = findViewById(R.id.ivNCISLADislikeOutline);
            ivNCISLADislikeOutline.setImageResource(R.drawable.dislike_outline);
            TextView tvNCISLAor = findViewById(R.id.tvNCISLAor);
            tvNCISLAor.setText(getString(R.string.liked));
            TextView tvNCISLAstatus = findViewById(R.id.tvNCISLALoved);
            tvNCISLAstatus.setText(getString(R.string.loved_ncisla));
            // Download related shows
            downloadRelatedShows();
        } else if (view.getId() == R.id.ivNCISLADislikeOutline) {
            // Set Shared Preferences variable to false
            ncisla_liked = false;
            // Change image to LikeIcon
            ImageView ivNCISLALikeOutline = findViewById(R.id.ivNCISLALikeOutline);
            ivNCISLALikeOutline.setImageResource(R.drawable.like_outline);
            ImageView ivNCISLADislikeOutline = findViewById(R.id.ivNCISLADislikeOutline);
            ivNCISLADislikeOutline.setImageResource(R.drawable.dislike_icon);
            TextView tvNCISLAor = findViewById(R.id.tvNCISLAor);
            tvNCISLAor.setText(R.string.disliked);
            TextView tvNCISLAstatus = findViewById(R.id.tvNCISLALoved);
            tvNCISLAstatus.setText(getString(R.string.hated_ncisla));
            TextView tvNCISLARecommendations = findViewById(R.id.tvNCISLARecommendations);
            tvNCISLARecommendations.setText(getString(R.string.dislikedrecommendations_ncisla));
        }
    }

    // Method to launch Implicit Intent to load the web browser

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
        // Get the Shared Preferences editor
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        // Store if Like icon has been clicked or not
        sharedPrefsEditor.putBoolean(getString(R.string.shared_pref_personofinterest_liked), ncisla_liked);

        // Apply the edits to Shared Preferences
        sharedPrefsEditor.apply();
    }
}
