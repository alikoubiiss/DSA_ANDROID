package edu.upc.dsa.dsa_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {

    private final List<ForumTopic> topics;

    public ForumAdapter(List<ForumTopic> topics) {
        this.topics = topics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forum_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumTopic topic = topics.get(position);
        holder.tvTitle.setText(topic.getTitle());
        holder.tvDesc.setText(topic.getDescription() != null ? topic.getDescription() : "");
        holder.tvAuthor.setText("Por: " + topic.getAuthor());
        // Format date: take only the first 10 chars of ISO timestamp
        String date = topic.getCreatedAt() != null && topic.getCreatedAt().length() >= 10
                ? topic.getCreatedAt().substring(0, 10) : "";
        holder.tvDate.setText(date);
        holder.tvMsgCount.setText(topic.getMessageCount() + " mensajes");

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), MensajesActivity.class);
            intent.putExtra("topicId", topic.getId());
            intent.putExtra("topicTitle", topic.getTitle());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvAuthor, tvDate, tvMsgCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tvTopicTitle);
            tvDesc     = itemView.findViewById(R.id.tvTopicDesc);
            tvAuthor   = itemView.findViewById(R.id.tvTopicAuthor);
            tvDate     = itemView.findViewById(R.id.tvTopicDate);
            tvMsgCount = itemView.findViewById(R.id.tvTopicMsgCount);
        }
    }
}
