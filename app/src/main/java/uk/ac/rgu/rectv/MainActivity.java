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

        // Get the Continue button which should be made clickable (Adapted code from Course Materials Week 3)
        Button btnContinue = findViewById(R.id.btnContinue);

        // Set the click listener to the btnContinue Button (Adapted code from Course Materials Week 3)
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // If the continue button was clicked, start the Recommendations activity (Adapted code from Course Materials Week 3)
        if (view.getId() == R.id.btnContinue) {
            Intent intent = new Intent(getApplicationContext(), RecommendationsActivity.class);
            startActivity(intent);
        }
    }
}
