package ru.ilyamodder.vkfeed.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.presenter.LoginPresenter;
import ru.ilyamodder.vkfeed.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    public static void startAsRootActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        context.startActivity(mainIntent);
    }

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this, this);
    }

    @OnClick(R.id.buttonLogin)
    public void onClick() {
        mPresenter.onLoginButtonClicked();
    }

    @Override
    public void showMainActivity() {
        MainActivity.start(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mPresenter.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
