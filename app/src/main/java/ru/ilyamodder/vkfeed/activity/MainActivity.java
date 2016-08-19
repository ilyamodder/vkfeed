package ru.ilyamodder.vkfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

import ru.ilyamodder.vkfeed.R;

public class MainActivity extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!VKSdk.isLoggedIn()) {
            LoginActivity.startAsRootActivity(this);
        }
    }
}
