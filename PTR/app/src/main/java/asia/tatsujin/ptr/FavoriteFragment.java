package asia.tatsujin.ptr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.tatsujin.ptr.graptt.GrapttClient;
import asia.tatsujin.ptr.models.Board;

public class FavoriteFragment extends Fragment implements BoardRecyclerViewAdapter.OnListFragmentInteractionListener {

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MainActivity.grapttClient.getFavorite(new GrapttClient.OnGetFavoriteListener() {
                @Override
                public void onGet(List<Board> boards) {
                    recyclerView.setAdapter(new BoardRecyclerViewAdapter(boards, FavoriteFragment.this));
                }

                @Override
                public void onError(String message) {

                }
            });
        }
        return view;
    }

    @Override
    public void onListFragmentInteraction(Board board) {
        MainActivity.grapttClient.enterBoard(board.en_name, new GrapttClient.OnEnterBoardListener() {
            @Override
            public void onEnter(String status, String name) {
                showPosts();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void showPosts() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        PostsFragment postsFragment = new PostsFragment();
        fragmentTransaction.replace(R.id.fragment, postsFragment)
                .addToBackStack(null)
                .commit();
    }

}
