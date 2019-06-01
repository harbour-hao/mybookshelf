package student.jndx.com.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView mSearchView;
    private AutoCompleteTextView mAutoCompleteTextView;//搜索输入框
    private ListAdapter adapter;//存储Book数据的数组的适配器
    private ArrayList<Book> books;
    private ListView listView;
    private ArrayList<Book> allbooks;
    private ListOperator operator=new ListOperator();
    private int chooseitem=0;//选中第几个item
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //搜索框初始化
        initView();
        initData();
        setListener();
        //listview,只需更新ArrayList的数据即可

        this.allbooks=new ArrayList<>(this.books);//记得保存原数据
        Log.d("Main","activty"+String.valueOf(allbooks.size()));
        adapter = new ListAdapter( this.books,MainActivity.this);
        listView =(ListView)this.findViewById(R.id.MyListView);
        listView.setAdapter(adapter);
        //listview的点击事件
        listView.setOnItemClickListener(new mItemClick());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //菜单无反应，不显示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.menu_main_sort) {
            Log.d("Main","sort");
            final String []SortWay={"标题","作者","出版社","出版时间"};
            AlertDialog.Builder sortdialog=new AlertDialog.Builder(MainActivity.this).setTitle("选择排序方式");
            sortdialog.setSingleChoiceItems(SortWay, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Main",SortWay[which]);
                            dialog.dismiss();
                            switch (SortWay[which]){
                                case "标题":
                                    Collections.sort(books, new Comparator<Book>() {
                                        @Override
                                        public int compare(Book b1, Book b2) {
                                            return b1.getTitle().compareTo(b2.getTitle());
                                        }
                                    });
                                    break;
                                case "作者":
                                    Collections.sort(books, new Comparator<Book>() {
                                        @Override
                                        public int compare(Book b1, Book b2) {
                                            return b1.getWriter().compareTo(b2.getWriter());
                                        }
                                    });
                                    break;
                                case "出版社":
                                    Collections.sort(books, new Comparator<Book>() {
                                        @Override
                                        public int compare(Book b1, Book b2) {
                                            return b1.getPublisher().compareTo(b2.getPublisher());
                                        }
                                    });
                                    break;
                                case "出版时间":
                                    Collections.sort(books, new Comparator<Book>() {
                                        @Override
                                        public int compare(Book b1, Book b2) {
                                            return b1.getDate().compareTo(b2.getDate());
                                        }
                                    });
                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
            sortdialog.setCancelable(false);
            sortdialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_book) {
            // Handle the camera action
        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_tag) {

        } else if (id == R.id.nav_book) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView(){

        mSearchView=findViewById(R.id.searchView);
        mAutoCompleteTextView=mSearchView.findViewById(R.id.search_src_text);
    }

    private void initData(){
        this.books=operator.load(getBaseContext());
        if(books==null){//样例数据
            this.books=new ArrayList<Book>();
            Book book=new Book();
            book.setTitle("深入了解jvm");
            book.setDate("2011-01");
            book.setWriter("周志明");
            book.setPublisher("中华出版社");
            this.books.add(book);
            book=new Book();
            book.setTitle("Effective Java");
            book.setDate("2010-03");
            book.setPublisher("美国出版社");
            book.setWriter("joshua bloch");
            this.books.add(book);
            book=new Book();
            book.setTitle("this world");
            book.setDate("2015-09");
            book.setWriter("小明");
            book.setPublisher("广东出版社");
            this.books.add(book);
        }
        mSearchView.setIconifiedByDefault(false);//设置搜索图标是否显示在搜索框内
        //1:回车
        //2:前往
        //3:搜索
        //4:发送
        //5:下一項
        //6:完成
        mSearchView.setImeOptions(2);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
//        mSearchView.setInputType(1);//设置输入类型
//        mSearchView.setMaxWidth(200);//设置最大宽度
        mSearchView.setQueryHint("搜索书本");//设置查询提示字符串
//        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        setUnderLinetransparent(mSearchView);
    }

    private void setListener(){

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Main","=====query="+query);
                books.clear();
                for(Book tmp:allbooks){
                    if(tmp.getTitle().contains(query)){
                        books.add(tmp);
                    }
                }
                Log.d("Main",String.valueOf(allbooks.size()));
                adapter.notifyDataSetChanged();
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Main","=====newText="+newText);
                if(newText.isEmpty()){
                    books.clear();
                    books.addAll(allbooks);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    /**设置SearchView下划线透明**/
    private void setUnderLinetransparent(SearchView searchView){
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    class mItemClick implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            chooseitem=i;
            Book selectcontent = books.get(i);//获取选中的数据
            Intent intent = new Intent(MainActivity.this, Book_detail.class);
            Bundle bundle = new Bundle();
            bundle.putInt("img", selectcontent.getImageurl());
            bundle.putString("title", selectcontent.getTitle());
            bundle.putString("writer", selectcontent.getWriter());
            bundle.putString("partner", selectcontent.getParter());
            bundle.putString("publisher",selectcontent.getPublisher());
            bundle.putString("bookshelf",selectcontent.getBookshelf());
            bundle.putString("date",selectcontent.getDate());
            bundle.putString("code",selectcontent.getCode());
            bundle.putString("readstatus",selectcontent.getReadstatus());
            bundle.putString("note",selectcontent.getNote());
            bundle.putString("url",selectcontent.getUrl());
            bundle.putString("tag",selectcontent.getTag());
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            Log.d("Main","click");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle;
                    bundle = data.getExtras();
                    Book changeitem=books.get(chooseitem);
                    changeitem.setTitle(bundle.getString("title"));
                    changeitem.setWriter(bundle.getString("writer"));
                    changeitem.setParter(bundle.getString("partner"));
                    changeitem.setPublisher(bundle.getString("publisher"));
                    changeitem.setDate(bundle.getString("date"));
                    changeitem.setCode(bundle.getString("code"));
                    changeitem.setReadstatus(bundle.getString("readstatus"));
                    changeitem.setBookshelf(bundle.getString("bookshelf"));
                    changeitem.setTag(bundle.getString("tag"));
                    changeitem.setNote(bundle.getString("note"));
                    changeitem.setUrl(bundle.getString("url"));
                    books.set(chooseitem,changeitem);
                    operator.save(getBaseContext(),books);
                    break;
                }
        }
    }

}
