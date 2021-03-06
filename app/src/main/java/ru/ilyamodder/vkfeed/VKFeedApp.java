package ru.ilyamodder.vkfeed;

import android.app.Application;
import android.util.Log;

import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import ru.ilyamodder.vkfeed.activity.LoginActivity;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.api.VKRemoteRepository;
import ru.ilyamodder.vkfeed.database.SqlBriteDaoRepository;

/**
 * Created by ilya on 20.08.16.
 */

public class VKFeedApp extends Application {
    VKAccessTokenTracker mVkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                LoginActivity.startAsRootActivity(getApplicationContext());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mVkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        if (BuildConfig.DEBUG) {
            Log.d("fingerprint", VKUtil.getCertificateFingerprint(this, this.getPackageName())[0]);
        }

        SqlBriteDaoRepository daoRepository = new SqlBriteDaoRepository(this);

        RepositoryProvider.setLocalRepository(daoRepository);
        RepositoryProvider.setRemoteRepository(new VKRemoteRepository());

        DaoManager daoManager = DaoManager.with(this)
                .databaseName("cache.db")
                .version(1)
                .add(daoRepository)
                .logging(BuildConfig.DEBUG)
                .build();

    }
}
