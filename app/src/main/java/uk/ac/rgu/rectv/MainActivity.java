package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the Continue button
        Button btnContinue = findViewById(R.id.btnContinue);

        // set the click listener to the btnContinue Button
        btnContinue.setOnClickListener(this);
    }

    // Changing Activity
    @Override
    public void onClick(View view) {
        // view is the View (Button, ExitText, TextView, etc) that was clicked
        if (view.getId() == R.id.btnContinue) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), RecommendationsActivity.class);
            // start the Activity
            startActivity(intent);
        }
    }
}
