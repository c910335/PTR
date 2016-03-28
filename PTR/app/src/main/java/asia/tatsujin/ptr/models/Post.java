package asia.tatsujin.ptr.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tatsujin on 2016/3/28.
 */
@Table(name = "posts", id = "_id")
public class Post extends Model {
    @Column(name = "num")
    public int num;
    @Column(name = "author")
    public String author;
    @Column(name = "title")
    public String title;
    @Column(name = "date")
    public String date;
    @Column(name = "status")
    public String status;
    @Column(name = "like")
    public String like;
    @Column(name = "source")
    public String source;
    @Column(name = "id")
    public String id;
    @Column(name = "url")
    public String url;
    @Column(name = "pttcoin")
    public int pttcoin;

    public String toString() {
        return "{num: " + num + ",\n"
                + "author: " + author + ",\n"
                + "title: " + title + ",\n"
                + "date: " + date + ",\n"
                + "status: " + status + ",\n"
                + "like: " + like + ",\n"
                + "source: " + source + ",\n"
                + "id: " + id + ",\n"
                + "url: " + url + ",\n"
                + "pttcoin: " + pttcoin + ",\n"
                + "}";
    }
}