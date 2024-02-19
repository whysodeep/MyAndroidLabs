package algonquin.cst2355.rama0110;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity class represents the main activity of the Android application.
 * It checks the complexity of a password entered by the user.
 * @author Ramandeep Singh
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * this holds text at the center of the screen.
     */
    private TextView tw = null;
    /**
     * this holds edittext for the password field and is set horizontally.
     */
    private EditText et = null;
    /**
     * this is login button and holds the button in the centre.
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        tw = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.loginButton);

        // Set onClickListener for the button
        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if (checkPasswordComplexity(password)) {
                // Password meets requirements
                tw.setText("Your password meets the requirements");
            } else {
                // Password does not meet requirements
                tw.setText("You shall not pass!");
            }
        });
    }

    /**
     * Checks the complexity of the password.
     *
     * @param password The password to be checked.
     * @return True if the password meets the complexity requirements, otherwise false.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        // Iterate over each character in the password
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        // Check if all complexity requirements are met
        if (!foundUpperCase) {
            showToast("Your password does not have an upper case letter");
            return false;
        } else if (!foundLowerCase) {
            showToast("Your password does not have a lower case letter");
            return false;
        } else if (!foundNumber) {
            showToast("Your password does not have a number");
            return false;
        } else if (!foundSpecial) {
            showToast("Your password does not have a special symbol");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the character is a special character.
     *
     * @param c The character to be checked.
     * @return True if the character is a special character, otherwise false.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }
    }

    /**
     * Displays a toast message.
     *
     * @param message The message to be displayed in the toast.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
