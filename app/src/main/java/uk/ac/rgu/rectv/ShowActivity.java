package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG to be used when logging
    private static final String TAG = RecommendationsActivity.class.getCanonicalName();

    // constant for downloading show data
    private static final String SHOW_URL_TEMPLATE = "http://www.omdbapi.com/?t=%s&apikey=4c1aac0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // get the AppName button
        Button btnAppName = findViewById(R.id.btnAppName);

        // set the click listener to the btnAppName burtton
        btnAppName.setOnClickListener(this);

        // Download show description from Trakt API
        downloadShowDescription();
    }

    public void downloadShowDescription(){
        // Downloads and displays a show name from Trakt API
        String getShowNameForShowMetadata = (getString(R.string.personofinterest_name));

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
                        TextView tvShowNameDisplay = findViewById(R.id.tvShowDescription);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String titleObj = responseObj.getString("Plot");
                            // add the title to the display
                            showDescription.append("\n")
                                    .append(responseObj.getString("Plot"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (showDescription.length() == 0){
                            tvShowNameDisplay.setText(getString(R.string.showdetails_json_error));
                        } else {
                            tvShowNameDisplay.setText(showDescription.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tvShowNameDisplay = findViewById(R.id.tvShowDescription);
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
        if (view.getId() == R.id.btnAppName) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // start the Activity
            startActivity(intent);
        }
    }
}
