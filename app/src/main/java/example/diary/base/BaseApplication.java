package example.diary.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.DbUtils;

import java.io.File;

import example.diary.constant.Constant;
import example.diary.db.DbManager;
import example.diary.db.GreenDaoOpenHelper;
import example.diary.db.dao.DaoMaster;
import example.diary.db.dao.DaoSession;
import example.diary.util.MyActivityLifecycleCallbacks;
import example.diary.util.SPUtils;



public class BaseApplication extends Application {

    private static Context mContext;

    public static String account;

    public static final boolean ENCRYPTED = true;

    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Logger.init("your-diary")
                .methodCount(3)
                .hideThreadInfo()
                .logLevel(LogLevel.FULL)
                .methodOffset(2);
        initActivityLifecycle();
        initDataBase();
    }

    public static Context getContext() {
        return mContext;
    }

    private void initActivityLifecycle() {
        SPUtils spPin = new SPUtils(this, Constant.SP_PIN);
        MyActivityLifecycleCallbacks callbacks = new MyActivityLifecycleCallbacks();
        this.registerActivityLifecycleCallbacks(callbacks);
        callbacks.setCallback(new MyActivityLifecycleCallbacks.Callback() {
            @Override
            public void backToForeground() {
                boolean isPinOpen = spPin.getBoolean(Constant.SP_PIN_STATUS, false);
//                if (isPinOpen) {
//                    PinlockActivity.startPinlockActivity(getApplicationContext(), PinlockActivity.ACTION_VERIFY);
//                }
            }

            @Override
            public void foreToBackground() {

            }
        });
    }

    private void initDataBase() {

        File path = new File(Environment.getExternalStorageDirectory(), "/YourDiary/DB/" + "yd-db");
        path.getParentFile().mkdirs();
        GreenDaoOpenHelper helper = null;
        if (path.getParentFile().exists()) {
            helper = new GreenDaoOpenHelper(this, path.getAbsolutePath(), null);
        } else {
            helper = new GreenDaoOpenHelper(this, "yd-db", null);
        }
//        GreenDaoOpenHelper helper = new GreenDaoOpenHelper(this, "yd-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        DbManager.init(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
