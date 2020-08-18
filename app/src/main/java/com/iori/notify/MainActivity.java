package com.iori.notify;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID = "2882303761518600282";
    public static final String APP_KEY = "5181860084282";

    private ClipboardManager mClipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
            MiPushClient.setAlias(this, "iori", null);
            MiPushClient.setUserAccount(this, "iori", null);
            MiPushClient.subscribe(this, "iori", null);
            String regId = MiPushClient.getRegId(this);
            if (!TextUtils.isEmpty(regId)) {
                Toast.makeText(this, "注册成功,RegId=" + regId, Toast.LENGTH_LONG).show();
                ((TextView) findViewById(R.id.regId)).setText(regId);
                ((TextView) findViewById(R.id.account)).setText("iori");
                ((TextView) findViewById(R.id.subscribe)).setText("iori");
            }

            ((TextView) findViewById(R.id.regId)).setOnClickListener(new Click());
            ((TextView) findViewById(R.id.account)).setOnClickListener(new Click());
            ((TextView) findViewById(R.id.subscribe)).setOnClickListener(new Click());
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getApplicationInfo().processName;
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String t = ((TextView) v).getText().toString();
            mClipboardManager.setPrimaryClip(ClipData.newPlainText(t, t));
            Toast.makeText(v.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
        }
    }
}
