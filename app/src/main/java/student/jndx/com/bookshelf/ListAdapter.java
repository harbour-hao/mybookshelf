package student.jndx.com.bookshelf;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private ArrayList<Book> books;
    private Context context;
    public ListAdapter(ArrayList<Book> books,Context context)
    {
        this.books=books;
        this.context=context;
    }
    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//组装数据
        if(convertView==null)
        convertView=View.inflate(context,R.layout.listcontent,null);//在list_item中有两个id,现在要把他们拿过来
        TextView title=convertView.findViewById(R.id.list_book_title);
        TextView detail=convertView.findViewById(R.id.list_book_detail);
        TextView date=convertView.findViewById(R.id.list_book_date);
        ImageView imageView=convertView.findViewById(R.id.list_book_image);
        //组件一拿到，开始组装
        title.setText(books.get(position).getTitle());
        String detailtxt=books.get(position).getWriter()+","+books.get(position).getPublisher();
        detail.setText(detailtxt);
        date.setText(books.get(position).getDate());
        imageView.setImageResource(books.get(position).getImageurl());
        //组装玩开始返回
        return convertView;
    }
}
