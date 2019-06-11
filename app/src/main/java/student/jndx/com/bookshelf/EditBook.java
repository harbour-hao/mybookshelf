package student.jndx.com.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

public class EditBook extends AppCompatActivity {
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText translatorEditText;
    private EditText publisherEditText;
    private EditText DateEditText;
    private EditText isbnEditText;
    private ImageView coverImageView;
    private EditText readingStatusSpinner;
    private EditText bookshelfSpinner;
    private EditText labelsEditText;
    private EditText notesEditText;
    private EditText websiteEditText;
    private TextView detailBarTextView;
    private TextView booksave;
    private TextView cancel;
    private Bundle nowbunble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);
        Bundle bundle = this.getIntent().getExtras();
        //final修饰引用数据的时候，不能改变引用指向，但可以改变对象属性
        if(bundle!=null)InitData(bundle);
        booksave=(TextView)findViewById(R.id.edit_book_save_change);
        booksave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookSave();
            }
        });
        cancel=(TextView)findViewById(R.id.edit_book_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(EditBook.this);
                builder.setTitle("是否保存书籍");
                builder.setMessage("请选择你的操作");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNeutralButton("舍弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                });
                builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookSave();
                    }
                });
                builder.show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_book_menu, menu);
        return true;
    }
    //设置菜单，选择排序的位置
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save) {
            Log.d("Main","保存");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void BookSave(){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("img",nowbunble.getInt("img"));
                bundle.putString("title", titleEditText.getText().toString());
                bundle.putString("writer", authorEditText.getText().toString());
                bundle.putString("partner", translatorEditText.getText().toString());
                bundle.putString("publisher",publisherEditText.getText().toString());
                bundle.putString("bookshelf",bookshelfSpinner.getText().toString());
                bundle.putString("date",DateEditText.getText().toString());
                bundle.putString("code",isbnEditText.getText().toString());
                bundle.putString("readstatus",readingStatusSpinner.getText().toString());
                bundle.putString("note",notesEditText.getText().toString());
                bundle.putString("url",websiteEditText.getText().toString());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
    }
    //数据传进来放到控件中
    private void InitData(Bundle bundle){
        nowbunble=bundle;
        titleEditText = (EditText) findViewById(R.id.book_title_edit_text);
        titleEditText.setText(bundle.getString("title"));
        authorEditText = (EditText) findViewById(R.id.book_author_edit_text);
        authorEditText.setText(bundle.getString("writer"));
        translatorEditText = (EditText) findViewById(R.id.book_translator_edit_text);
        translatorEditText.setText(bundle.getString("partner"));
        publisherEditText = (EditText) findViewById(R.id.book_publisher_edit_text);
        publisherEditText.setText(bundle.getString("publisher"));
        DateEditText = (EditText) findViewById(R.id.book_pubyear_edit_text);
        DateEditText.setText(bundle.getString("date"));
        isbnEditText = (EditText) findViewById(R.id.book_isbn_edit_text);
        isbnEditText.setText(bundle.getString("code"));
        detailBarTextView = (TextView) findViewById(R.id.book_edit_detail_bar_text_view);
        detailBarTextView.setText("详情");
        coverImageView = (ImageView) findViewById(R.id.book_cover_image_view);
        coverImageView.setImageResource(bundle.getInt("img"));
        readingStatusSpinner = (EditText) findViewById(R.id.reading_status_spinner);
        readingStatusSpinner.setText(bundle.getString("readstatus"));
        bookshelfSpinner = (EditText) findViewById(R.id.book_shelf_spinner);
        bookshelfSpinner.setText(bundle.getString("bookshelf"));
        labelsEditText = (EditText) findViewById(R.id.book_labels_edit_text);
        labelsEditText.setText(bundle.getString("tag"));
        notesEditText = (EditText) findViewById(R.id.book_notes_edit_text);
        notesEditText.setText(bundle.getString("note"));
        websiteEditText = (EditText) findViewById(R.id.book_website_edit_text);
        websiteEditText.setText(bundle.getString("url"));

    }
}
