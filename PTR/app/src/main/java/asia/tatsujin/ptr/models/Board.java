package asia.tatsujin.ptr.models;

/**
 * Created by tatsujin on 2016/3/28.
 */
public class Board {
    public int num;
    public String en_name;
    public String zh_name;
    public String popularity;
    public String type;
    public boolean unread;

    public String toString() {
        return "{num: " + num + ",\n"
                + "en_name: " + en_name + ",\n"
                + "zh_name: " + zh_name + ",\n"
                + "popularity: " + popularity + ",\n"
                + "type: " + type + ",\n"
                + "unread: " + unread + ",\n"
                + "}";
    }
}