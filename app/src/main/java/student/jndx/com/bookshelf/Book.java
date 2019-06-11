package student.jndx.com.bookshelf;

import java.io.Serializable;

public class Book implements Serializable {
    private int imageurl=R.mipmap.book_img_default;
    private String title="标题";
    private String writer="writer";
    private String partner="parter";
    private String publisher="publisher";
    private String date="2019-4";
    private String code="611626";
    private String readstatus="未读";
    private String bookshelf="bookshelf";
    private String tag="标签";
    private String note="书籍笔记";
    private String url="www.baidu.com";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public int getImageurl() {
        return imageurl;
    }

    public void setImageurl(int imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getParter() {
        return partner;
    }

    public void setParter(String parter) {
        this.partner = parter;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReadstatus() {
        return readstatus;
    }

    public void setReadstatus(String readstatus) {
        this.readstatus = readstatus;
    }

    public String getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(String bookshelf) {
        this.bookshelf = bookshelf;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
