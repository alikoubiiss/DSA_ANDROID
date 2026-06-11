package edu.upc.dsa.dsa_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<TeamMember> members;

    public MemberAdapter(List<TeamMember> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeamMember member = members.get(position);
        holder.tvName.setText(member.getName());
        holder.tvPoints.setText(member.getPoints() + " pts");

        // Load avatar using Picasso
        if (member.getAvatar() != null && !member.getAvatar().trim().isEmpty()) {
            Picasso.get()
                   .load(member.getAvatar())
                   .placeholder(R.mipmap.ic_launcher_round)
                   .error(R.mipmap.ic_launcher_round)
                   .into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvPoints = itemView.findViewById(R.id.tvUserPoints);
        }
    }
}