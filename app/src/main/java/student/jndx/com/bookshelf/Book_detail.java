package student.jndx.com.bookshelf;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Book_detail extends AppCompatActivity {
    private ImageView img;
    private TextView title;
    private TextView writer;
    private TextView partner;
    private TextView publisher;
    private TextView date;
    private TextView code;
    private TextView readstatus;
    private TextView bookshelf;
    private TextView url;
    private Bundle nowbunble;//实时的bunble
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);
        final Bundle bundle = this.getIntent().getExtras();
        //final修饰引用数据的时候，不能改变引用指向，但可以改变对象属性
        nowbunble=bundle;
        InitResource();//绑定控件
        if(bundle!=null)InitData(bundle);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Book_detail.this, EditBook.class);
                intent.putExtras(nowbunble);
                startActivityForResult(intent, 1);
                Log.d("Main","bunble:"+bundle.getString("title"));
            }
        });
    }
    private void InitResource(){
        img=(ImageView)findViewById(R.id.book_detail_img);
        title=(TextView)findViewById(R.id.book_detail_name);
        writer=(TextView)findViewById(R.id.book_detail_writer);
        partner=(TextView)findViewById(R.id.book_detail_partner);
        publisher=(TextView)findViewById(R.id.book_detail_publisher);
        date=(TextView)findViewById(R.id.book_detail_date);
        code=(TextView)findViewById(R.id.book_detail_code);
        readstatus=(TextView)findViewById(R.id.book_detail_readstatus);
        bookshelf=(TextView)findViewById(R.id.book_detail_bookshelf);
        url=(TextView)findViewById(R.id.book_detail_url);
    }
    private void InitData(Bundle bundle){
        img.setImageResource(bundle.getInt("img"));
        title.setText(bundle.getString("title"));
        writer.setText(bundle.getString("writer"));
        partner.setText(bundle.getString("partner"));
        publisher.setText(bundle.getString("publisher"));
        date.setText(bundle.getString("date"));
        code.setText(bundle.getString("code"));
        readstatus.setText(bundle.getString("readstatus"));
        bookshelf.setText(bundle.getString("bookshelf"));
        url.setText(bundle.getString("url"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle;
                    bundle = data.getExtras();
                    nowbunble=bundle;
                    title.setText(bundle.getString("title"));
                    writer.setText(bundle.getString("writer"));
                    partner.setText(bundle.getString("partner"));
                    publisher.setText(bundle.getString("publisher"));
                    date.setText(bundle.getString("date"));
                    code.setText(bundle.getString("code"));
                    readstatus.setText(bundle.getString("readstatus"));
                    bookshelf.setText(bundle.getString("bookshelf"));
                    url.setText(bundle.getString("url"));
                    break;
                }
            case 0:
                if (resultCode == RESULT_CANCELED) {
                   InitData(nowbunble);
                    break;
                }
        }
    }
}
