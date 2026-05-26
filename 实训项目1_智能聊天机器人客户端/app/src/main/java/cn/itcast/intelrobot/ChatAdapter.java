package cn.itcast.intelrobot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatBean> mList;

    public ChatAdapter(List<ChatBean> list){
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == ChatBean.TYPE_ROBOT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatting_left_item, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatting_right_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatBean bean = mList.get(position);
        if(bean.getType() == ChatBean.TYPE_ROBOT){
            holder.tvLeft.setText(bean.getContent());
        }else{
            holder.tvRight.setText(bean.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLeft, tvRight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.tv_content_left);
            tvRight = itemView.findViewById(R.id.tv_content_right);
        }
    }
}
