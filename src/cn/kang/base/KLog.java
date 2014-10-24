package cn.kang.base;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class KLog {
    public static boolean LOG_ENABLED = true;
    public static boolean TRACE_ENABLED = true;
    private static String TRACE = "TRACE";
    private String TAG = "TAG";
    private static final String TAG_CONTENT_PRINT = "%s:%s.%s:%d";

    private KLog() {
    }

    private KLog(Class<?> clazz) {
        TAG = initTag(clazz);
    }

    protected String initTag(Class<?> clazz) {
        return String.format("%s: %s", Build.VERSION.RELEASE, clazz.getSimpleName());
    }

    public static final KLog getLog(Class<?> clazz) {
        return new KLog(clazz);
    }

    //使用实例方式,用类名作为TAG记录日志
    public void d(String msg) {
        if (LOG_ENABLED) Log.d(TAG, format(msg));
    }

    public void i(String msg) {
        if (LOG_ENABLED) Log.i(TAG, format(msg));
    }

    public void e(String msg) {
        if (LOG_ENABLED) Log.e(TAG, format(msg));
    }

    public void w(String msg) {
        if (LOG_ENABLED) Log.w(TAG, format(msg));
    }

    public void v(String msg) {
        if (LOG_ENABLED) Log.v(TAG, format(msg));
    }

    public void wtf(String msg) {
        if (LOG_ENABLED) Log.wtf(TAG, format(msg));
    }

    //使用默认TAG记录日志,仅仅输出记录而已
    public static void print(String msg) {
        if (LOG_ENABLED) Log.d(TRACE, format(msg));
    }

    public static void error(String msg) {
        if (LOG_ENABLED) Log.e(TRACE, format(msg));
    }

    //自定义TAG输出日志,使用系统Log
    public static void d(String tag, String msg) {
        if (LOG_ENABLED) Log.d(tag, format(msg));
    }

    public static void i(String tag, String msg) {
        if (LOG_ENABLED) Log.i(tag, format(msg));
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLED) Log.e(tag, format(msg));
    }

    public static void w(String tag, String msg) {
        if (LOG_ENABLED) Log.w(tag, format(msg));
    }

    public static void v(String tag, String msg) {
        if (LOG_ENABLED) Log.v(tag, format(msg));
    }

    public static void wtf(String tag, String msg) {
        if (LOG_ENABLED) Log.wtf(tag, format(msg));
    }

    //自定义TAG输出日志,使用系统Log
    public static void d(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.d(tag, format(msg), r);
    }

    public static void i(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.i(tag, format(msg), r);
    }

    public static void e(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.e(tag, format(msg), r);
    }

    public static void w(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.w(tag, format(msg), r);
    }

    public static void v(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.v(tag, format(msg), r);
    }

    public static void wtf(String tag, String msg, Throwable r) {
        if (LOG_ENABLED) Log.wtf(tag, format(msg), r);
    }


    //使用类名作为TAG输出日志,使用系统Log
    public static void d(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.d(_class.getSimpleName(), format(msg));
    }

    public static void i(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.i(_class.getSimpleName(), format(msg));
    }

    public static void e(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.e(_class.getSimpleName(), format(msg));
    }

    public static void w(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.w(_class.getSimpleName(), format(msg));
    }

    public static void v(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.v(_class.getSimpleName(), format(msg));
    }

    public static void wtf(Class<?> _class, String msg) {
        if (LOG_ENABLED) Log.wtf(_class.getSimpleName(), format(msg));
    }

    public static String getStackTraceString(Throwable tr) {
        if (LOG_ENABLED) return Log.getStackTraceString(tr);
        else return "";
    }

    /**
     * 使用Toast打印日志提示
     */
    public static void toast(Context context, String content) {
        if (LOG_ENABLED) {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        }
    }

    //打印LOG
    public static void trace() {
        if (LOG_ENABLED) {
            StackTraceElement stack = getCurrentStackTraceElement(4);
            Log.d(TRACE, stack != null ? stack.toString() : "");
        }
    }

    //打印默认TAG的LOG
    public static void traceStack() {
        if (LOG_ENABLED) {
            traceStack(TRACE, Log.ERROR);
        }
    }

    // 打印Log当前调用栈信息
    public static void traceStack(String tag, int priority) {
        if (LOG_ENABLED) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StringBuilder str = new StringBuilder();
            str.append(stackTrace[4].toString());
            for (int i = 5; i < stackTrace.length; i++) {
                str.append("\n").append(stackTrace[i].toString());
            }
            Log.println(priority, tag, str.toString());
        }
    }




    private static String format(String msg) {
        if (TRACE_ENABLED) {
            //return getCurrentStackTraceElement(5) + " >> " + msg;
            return msg + " [" + getCurrentStackTraceElement(5) + "]";
        }
        return msg;
    }

    private static StackTraceElement getCurrentStackTraceElement(int num) {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        return Thread.currentThread().getStackTrace()[num];

    }


    //==========================暂时不用的方法=============================

    //获取LOG
    private static String getContent(StackTraceElement trace) {
        return String.format(TAG_CONTENT_PRINT, TRACE, trace.getClassName(), trace.getMethodName(), trace.getLineNumber());
    }

    // 打印Log当前调用栈信息
    private static void traceStack2(String tag, int priority) {
        if (LOG_ENABLED) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            Log.println(priority, tag, stackTrace[4].toString());
            StringBuilder str = new StringBuilder();
            String prevClass = null;
            for (int i = 5; i < stackTrace.length; i++) {
                String className = stackTrace[i].getFileName();
                int idx = className.indexOf(".java");
                if (idx >= 0) {
                    className = className.substring(0, idx);
                }
                if (prevClass == null || !prevClass.equals(className)) {
                    str.append(className.substring(0, idx));
                }
                prevClass = className;
                str.append(".").append(stackTrace[i].getMethodName()).append(":").append(stackTrace[i].getLineNumber()).append("->");
            }
            Log.println(priority, tag, str.toString());
        }
    }

    private String getContent2() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
        }
        return null;
    }

}
