package com.example.chat;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class LoginTest {

    @Rule
    public IntentsTestRule intentsRule = new IntentsTestRule(Login.class);

    @Test
    public void testLoginToRegisterNavigation() {
        Espresso.onView(withId(R.id.register)).perform(ViewActions.click());
        intended(hasComponent(Registration.class.getName()));
        pressBack();
    }

    @Test
    public void testRegisterToLoginNavigation() {
        Espresso.onView(withId(R.id.register)).perform(ViewActions.click());
        intended(hasComponent(Registration.class.getName()));
        Espresso.onView(withId(R.id.login)).perform(ViewActions.click());
    }
    @Test
    public void testEnter() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("testt"));
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText("testt"));
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());
        Espresso.onView(isRoot()).perform(waitFor(5000));
        intended(hasComponent(MainActivity.class.getName()));
        pressBack();
    }
    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
}
