package student.jndx.com.bookshelf;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Guanyu_activity extends AppCompatActivity {
    TextView feedback;
    TextView Source_page;
    TextView donate;
    TextView app_search;
    TextView promission;
    TextView close;
    TextView return_button;
    String packageName="BookShelf";
    private MyDialog1 myDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_guanyu_activity);

        feedback=(TextView)findViewById(R.id.feedback);
        feedback.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                Uri uri = Uri.parse("mailto:"+"PayPal Alipay:smartjinyu@gmail.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                v.getContext().startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
            }
        });
        Source_page=(TextView)findViewById(R.id.Source_path);
        Source_page.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                Uri uri = Uri.parse("https://github.com/smartjinyu/MyBookShelf");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        donate=(TextView)findViewById(R.id.denote);
        donate.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                ClipboardManager cm = (ClipboardManager) getSystemService(v.getContext().CLIPBOARD_SERVICE);
               ClipData mClipData = ClipData.newPlainText("Label", "PayPal Alipay:smartjinyu@gmail.com");
               cm.setPrimaryClip(mClipData);
                Toast.makeText(Guanyu_activity.this, "BookShelf:我的账号已复制到剪贴板，感谢您的捐赠！", Toast.LENGTH_SHORT).show();
            }
        });
        app_search=(TextView)findViewById(R.id.app_search);
        app_search.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
             try {
                String str = "market://details?id=" + packageName;
                Intent localIntent = new Intent(Intent.ACTION_VIEW);
                localIntent.setData(Uri.parse(str));
                startActivity(localIntent);
            } catch (Exception e) {
                // 打开应用商店失败 可能是没有手机没有安装应用市场
                e.printStackTrace();
                Toast.makeText(v.getContext().getApplicationContext(), "打开应用商店失败", Toast.LENGTH_SHORT).show();
                // 调用系统浏览器进入（小米）商城
                String url = "http://app.mi.com/details?id="+packageName+"&ref=search";
                openLinkBySystem(url);
            }
            }

        });
        promission=(TextView)findViewById(R.id.promission);
        promission.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                myDialog1 = new MyDialog1(v.getContext(),R.layout.promission_page_activity,new int[]{R.id.title});
                myDialog1.show();
            }
        });
        /*close=(TextView)findViewById(R.id.close);
        close.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                //myDialog1.cancel();
            }
        });*/
        return_button=(TextView)findViewById(R.id.return_jiantou2);
        return_button.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(Guanyu_activity.this,MainActivity.class);
                startActivity(intent);
            }

        });
    }
    private void openLinkBySystem(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
