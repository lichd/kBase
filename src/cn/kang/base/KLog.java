package cn.kang.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class KLog {
    public static boolean LOG_ENABLED = true;
    public static boolean TRACE_ENABLED = true;
    private static String TAG = "TRACE";
    private static final String TAG_CONTENT_PRINT = "%s:%s.%s:%d";

    private KLog() {
    }

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
            Log.d(TAG, stack!=null?stack.toString():"");
        }
    }

    //打印默认TAG的LOG
    public static void traceStack() {
        if (LOG_ENABLED) {
            traceStack(TAG, Log.ERROR);
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



    //默认TAG和指定内容的方法
    public static void print(String msg) {
        if (LOG_ENABLED) {
            Log.d(TAG, format(msg));
        }
    }

    private static String format(String msg) {
        if (TRACE_ENABLED) {
            //return getCurrentStackTraceElement(5) + " >> " + msg;
            return msg + " ["+getCurrentStackTraceElement(5)+"]";
        }
        return msg;
    }

    private static StackTraceElement getCurrentStackTraceElement(int num) {
        StackTraceElement[] sts= Thread.currentThread().getStackTrace();
        return Thread.currentThread().getStackTrace()[num];

    }




    //==========================暂时不用的方法=============================

    //获取LOG
    private static String getContent(StackTraceElement trace) {
        return String.format(TAG_CONTENT_PRINT, TAG, trace.getClassName(), trace.getMethodName(), trace.getLineNumber());
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

            return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId()
                   + "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
        }
        return null;
    }

}
