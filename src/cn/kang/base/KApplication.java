package cn.kang.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;

import cn.kang.base.crash.handler.CrashHandlerFactory;

public class KApplication extends Application {
    //调试日志
    private String TAG = KApplication.class.getSimpleName();
    private static KApplication self;

    public static KApplication getSharedApplication() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.d(TAG, "执行Application.onCreate");
        self = this;
        //保存崩溃日志,开启友盟之类统计的话,不需要开启
        initCrashEngine();
        //开启StrictMode监控应用运行情况
        initStrictMode();

    }


    private void initStrictMode() {
        // 启用StrictMode条件:
        // 1.SDK版本大约2.3
        // 2.调试模式或者开发模式
        if (((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 || K.config.DEVELOPER_MODE) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            //线程策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDialog().build());
            //VM策略
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build());
        }
    }

    private void initCrashEngine() {
        //KSimpleCrashHandler crashHandler = KSimpleCrashHandler.getInstance();
        //crashHandler.init(this);
        //发送Email方式
        CrashHandlerFactory.initHandler(CrashHandlerFactory.EMAIL_REPORT, this);
    }

}
