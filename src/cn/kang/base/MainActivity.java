package cn.kang.base;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        KLog.trace();
        KLog.traceStack();
        KLog.print("ssssssssssss");
        KLog.d("ddd","sdsfsdfsdf");
        KLog.wtf("ss","sssssssssssss");
	}
}
