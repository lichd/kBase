package cn.kang.base.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.kang.base.K;


public class KSimpleCrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = KSimpleCrashHandler.class.getSimpleName();
    private UncaughtExceptionHandler mDefaultHandler;
    private static KSimpleCrashHandler INSTANCE = new KSimpleCrashHandler();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final int LOG_FILE_SAVE_DAYS = 5; // 日志文件保存天数
    private String log_path="";

    /**
     * 保证只有一个CrashHandler实例
     */
    private KSimpleCrashHandler() {

    }

    public static KSimpleCrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        log_path=getLogPath();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        // 删除过期文件
        delOverdueLogFile();
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            String time = formatter.format(new Date());
            String fileName = time + ".log";
            File dir = new File(getLogPath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(getLogPath() + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除过期日志文件
     */
    private void delOverdueLogFile() {
        File file = new File(getLogPath());
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                String fileName = logFile.getName();
                String createDateInfo = getFileNameWithoutExtension(fileName);
                if (isDel(createDateInfo)) {
                    logFile.delete();
                }
            }
        }
    }

    /**
     * 去除文件的扩展类型(.log)
     *
     * @param fileName
     * @return
     */
    private String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * 判断文件是否属于过期的日志文件
     *
     * @param data
     * @return
     */
    private boolean isDel(String data) {
        boolean canDel = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1 * LOG_FILE_SAVE_DAYS);// 删除7天之前日志
        Date expiredDate = calendar.getTime();
        try {
            Date createDate = formatter.parse(data);
            canDel = createDate.before(expiredDate);
        } catch (ParseException e) {
            e.printStackTrace();
            canDel = false;
        }
        return canDel;
    }

    private String getLogPath() {
        String logPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()) {
            logPath = Environment.getExternalStorageDirectory().getPath() + File.separator + K.config.LOG_DATA_DIR + File.separator;
            File file = new File(logPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            logPath = mContext.getFilesDir().getAbsolutePath() + File.separator + K.config.LOG_DATA_DIR + File.separator;
            File file = new File(logPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return logPath;
    }
}
