package asia.tatsujin.ptr.graptt;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import asia.tatsujin.ptr.graptt.data.Response;
import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;

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
        void onGet(Board[] boards);
        void onError(String message);
    }

    public interface OnEnterBoardListener {
        void onEnter(String status);
        void onError(String message);
    }

    public interface OnGetPostsListener {
        void onGet(Post[] posts);
        void onError(String message);
    }

    public interface OnResponseListener {
        void onResponse(Response response);
        void onError(String message);
    }

    private String baseURL;
    private RequestQueue requestQueue;
    private String token;

    public GrapttClient(Context context, String baseURL, OnConnectListener onConnectListener) {
        this.baseURL = baseURL;
        requestQueue = Volley.newRequestQueue(context);
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
                onEnterBoardListener.onEnter(response.status);
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
                        Response response = new Gson().fromJson(res, Response.class);
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
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                return params;
            }
        }.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }
}
