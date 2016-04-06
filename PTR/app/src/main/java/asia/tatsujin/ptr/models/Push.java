package asia.tatsujin.ptr.models;

/**
 * Created by tatsujin on 2016/4/6.
 */
public class Push {
    public String tag;
    public String author;
    public String content;
    public String time;

    public String toString() {
        return "{tag: " + tag + ","
                + "author: " + author + ","
                + "content: " + content + ","
                + "time: " + time + ","
                + "}";
    }
}
