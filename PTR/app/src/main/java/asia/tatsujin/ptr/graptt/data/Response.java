package asia.tatsujin.ptr.graptt.data;

import java.util.List;

import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;

/**
 * Created by tatsujin on 2016/3/28.
 */
public class Response {
    public String status;
    public String token;
    public String name;
    public List<Board> boards;
    public List<Post> posts;
    public Post post;
}
