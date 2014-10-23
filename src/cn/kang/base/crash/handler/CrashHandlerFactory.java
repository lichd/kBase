package cn.kang.base.crash.handler;

import android.content.Context;

public class CrashHandlerFactory {
    public static int EMAIL_REPORT=1;
    public static int HTTP_REPORT=2;

    public static void initHandler(int type,Context context){
        if(type==EMAIL_REPORT){
            initCrashEmailReport(context);
        }else if(type==HTTP_REPORT){
            initCrashHttpReport(context);
        }
    }

    private static void initCrashEmailReport(Context context){
        CrashEmailReport report = new CrashEmailReport(context);
        report.setReceiver("lichaode@kang.cn");
        report.setSender("lichaode@kang.cn");
        report.setSendPassword("richard_522");
        report.setSMTPHost("smtp.kang.cn");
        report.setPort("465");
        report.start();
    }

    private static void initCrashHttpReport(Context context){
        CrashHttpReport report = new CrashHttpReport(context);
        report.setUrl("http://test.com/ReportFile");
        report.setTo("log@comon.cn");
        report.setToParam("to");
        report.setTitleParam("subject");
        report.setBodyParam("message");
        report.setFileParam("fileName");
        report.setCallback(new CrashHttpReport.HttpReportCallback() {
            @Override
            public boolean isSuccess(int i, String s) {
                return s.endsWith("ok");
            }
        });
        report.start();
    }
}
