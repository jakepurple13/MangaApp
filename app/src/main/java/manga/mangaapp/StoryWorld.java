package manga.mangaapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;

/**
 * Created by Jacob on 8/23/17.
 */

public class StoryWorld extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this, true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
