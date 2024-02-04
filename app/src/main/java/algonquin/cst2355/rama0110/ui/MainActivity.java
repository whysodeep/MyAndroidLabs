package algonquin.cst2355.rama0110.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
// import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2355.rama0110.R;
import algonquin.cst2355.rama0110.databinding.ActivityMainBinding;
import algonquin.cst2355.rama0110.data.MainViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private TextView mytext;
    private Button mybutton;
    private EditText myedittext;
    private CheckBox checkBox;
    private Switch switchButton;
    private RadioButton radioButton;
    private ActivityMainBinding variableBinding;
    private MainViewModel model;
    private ImageView myImageView;
    private ImageButton myImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        // setContentView(R.layout.activity_main);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        mytext = findViewById(R.id.textview);
        mybutton = findViewById(R.id.mybutton);
        myedittext = findViewById(R.id.myedittext);
        checkBox = findViewById(R.id.checkbox);
        switchButton = findViewById(R.id.switchBox);
        radioButton = findViewById(R.id.radioButton);
        myImageView = findViewById(R.id.myimageview);
        myImageButton = findViewById(R.id.myimagebutton);

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editString = myedittext.getText().toString();
                mytext.setText("Your edit text has: " + editString);
                model.setEditTextValue(editString);
            }
        });

        // Observe the LiveData and update the UI components accordingly
        model.setCheckboxState().observe(this, isChecked -> {
            if (isChecked != null) {
                checkBox.setChecked(isChecked);
                showToast("Checkbox state: " + isChecked);
            }
        });

        model.setSwitchState().observe(this, isChecked -> {
            if (isChecked != null) {
                switchButton.setChecked(isChecked);
                showToast("Switch state: " + isChecked);
            }
        });

        model.setRadioButtonState().observe(this, checkedId -> {
            if (checkedId != null) {
                radioButton.setChecked(checkedId == R.id.radioButton);
                showToast("Radio button state: " + (checkedId == R.id.radioButton));
            }
        });

        // Set listeners for CompoundButtons using Lambda expressions
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setCheckboxState().postValue(isChecked));
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> model.setSwitchState().postValue(isChecked));
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                model.setRadioButtonState().postValue(R.id.radioButton);
            }
        });

        // Set up click listener for ImageButton
        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = myImageButton.getWidth();
                int height = myImageButton.getHeight();

                showToast("The width = " + width + " and height = " + height);
            }
        });

        // Load an image into the ImageView
        myImageView.setImageResource(R.drawable.algonquin_icon);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}