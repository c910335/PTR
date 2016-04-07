package asia.tatsujin.ptr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asia.tatsujin.ptr.models.Post;
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Post item);
    }

    private final List<Post> posts;
    private final OnListFragmentInteractionListener listener;

    public PostRecyclerViewAdapter(List<Post> posts, OnListFragmentInteractionListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = posts.get(position);
        holder.idView.setText(posts.get(position).author);
        holder.contentView.setText(posts.get(position).title);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView idView;
        public final TextView contentView;
        public Post item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            idView = (TextView) view.findViewById(R.id.id);
            contentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
