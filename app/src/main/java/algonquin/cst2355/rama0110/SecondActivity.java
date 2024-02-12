package algonquin.cst2355.rama0110;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private Button callButton;
    private Button ChangePicButton;
    private ImageView profileImageView;
    private String fileName = "ProfilePicture.png";
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        editTextPhone = findViewById(R.id.editTextPhone);
        callButton = findViewById(R.id.callButton);
        ChangePicButton =findViewById(R.id.ChangePicButton);
        profileImageView =findViewById(R.id.profileImageView);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + editTextPhone.getText().toString()));

                startActivity(callIntent);
            }
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
              new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                  @Override
                  public void onActivityResult(ActivityResult result) {
                      if (result.getResultCode() == Activity.RESULT_OK) {
                          Bundle extras = result.getData().getExtras();
                          Bitmap imageBitmap = (Bitmap) extras.get("data");
                          saveBitmapToFile(imageBitmap);
                          profileImageView.setImageBitmap(imageBitmap);
                      }
                  }
              });
              ChangePicButton.setOnClickListener(V -> {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraResult.launch(cameraIntent);
                });


        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome back " + emailAddress);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", "");
        editTextPhone.setText(savedPhoneNumber);

        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            // Load the bitmap from the file
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            // Set the bitmap to ImageView
            profileImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the phone number entered in the EditText to SharedPreferences
        String phoneNumber = editTextPhone.getText().toString();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }

        private void saveBitmapToFile(Bitmap bitmap) {
            FileOutputStream fOut = null;
            try {

                fOut = openFileOutput(fileName, MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fOut != null) {
                        fOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }}


