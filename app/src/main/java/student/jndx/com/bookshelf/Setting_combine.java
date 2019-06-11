package student.jndx.com.bookshelf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

public class Setting_combine extends AppCompatActivity {
    TextView net_servise;
    TextView back_log;
    TextView return_button;
    TextView backup_appl;
    TextView back_recover;
    static boolean douban=true;
    static boolean open=true;
    String file_path2;
    private MyDialog1 myDialog1;
    Calendar cal;
    String year;
    String month;
    String day;
    String hour;
    String minute;
    String second;
    String my_time_1;
    String my_time_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_combine_activity);
        net_servise=(TextView)findViewById(R.id.Net_service);
        return_button=(TextView)findViewById(R.id.return_jiantou);
        return_button.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(Setting_combine.this,MainActivity.class);
                startActivity(intent);
            }

        });
        //网络服务选择触发事件
        net_servise.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting_combine.this);
                builder.setTitle("网络服务");
                final String[] hobbies = {"DouBan.com","OpenLibrary.org"};
                //    设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉多选框的数据集合
                 * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
                 * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
                 * 第三个参数给每一个多选项绑定一个监听器
                 */
                boolean[] select={douban,open};

                //网络服务选择框
                builder.setMultiChoiceItems(hobbies, select, new DialogInterface.OnMultiChoiceClickListener()
                {
                    StringBuffer sb = new StringBuffer(100);
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        if(isChecked)
                        {
                            if(hobbies[which].equals("DouBan.com"))
                                douban=true;
                            if(hobbies[which].equals("OpenLibrary.org"))
                                open=true;
                        }
                        if(!isChecked)
                        {
                            if(hobbies[which].equals("DouBan.com"))
                                douban=false;
                            if(hobbies[which].equals("OpenLibrary.org"))
                                open=false;
                            if(douban==false&&open==false){
                                Toast.makeText(Setting_combine.this, "BookShelf:请至少选择一个网络服务", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                builder.show();
            }
        });

        back_log=(TextView)findViewById(R.id.backup_place);
        File file_path=this.getBaseContext().getFilesDir();
        String path=file_path.getPath();
        file_path2=path;
        back_log.setText(path);
        /*back_log.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    mIntent.setData(Uri.fromParts("package", getBaseContext().getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    mIntent.setAction(Intent.ACTION_VIEW);
                    mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                    mIntent.putExtra("com.android.settings.ApplicationPkgName", getBaseContext().getPackageName());
                }
                getBaseContext().startActivity(mIntent);
            }
        });*/
        backup_appl=(TextView)findViewById(R.id.backup_applicant);
        backup_appl.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                final ProgressDialog dialog = new ProgressDialog(v.getContext());

                dialog.setTitle("正在加载中......");
                //设置一下进度条的样式
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                //创建一个子线程
                new Thread(){
                    @Override
                    public void run() {
                        //设置进度条的最大值
                        dialog.setMax(100);

                        //设置当前进度
                        for (int i = 0; i <=30 ; i++) {
                            dialog.setProgress(i);
                            //睡眠一会儿
                            SystemClock.sleep(15);
                        }
                        //关闭对话框
                        dialog.dismiss();
                    }
                }.start();

                //一样要show
                dialog.show();
                SystemClock.sleep(205);
                cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

                year = String.valueOf(cal.get(Calendar.YEAR));
                month = String.valueOf(cal.get(Calendar.MONTH))+1;
                day = String.valueOf(cal.get(Calendar.DATE));
                if (cal.get(Calendar.AM_PM) == 0)
                    hour = String.valueOf(cal.get(Calendar.HOUR));
                else
                    hour = String.valueOf(cal.get(Calendar.HOUR)+12);
                minute = String.valueOf(cal.get(Calendar.MINUTE));
                second = String.valueOf(cal.get(Calendar.SECOND));

                my_time_1 = year + "-" + month + "-" + day;
                my_time_2 = hour + "-" + minute + "-" + second;
                Toast.makeText(Setting_combine.this, "BookShelf:备份成功!备份文件："+file_path2+my_time_1+"/"+my_time_2, Toast.LENGTH_SHORT).show();
            }
        });


        back_recover=(TextView)findViewById(R.id.backup_recover);
        back_recover.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                myDialog1 = new MyDialog1(v.getContext(),R.layout.backup_page,new int[]{R.id.backup_applicant});
                myDialog1.show();
            }
        });
    }



}
