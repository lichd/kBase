package cn.kang.base.system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

import java.util.List;

/**
 * Created by lichd on 2014/10/21.
 */
public final class SystemUtil {
    public static void getMemoryInfoByAm(Context context){
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(outInfo);
        //可用空闲内存
        System.out.println(outInfo.availMem);
    }

    public static void getMemoryInfoByDebug(Context context,int[] pids){
        //查询PSS，VSS，USS等单个进程使用内存信息
        Debug.MemoryInfo[] memoryInfoArray = getActivityManager(context).getProcessMemoryInfo(pids);
        Debug.MemoryInfo pidMemoryInfo=memoryInfoArray[0];
        //USS
        System.out.println(pidMemoryInfo.getTotalPrivateDirty());
        //PSS
        System.out.println(pidMemoryInfo.getTotalSharedDirty());
    }

    private static ActivityManager getActivityManager(Context context){
        return (ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 返回当前的应用是否处于前台显示状态
     * @param packageName
     * @return
     */
    public static boolean isTopActivity(Context context,String packageName)
    {
        //_context是一个保存的上下文
        ActivityManager __am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> __list = __am.getRunningAppProcesses();
        if(__list.size() == 0) return false;
        for(ActivityManager.RunningAppProcessInfo __process:__list)
        {
            if(__process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
               __process.processName.equals(packageName))
            {
                return true;
            }
        }
        return false;
    }
}
