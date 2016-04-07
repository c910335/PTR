package asia.tatsujin.ptr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asia.tatsujin.ptr.models.Board;
import asia.tatsujin.ptr.models.Post;

/**
 * Created by YanBo on 2016/4/7.
 */


public class MyAdapter extends ArrayAdapter<Object> {

    public MyAdapter(Context context, List items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}