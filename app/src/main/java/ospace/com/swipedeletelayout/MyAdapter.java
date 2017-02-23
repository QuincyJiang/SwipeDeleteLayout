package ospace.com.swipedeletelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang on 2017/2/23.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> implements View.OnClickListener{
    private ArrayList<String> mDatas;
    private Context mContext;
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
holder.tv_cancel.setOnClickListener(this);
        holder.tv_delete.setOnClickListener(this);
        holder.tv_name.setText(mDatas.get(position));
        holder.tv_delete.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public MyAdapter(ArrayList<String> mData, Context context) {
        MyAdapter.this.mDatas = mData;
        MyAdapter.this.mContext = context;

    }

    public class MyHolder extends  RecyclerView.ViewHolder{
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_cancel)
        TextView tv_cancel;
        @Bind(R.id.tv_delete)
        TextView tv_delete;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.tv_cancel:
        break;
    case R.id.tv_delete:
        mDatas.remove( (int)v.getTag());
        notifyItemRemoved((int)v.getTag());
        break;
}
    }
}
