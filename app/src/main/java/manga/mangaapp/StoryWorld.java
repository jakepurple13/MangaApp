package manga.mangaapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;

import io.fabric.sdk.android.Fabric;
import manga.mangaapp.manymanga.sites.Site;

/**
 * Created by Jacob on 8/23/17.
 */

public class StoryWorld extends Application {

    static Site site;

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SharedPreferencesManager.init(this, true);
        StoryWorld.context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return context;
    }

    public static String getResource(int resid) {
        return context.getString(resid);
    }

    public static void setSite(Site s) {
        site = s;
    }

    public static Site getSite() {
        return site;
    }

}
