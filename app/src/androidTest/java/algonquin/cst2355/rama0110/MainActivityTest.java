package algonquin.cst2355.rama0110;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * sample test case which only checks with numbers and the text displays
     * "you shall not pass"...
     */
    @Test
    public void mainActivityTest() {


        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());


        ViewInteraction materialButton = onView(withId(R.id.loginButton));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * Test case for checking if the password is missing an uppercase letter.
     * Verifies that the TextView displays "You shall not pass!".
     */
    @Test
    public void testFindMissingUpperCase() {
        //finds the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //types the password without uppercase
        appCompatEditText.perform(replaceText("deepxraman1234!"));

        // finds the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // clicks on button
        materialButton.perform(click());

        // finds the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        // checks the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for checking if the password is missing an lowercase letter.
     * Verifies that the TextView displays "You shall not pass!".
     */
    @Test
    public void testFindMissingLowerCase(){
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("DEEPXRAMAN1234!"));

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for checking if the password is missing digits.
     * Verifies that the TextView displays "You shall not pass!".
     */
    @Test
    public void testFindMissingDigt(){
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Deepxraman!"));

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for checking if the password is missing a special character.
     * Verifies that the TextView displays "You shall not pass!".
     */
    @Test
    public void testFindMissingSplCharacter(){
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Deepxraman1234"));

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for checking if the password meets all complexity requirements.
     * Verifies that the TextView displays "Your password meets the requirements".
     */

    @Test
    public void testGoodPassword(){
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Deepxraman1234!"));

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("Your password meets the requirements")));
    }



}