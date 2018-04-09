package com.jfeng.usesodemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jfeng.bspatchlibrary.BsPatch;
import com.jfeng.usesodemo.utils.ApkExtract;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    private Button btnPatch;

    private String currInstallAppApk;

    private String currVersion = "";

    private TextView tvInstallApkPath;

    private TextView tvpatchPath;

    private TextView tvTargetapkPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        btnPatch = (Button) findViewById(R.id.btnpatch);

        btnPatch.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {//判断是否有sdcard读写权限,没有权限则动态申请
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android
                            .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
                else
                {//有sdcard读写权限
                    doBspatch();
                }
            }
        });

        currInstallAppApk = ApkExtract.extract(this);
        currVersion = "v" + getVesionName();

        tvInstallApkPath = (TextView) findViewById(R.id.tvusedemoapkpath);

        tvInstallApkPath.setText(currInstallAppApk);

        TextView tvVerInfos = (TextView) findViewById(R.id.tvversioninfos);
        tvVerInfos.setText("usesodemo-" + currVersion);

        tvpatchPath = (TextView) findViewById(R.id.tvpatchpath);
        tvTargetapkPath = (TextView) findViewById(R.id.tvtargetapkpath);
    }

    private String getVesionName()
    {
        String versionName = "";
        try
        {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return versionName;
    }

    private class BsPatchTask extends AsyncTask<Void, Void, Boolean>
    {
        private Context context;
        private File oldApk;
        private File destApk;
        private File patch;
        private File targetApk;

        public BsPatchTask(Context context, File oldApk, File destApk, File patch, File targetApk)
        {
            this.context = context;
            this.oldApk = oldApk;
            this.destApk = destApk;
            this.patch = patch;
            this.targetApk = targetApk;
        }

        @Override protected void onPreExecute()
        {
            super.onPreExecute();

            Log.d("jfeng", "begin to patch oldApk destApk patch ... ");
        }

        @Override protected Boolean doInBackground(Void... params)
        {
            //真实升级环境下这里第一个参数值换成ApkExtract.extract(this)
            BsPatch.bspatch(oldApk.getAbsolutePath(),
                    destApk.getAbsolutePath(),
                    patch.getAbsolutePath());

            Log.d("jfeng", "new destApk " + destApk.getAbsolutePath
                    () + " , " + destApk.exists());

            if (destApk.exists())
            {
                String md5TargetApk = ApkExtract.getFileMD5(targetApk);

                String md5DestApk = ApkExtract.getFileMD5(destApk);

                Log.d("jfeng", "destApk,md5(destApk) = " + md5DestApk);

                Log.d("jfeng", "targetApk,md5(targetApk) = " + md5TargetApk);

                //ApkExtract.install(this, destApk.getAbsolutePath());

                if (md5DestApk.equals(md5TargetApk))
                {
                    return true;
                }
            }

            return false;
        }

        @Override protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);

            if (aBoolean)
            {
                ApkExtract.install(context, destApk.getAbsolutePath());
            }
        }
    }

    private void doBspatch()
    {
        final File oldApk = new File(currInstallAppApk);//new File(Environment
        // .getExternalStorageDirectory(), "ordermgr_v2.4.3_release.apk");
        final File newdestApk = new File(Environment.getExternalStorageDirectory(),
                "new-usesodemo.apk");
        final File patch = new File(Environment.getExternalStorageDirectory(), "usesodemo" +
                currVersion + "-v2.patch");
        final File targetApk = new File(Environment.getExternalStorageDirectory(),
                "usesodemo-v2.apk");

        tvInstallApkPath.setText("oldApk.exists()= " + oldApk.exists() + " , " + oldApk
                .getAbsolutePath());
        tvpatchPath.setText("patch.exists()= " + patch.exists() + " , " + patch.getAbsolutePath());
        tvTargetapkPath.setText("targetApk.exists()= " + targetApk.exists() + " , " + targetApk
                .getAbsolutePath());

        if (!oldApk.exists())
        {//旧的apk文件不存在则直接返回
            return;
        }

        if (!patch.exists())
        {//旧的patch文件不存在则直接返回
            return;
        }

        if (!targetApk.exists())
        {//预期的 targetApk文件不存在则直接返回
            return;
        }

        if (newdestApk.exists())
        {//如果dest.apk之前存在则先删除

            Log.d("jfeng", "destApk.exists()=true,delete it !");
            newdestApk.delete();
        }

        BsPatchTask task = new BsPatchTask(this, oldApk, newdestApk, patch, targetApk);
        task.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                doBspatch();
            }
        }
    }
}
