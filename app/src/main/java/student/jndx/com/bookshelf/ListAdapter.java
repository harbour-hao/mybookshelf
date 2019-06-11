package student.jndx.com.bookshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private ArrayList<Book> books;
    private MainActivity context;
    private OnShowItemClickListener onShowItemClickListener;
    ViewHolder viewHolder;
    public ListAdapter(ArrayList<Book> books,MainActivity context)
    {
        this.books=books;
        this.context=context;
    }
    @Override
    public int getCount() {
        return books.size();
    }
    public void delAll(){
        books.clear();
    }
    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder{
        View bookview;
        ImageView bookImage;
        TextView title;
        TextView detail;
        TextView date;
        CheckBox checkBox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//组装数据
        final Book book=books.get(position);
        ViewHolder viewHolder=null;
        if(convertView==null) {
            convertView = View.inflate(context, R.layout.listcontent, null);//在list_item中有两个id,现在要把他们拿过来
            viewHolder = new ViewHolder();
            viewHolder.bookImage=convertView.findViewById(R.id.list_book_image);
            viewHolder.title = convertView.findViewById(R.id.list_book_title);
            viewHolder.detail = convertView.findViewById(R.id.list_book_detail);
            viewHolder.date = convertView.findViewById(R.id.list_book_date);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.listview_select_cb);
            convertView.setTag(viewHolder);

        }else {
                viewHolder = (ViewHolder) convertView.getTag();

        }
        if (book.isShow()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        viewHolder.title.setText(books.get(position).getTitle());
        String detailtxt = books.get(position).getWriter() + "," + books.get(position).getPublisher();
        viewHolder.detail.setText(detailtxt);
        viewHolder.date.setText(books.get(position).getDate());
        Bitmap bitmap= Parsebook.ImageManager.GetLocalBitmap(context,book.getUuid());
       // viewHolder.bookImage.setImageResource(R.mipmap.book_img_default);
        if(bitmap!=null)
        viewHolder.bookImage.setImageBitmap(bitmap);
        else
            viewHolder.bookImage.setImageResource(R.mipmap.book_img_default);


        //组装玩开始返回

     viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                book.setChecked(true);
            } else {
                book.setChecked(false);
            }

            onShowItemClickListener.onShowItemClick(book);
        }
    });

        viewHolder.checkBox.setChecked(book.isChecked());

            return convertView;
        }

public interface OnShowItemClickListener {
    void onShowItemClick(Book book);
}

    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener) {
        this.onShowItemClickListener = onShowItemClickListener;
    }
}
