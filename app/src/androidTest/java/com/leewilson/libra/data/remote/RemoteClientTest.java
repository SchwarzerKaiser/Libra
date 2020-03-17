package com.leewilson.libra.data.remote;

import androidx.test.rule.ActivityTestRule;

import com.leewilson.libra.views.MainActivity;

import org.junit.Rule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


public class RemoteClientTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
}