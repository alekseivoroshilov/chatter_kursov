package com.example.chat;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
        //intended(hasComponent(Login.class.getName()));
    }
    /*@Test
    public void testEnter() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("testt"));
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText("testt"));
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());
        intended(hasComponent(MainActivity.class.getName()));
    }*/
}