package asia.tatsujin.ptr;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import asia.tatsujin.ptr.models.Post;


public class PostFragment extends ListFragment {

    public  Post[] pp;
    public PostFragment(Post[] posts) {
        this.pp=posts;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        // do not call super.onSaveInstanceState()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_post, container, false);
        Log.d("G_G", pp[0].title);
        this.setListAdapter(new MyAdapter(getActivity(), pp));
        return view;
    }

    public void onListItemClick(ListView parent, View v,
                                int position, long id) {
        Log.d("Post_title_click", "onListItemClick");
        Toast.makeText(getActivity(),
                "You have selected " + pp[position].title,
                Toast.LENGTH_SHORT).show();

    }

}
