package edu.upc.dsa.dsa_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ViewHolder> {

    private final List<ForumMessage> messages;

    public MensajesAdapter(List<ForumMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumMessage message = messages.get(position);
        holder.tvAuthor.setText(message.getAuthor());
        holder.tvContent.setText(message.getContent());

        String time = "";
        if (message.getCreatedAt() != null) {
            if (message.getCreatedAt().length() >= 19) {
                time = message.getCreatedAt().substring(0, 10) + " " + message.getCreatedAt().substring(11, 16);
            } else {
                time = message.getCreatedAt();
            }
        }
        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvContent, tvTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor  = itemView.findViewById(R.id.tvMessageAuthor);
            tvContent = itemView.findViewById(R.id.tvMessageContent);
            tvTime    = itemView.findViewById(R.id.tvMessageTime);
        }
    }
}
