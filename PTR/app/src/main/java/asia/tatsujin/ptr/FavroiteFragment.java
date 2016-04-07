package asia.tatsujin.ptr;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import asia.tatsujin.ptr.graptt.GrapttClient;
import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;


public class FavroiteFragment extends ListFragment {
    private static final String TAG = "ListFragmentImpl";


    private ListView selfList;
    private Board[] boards;
    private View view;

    FavroiteFragment(Board[] bb){

        this.boards=bb;

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        // do not call super.onSaveInstanceState()
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_favroite, container, false);
        this.setListAdapter(new MyAdapter(getActivity(),boards));
        return view;
    }



    public void onListItemClick(ListView parent, View v,
                                int position, long id) {
        Log.d(TAG, "onListItemClick");
        Toast.makeText(getActivity(),
                "You have selected " + boards[position].en_name,
                Toast.LENGTH_SHORT).show();
        enterBoard(boards[position].en_name);

    }

    public void  enterBoard(String boardName){
        MainActivity.grapttClient.enterBoard(boardName, new GrapttClient.OnEnterBoardListener() {
            @Override
            public void onEnter(String status) {
                Log.d("F_F", status.toString());
                getPosts();
            }

            @Override
            public void onError(String message) {
                Log.d("F_F", message);
            }
        });
    }

    public  void  changeFragment(Post[] posts){
        android.support.v4.app.FragmentTransaction fragmentTransaction =MainActivity.fragmentManager.beginTransaction();
        PostFragment pp =new PostFragment(posts);
        fragmentTransaction.replace(R.id.favroiteFragment, pp);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void  getPosts(){
        MainActivity.grapttClient.getPosts(new GrapttClient.OnGetPostsListener() {
            @Override
            public void onGet(Post[] posts) {
                changeFragment(posts);
            }

            @Override
            public void onError(String message) {
                Log.d("F_F", message);
            }
        });

    }

}
