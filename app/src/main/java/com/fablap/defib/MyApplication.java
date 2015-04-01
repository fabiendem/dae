package com.fablap.defib;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Fabien on 12/11/14.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        if(BuildConfig.IS_CRASHLYTICS_ENABLED) {
            Fabric.with(this, new Crashlytics());
        }
    }
}
