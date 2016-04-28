package com.unbelievable.tangweny.apportion;

import android.app.Application;
import android.content.Context;

import com.unbelievable.tangweny.apportion.util.CrashHandlerUtil;

public class BaseApplication extends Application {
    private static BaseApplication application;
    private static Context mcontext;



    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mcontext = this.getApplicationContext();

		CrashHandlerUtil crashHandler = CrashHandlerUtil.getInstance();
        crashHandler.init(getApplicationContext());

    }

    public synchronized static BaseApplication getInstance() {
        if (null == application) {
            application = new BaseApplication();
        }
        return application;
    }
}
