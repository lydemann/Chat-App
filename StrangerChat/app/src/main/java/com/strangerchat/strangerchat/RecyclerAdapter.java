package com.strangerchat.strangerchat;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.Collections;
        import java.util.List;

        import Models.ChatRoom;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ChatRoom> mData = Collections.emptyList();

    OnItemRecycleViewClickListener mOnItemRecycleViewClickListener;


    public RecyclerAdapter(List<ChatRoom> myData, OnItemRecycleViewClickListener mOnItemRecycleViewClickListener) {
        mData = myData;
        this.mOnItemRecycleViewClickListener = mOnItemRecycleViewClickListener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public TextView message;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.txt1);
            mImageView = (ImageView) v.findViewById(R.id.pic);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public void onBindViewHolder(ViewHolder view, int position) {
        view.mTextView.setText( mData.get(position).name);
        view.message.setText( mData.get(position).name);
        view.itemView.setTag(position);
        view.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemRecycleViewClickListener.onItemClicked(Integer.parseInt(v.getTag().toString()), RecyclerAdapter.this);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

}