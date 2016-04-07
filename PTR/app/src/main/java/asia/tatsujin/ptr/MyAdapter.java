package asia.tatsujin.ptr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;

/**
 * Created by YanBo on 2016/4/7.
 */


public class MyAdapter extends ArrayAdapter<Object> {
    public MyAdapter(Context context, Post[] post) {
        super(context, 0, post);
    }
    public MyAdapter(Context context, Board[] fav) {
        super(context, 0, fav);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_favroite, parent, false);
        }

        if(getItem(position).getClass().equals(Post.class)){
            Post pp= (Post) getItem(position);
            TextView pp_name= (TextView) convertView.findViewById(R.id.tvName);
            pp_name.setText(pp.title + "\n" + "Author: "+pp.author+"\nStatus: "+pp.status+"\nlike: "+pp.like);
            return convertView;
        }
        else{
            Board fav = (Board) getItem(position);
            TextView fav_name= (TextView) convertView.findViewById(R.id.tvName);
            fav_name.setText(fav.en_name+"\n"+fav.zh_name);
        }



        return convertView;
    }
}