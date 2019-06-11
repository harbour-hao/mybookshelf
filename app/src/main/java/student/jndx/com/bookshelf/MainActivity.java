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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView mSearchView;
    private AutoCompleteTextView mAutoCompleteTextView;//搜索输入框
    private ListAdapter adapter;//存储Book数据的数组的适配器
    private ArrayList<Book> books;
    private ListView listView;
    private ArrayList<Book> allbooks;
    private ListOperator operator = new ListOperator();
    private int chooseitem = 0;//选中第几个item
    private List<String> tags = null;//记录标签
    private NavigationView navigationView = null;
    private Menu menu, submenu = null;
    private MenuItem sort_itme, rename_item, delete_item;
    private FloatingActionButton fab;
    private String current_tag = null;
    private int current_tag_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //搜索框初始化
        initView();
        initData();
        setListener();
        //listview,只需更新ArrayList的数据即可

        this.allbooks = new ArrayList<>(this.books);//记得保存原数据
        Log.d("Main", "activty" + String.valueOf(allbooks.size()));
        adapter = new ListAdapter(this.books, MainActivity.this);
        listView = (ListView) this.findViewById(R.id.MyListView);
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
        sort_itme = menu.getItem(0);
        rename_item = menu.getItem(1);
        delete_item = menu.getItem(2);
        sort_itme.setVisible(true);
        rename_item.setVisible(false);
        delete_item.setVisible(false);
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
            Log.d("Main", "sort");
            final String[] SortWay = {"标题", "作者", "出版社", "出版时间"};
            AlertDialog.Builder sortdialog = new AlertDialog.Builder(MainActivity.this).setTitle("选择排序方式");
            sortdialog.setSingleChoiceItems(SortWay, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Main", SortWay[which]);
                    dialog.dismiss();
                    switch (SortWay[which]) {
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
        } else if (id == R.id.menu_delete) {
            LocalDataUtils.delete_tag(current_tag);
            submenu.clear();
            submenu = menu.addSubMenu("我的标签");
            tags = LocalDataUtils.read_all_tag();
            if (tags != null && tags.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    submenu.add(tags.get(i));
                    submenu.getItem(i).setIcon(R.mipmap.book_tag);
                }

                navigationView.invalidate();
            }

            fab.setVisibility(View.VISIBLE);
            mSearchView.setVisibility(View.VISIBLE);
            sort_itme.setVisible(true);
            delete_item.setVisible(false);
            rename_item.setVisible(false);
            adapter = new ListAdapter(books, MainActivity.this);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (id == R.id.menu_rename) {
            final EditText editText = new EditText(MainActivity.this);
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("请输入标签的新名称").setView(editText);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //建立标签
                            int index = tags.indexOf(current_tag);
                            String tag = editText.getText().toString().trim();
                            if("".equals(tag))
                            {
                                Toast.makeText(MainActivity.this, "标签名称不能为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (current_tag.equals(tag)) {
                                Toast.makeText(MainActivity.this, "请取个不一样的名字！", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (LocalDataUtils.rename_tag(current_tag, tag)) {
                                Toast.makeText(MainActivity.this, "修改标签成功", Toast.LENGTH_LONG).show();
                                tags = LocalDataUtils.read_all_tag();//读取所有标签
                                if (menu == null) {
                                    menu = navigationView.getMenu();
                                    submenu = menu.addSubMenu("我的标签");
                                }
                                submenu.getItem(index).setTitle(tag);
                                ArrayList<Book> new_books = new ArrayList<>();
                                for (Book book : books) {
                                    String book_tag = book.getTag();
                                    if (book_tag != null && book.getTag().equals(tag)) {
                                        new_books.add(book);
                                    }

                                }
                                adapter = new ListAdapter(new_books, MainActivity.this);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else
                                Toast.makeText(MainActivity.this, "添加标签失败，标签已存在或者含有非法字符", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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
            final EditText editText = new EditText(MainActivity.this);
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("请输入你要新建的标签名称").setView(editText);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //建立标签
                            String tag = editText.getText().toString().trim();
                            if("".equals(tag))
                            {
                                Toast.makeText(MainActivity.this, "标签名称不能为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (LocalDataUtils.insert_tag(tag)) {
                                Toast.makeText(MainActivity.this, "添加标签成功", Toast.LENGTH_LONG).show();
                                tags = LocalDataUtils.read_all_tag();//读取所有标签
                                if (menu == null) {
                                    menu = navigationView.getMenu();
                                    submenu = menu.addSubMenu("我的标签");
                                }
                                submenu.add(tags.get(tags.size() - 1));
                                submenu.getItem(tags.size() - 1).setIcon(R.mipmap.book_tag);

                            } else
                                Toast.makeText(MainActivity.this, "添加标签失败，标签已存在或者含有非法字符", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.nav_book) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        } else {
            //执行标签动作*******************


            String tag = (String) item.getTitle();
            current_tag = tag;
            current_tag_id = item.getItemId();
            fab.setVisibility(View.GONE);
            mSearchView.setVisibility(View.GONE);
            sort_itme.setVisible(false);
            rename_item.setVisible(true);
            delete_item.setVisible(true);
            ArrayList<Book> new_books = new ArrayList<>();
            for (Book book : books) {
                String book_tag = book.getTag();
                if (book_tag != null && book.getTag().equals(tag)) {
                    new_books.add(book);
                }

            }
            adapter = new ListAdapter(new_books, MainActivity.this);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {

        mSearchView = findViewById(R.id.searchView);
        mAutoCompleteTextView = mSearchView.findViewById(R.id.search_src_text);
    }

    private void initData() {
        this.books = operator.load(getBaseContext());
        if (books == null) {//样例数据
            this.books = new ArrayList<Book>();
            Book book = new Book();
            book.setTitle("深入了解jvm");
            book.setDate("2011-01");
            book.setWriter("周志明");
            book.setPublisher("中华出版社");
            book.setTag("想看");
            this.books.add(book);
            book = new Book();
            book.setTitle("Effective Java");
            book.setDate("2010-03");
            book.setPublisher("美国出版社");
            book.setWriter("joshua bloch");
            book.setTag("很想看");
            this.books.add(book);
            book = new Book();
            book.setTitle("this world");
            book.setDate("2015-09");
            book.setWriter("小明");
            book.setPublisher("广东出版社");
            book.setTag("不想看");
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
        LocalDataUtils.init_local_storage(MainActivity.this);
        init_menu();
    }

    /**
     * 初始化标签
     */
    private void init_menu() {
        tags = LocalDataUtils.read_all_tag();//读取所有标签
        if (tags != null) {
            if (menu == null && tags.size() > 0) {
                menu = navigationView.getMenu();
                submenu = menu.addSubMenu("我的标签");
            }
            for (int i = 0; i < tags.size(); i++) {
                submenu.add(tags.get(i));
                submenu.getItem(i).setIcon(R.mipmap.book_tag);

            }

            navigationView.invalidate();

        }
    }


    private void setListener() {

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Main", "=====query=" + query);
                books.clear();
                for (Book tmp : allbooks) {
                    if (tmp.getTitle().contains(query)) {
                        books.add(tmp);
                    }
                }
                Log.d("Main", String.valueOf(allbooks.size()));
                adapter.notifyDataSetChanged();
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Main", "=====newText=" + newText);
                if (newText.isEmpty()) {
                    books.clear();
                    books.addAll(allbooks);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
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

    class mItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            chooseitem = i;
            Book selectcontent = books.get(i);//获取选中的数据
            Intent intent = new Intent(MainActivity.this, Book_detail.class);
            Bundle bundle = new Bundle();
            bundle.putInt("img", selectcontent.getImageurl());
            bundle.putString("title", selectcontent.getTitle());
            bundle.putString("writer", selectcontent.getWriter());
            bundle.putString("partner", selectcontent.getParter());
            bundle.putString("publisher", selectcontent.getPublisher());
            bundle.putString("bookshelf", selectcontent.getBookshelf());
            bundle.putString("date", selectcontent.getDate());
            bundle.putString("code", selectcontent.getCode());
            bundle.putString("readstatus", selectcontent.getReadstatus());
            bundle.putString("note", selectcontent.getNote());
            bundle.putString("url", selectcontent.getUrl());
            bundle.putString("tag", selectcontent.getTag());
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            Log.d("Main", "click");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle;
                    bundle = data.getExtras();
                    Book changeitem = books.get(chooseitem);
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
                    books.set(chooseitem, changeitem);
                    adapter.notifyDataSetChanged();
                    operator.save(getBaseContext(), books);
                    break;
                }
        }
    }

}
