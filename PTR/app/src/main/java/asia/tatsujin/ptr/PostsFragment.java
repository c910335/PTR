package asia.tatsujin.ptr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.tatsujin.ptr.graptt.GrapttClient;
import asia.tatsujin.ptr.models.Post;

public class PostsFragment extends Fragment implements PostRecyclerViewAdapter.OnListFragmentInteractionListener{

    private static final String ARG_BOARD_NAME = "board.name";
    private OnTransactListener onTransactListener;

    public static PostsFragment newInstance(String boardName) {
        PostsFragment postsFragment = new PostsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_BOARD_NAME, boardName);
        postsFragment.setArguments(bundle);
        return postsFragment;
    }

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
                    onTransactListener.onFinishTransaction(getArguments().getString(ARG_BOARD_NAME));
                }

                @Override
                public void onError(String message) {

                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onTransactListener = (OnTransactListener) getActivity();
    }

    @Override
    public void onDetach() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Post post) {
    }
}
