package uk.ac.rgu.rectv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // get the AppName button
        Button btnAppName = findViewById(R.id.btnAppName);

        // set the click listener to the btnAppName burtton
        btnAppName.setOnClickListener(this);

        // get the Down Arrow image
        ImageView ivDownArrow = findViewById(R.id.ivDownArrow);

        // set the click listener to the ivDownArrow
        ivDownArrow.setOnClickListener(this);
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

        else if (view.getId() == R.id.ivDownArrow) {
            // create an intent
            Intent intent = new Intent(getApplicationContext(), ShowPart2Activity.class);
            // start the Activity
            startActivity(intent);
        }
    }
}
