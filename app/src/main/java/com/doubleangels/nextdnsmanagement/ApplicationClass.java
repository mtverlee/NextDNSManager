package com.doubleangels.nextdnsmanagement;

import android.app.Application;

import com.onesignal.OneSignal;

import io.sentry.ITransaction;
import io.sentry.Sentry;

public class ApplicationClass extends Application {

    private static final String ONESIGNAL_APP_ID = "dabc92aa-6dc5-4c29-a096-ac6eba076214";
    public ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Override
    public void onCreate() {
        ITransaction ApplicationClass_create_transaction = Sentry.startTransaction("onCreate()", "ApplicationClass");
        super.onCreate();
        try{
            // Set up our notifications.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            OneSignal.initWithContext(this);
            OneSignal.setAppId(ONESIGNAL_APP_ID);
            OneSignal.sendTag("app_version_name", BuildConfig.VERSION_NAME);
            OneSignal.sendTag("app_version_code", String.valueOf(BuildConfig.VERSION_CODE));
        } catch (Exception e){
            exceptionHandler.captureException(e);
        } finally {
            ApplicationClass_create_transaction.finish();
        }
    }
}
