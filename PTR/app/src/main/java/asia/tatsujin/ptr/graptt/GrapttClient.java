package asia.tatsujin.ptr.graptt;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.tatsujin.ptr.graptt.data.Response;
import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;
import asia.tatsujin.ptr.models.Push;

/**
 * Created by tatsujin on 2016/3/28.
 */
public class GrapttClient {

    public interface OnConnectListener {
        void onConnect(String status);
        void onError(String message);
    }

    public interface OnDisconnectListener {
        void onDisconnect();
    }

    public interface OnLoginListener {
        void onLogin(String status);
        void onError(String message);
    }

    public interface OnDeleteOtherListener {
        void onDelete(String status);
        void onError(String message);
    }

    public interface OnGetFavoriteListener {
        void onGet(List<Board> boards);
        void onError(String message);
    }

    public interface OnEnterBoardListener {
        void onEnter(String status, String name);
        void onError(String message);
    }

    public interface OnGetPostsListener {
        void onGet(List<Post> posts);
        void onError(String message);
    }

    public interface OnGetPostListener {
        void onGet(Post post);
        void onError(String message);
    }

    public interface OnCreatePostListener {
        void onPost(String status);
        void onError(String message);
    }

    public interface OnPushListener {
        void onPush(String status);
        void onError(String message);
    }

    private interface OnResponseListener {
        void onResponse(Response response);
        void onError(String message);
    }

    public static class PushTag {
        public static final int PUSH = 1;
        public static final int BOO = 2;
        public static final int ARROW = 3;
    }

    private static final int POST_MAX_CACHE_NUM = 20;
    private String baseURL;
    private RequestQueue requestQueue;
    private String token;
    private Gson gson;
    private Post post;

    public GrapttClient(Context context, String baseURL, OnConnectListener onConnectListener) {
        this.baseURL = baseURL;
        requestQueue = Volley.newRequestQueue(context);
        postConnection(onConnectListener);
        gson = new Gson();
    }

    public void reconnect(final OnConnectListener onConnectListener) {
        close(null);
        postConnection(onConnectListener);
    }

    public void close(final OnDisconnectListener onDisconnectListener) {
        delete("/connection/" + token, null, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (onDisconnectListener != null)
                    onDisconnectListener.onDisconnect();
            }

            @Override
            public void onError(String message) {
                if (onDisconnectListener != null)
                    onDisconnectListener.onDisconnect();
            }
        });
    }

    public void login(String account, String password, final OnLoginListener onLoginListener) {
        Map<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        post("/connection/" + token + "/login", params, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onLoginListener.onLogin(response.status);
            }

            @Override
            public void onError(String message) {
                onLoginListener.onError(message);
            }
        });
    }

    public void deleteOther(boolean delete, final OnDeleteOtherListener onDeleteOtherListener) {
        Map<String, String> params = new HashMap<>();
        params.put("del", String.valueOf(delete));
        delete("/connection/" + token + "/other", params, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onDeleteOtherListener.onDelete(response.status);
            }

            @Override
            public void onError(String message) {
                onDeleteOtherListener.onError(message);
            }
        });
    }

    public void getFavorite(final OnGetFavoriteListener onGetFavoriteListener) {
        get("/connection/" + token + "/board", new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onGetFavoriteListener.onGet(response.boards);
            }

            @Override
            public void onError(String message) {
                onGetFavoriteListener.onError(message);
            }
        });
    }

    public void enterBoard(String name, final OnEnterBoardListener onEnterBoardListener) {
        put("/connection/" + token + "/board/" + name, null, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onEnterBoardListener.onEnter(response.status, response.name);
            }

            @Override
            public void onError(String message) {
                onEnterBoardListener.onError(message);
            }
        });
    }

    public void getPosts(final OnGetPostsListener onGetPostsListener) {
        get("/connection/" + token + "/post", new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onGetPostsListener.onGet(response.posts);
            }

            @Override
            public void onError(String message) {
                onGetPostsListener.onError(message);
            }
        });
    }

    public void getPost(String id, final OnGetPostListener onGetPostListener) {
        final boolean isNewPost = id != null;
        if (id == null) {
            id = "nil";
        }
        else if (id.isEmpty())
            onGetPostListener.onError("Not Found");
        else
            id = Uri.encode(id);
        get("/connection/" + token + "/post/" + id, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                cachePost(response.post, isNewPost);
                List<Object> content = new ArrayList<>();
                for (Object line : response.post.content) {
                    if (!line.getClass().equals(String.class))
                        line = gson.fromJson(gson.toJson(line), Push.class);
                    content.add(line);
                }
                response.post.content = content;
                onGetPostListener.onGet(response.post);
            }

            @Override
            public void onError(String message) {
                onGetPostListener.onError(message);
            }
        });
    }

    public List<Post> getCachedPosts() {
        List<Post> posts = new Select().from(Post.class).execute();
        for (Post post : posts) {
            post.content = new ArrayList<>();
            for (String line: post.text.split("\n")) {
                if (line.startsWith("{\"tag\":\""))
                    post.content.add(gson.fromJson(line, Push.class));
                else
                    post.content.add(line);
            }
        }
        return posts;
    }

    public void createPost(String title, String content, final OnCreatePostListener onCreatePostListener) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        post("/connection/" + token + "/post", params, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onCreatePostListener.onPost(response.status);
            }

            @Override
            public void onError(String message) {
                onCreatePostListener.onError(message);
            }
        });
    }

    public void push(String id, int tag, String content, final OnPushListener onPushListener) {
        if (id == null)
            id = "nil";
        else if (id.isEmpty())
            onPushListener.onError("Not Found");
        else
            id = Uri.encode(id);
        Map<String, String> params = new HashMap<>();
        params.put("tag", String.valueOf(tag));
        params.put("content", content);
        post("/connection/" + token + "/post/" + id + "/push", params, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                onPushListener.onPush(response.status);
            }

            @Override
            public void onError(String message) {
                onPushListener.onError(message);
            }
        });
    }

    private void cachePost(Post post, boolean isNewPost) {
        if (isNewPost) {
            this.post = post;
            Post oldPost = new Select().from(Post.class).where("id = ?", post.id).executeSingle();
            if (oldPost != null)
                oldPost.delete();
            else
                while (new Select().from(Post.class).count() >= POST_MAX_CACHE_NUM)
                    new Delete().from(Post.class).executeSingle();
            post.text = "";
        }
        for (Object line : post.content) {
            if (line.getClass().equals(String.class))
                this.post.text += line + "\n";
            else
                this.post.text += gson.toJson(line) + "\n";
        }
        this.post.save();
    }

    private void postConnection(final OnConnectListener onConnectListener) {
        post("/connection", null, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                token = response.token;
                if (onConnectListener != null)
                    onConnectListener.onConnect(response.status);
            }

            @Override
            public void onError(String message) {
                if (onConnectListener != null)
                    onConnectListener.onError(message);
            }
        });
    }

    private void get(String path, OnResponseListener onResponseListener) {
        request(Request.Method.GET, path, null, onResponseListener);
    }

    private void post(String path, Map<String, String> params, OnResponseListener onResponseListener) {
        request(Request.Method.POST, path, params, onResponseListener);
    }

    private void put(String path, Map<String, String> params, OnResponseListener onResponseListener) {
        request(Request.Method.PUT, path, params, onResponseListener);
    }

    private void delete(String path, Map<String, String> params, OnResponseListener onResponseListener) {
        request(Request.Method.DELETE, path, params, onResponseListener);
    }

    private void request(int method, String path, final Map<String, String> params, final OnResponseListener onResponseListener) {
        requestQueue.add(new StringRequest(method, baseURL + path,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        Response response = gson.fromJson(res, Response.class);
                        onResponseListener.onResponse(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String response = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                onResponseListener.onError(jsonResponse.getString("error"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        onResponseListener.onError(error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                return params;
            }
        }.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }
}
