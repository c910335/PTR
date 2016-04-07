package asia.tatsujin.ptr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.tatsujin.ptr.graptt.GrapttClient;
import asia.tatsujin.ptr.models.Post;

public class PostsFragment extends Fragment implements PostRecyclerViewAdapter.OnListFragmentInteractionListener{

    public PostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MainActivity.grapttClient.getPosts(new GrapttClient.OnGetPostsListener() {
                @Override
                public void onGet(List<Post> posts) {
                    recyclerView.setAdapter(new PostRecyclerViewAdapter(posts, PostsFragment.this));
                }

                @Override
                public void onError(String message) {

                }
            });
        }
        return view;
    }

    @Override
    public void onListFragmentInteraction(Post post) {
    }
}
