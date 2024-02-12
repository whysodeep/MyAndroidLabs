package algonquin.cst2355.rama0110;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private Button loginButton;
    private EditText emailEt;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "In onCreate() - Loading Widgets" );

        loginButton = findViewById(R.id.loginButton);
        emailEt = findViewById(R.id.emailEt);
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String emailAddress = prefs.getString("LoginName", "");
        emailEt.setText(emailAddress);

        loginButton.setOnClickListener(v -> nextPage());
    }
        private void nextPage(){

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailEt.getText().toString());
            editor.apply();

        Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress",emailEt.getText().toString());
            startActivity( nextPage);
        }
    @Override
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "The application is now visible on screen." );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(TAG, "The application is now responding to user input" );

    }
    @Override
    protected void onPause() {
        super.onPause();

        Log.w(TAG, "The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w(TAG, "The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w(TAG, "Any memory used by the application is freed." );
    }





}

