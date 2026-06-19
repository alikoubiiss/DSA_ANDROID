package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private final List<Team> data;
    private final Context context;
    private final String username;
    private final String myTeamName;

    public TeamAdapter(List<Team> data, Context context, String username, String myTeamName) {
        this.data = data;
        this.context = context;
        this.username = username;
        this.myTeamName = myTeamName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team team = data.get(position);
        holder.tvName.setText(team.getName());
        holder.tvPoints.setText(team.getPoints() + " pts");

        if (team.getAvatar() != null && !team.getAvatar().isEmpty()) {
            Picasso.get().load(team.getAvatar()).into(holder.ivAvatar);
        }

        boolean isMyTeam = myTeamName != null && myTeamName.equals(team.getName());
        if (isMyTeam) {
            holder.btnJoin.setText("ABANDONAR");
            holder.btnJoin.setBackgroundResource(R.drawable.td_button_red);
        } else {
            holder.btnJoin.setText("UNIRSE");
            holder.btnJoin.setBackgroundResource(R.drawable.td_button_green);
        }

        holder.btnJoin.setOnClickListener(v -> {
            ApiService api = RetrofitClient.getInstance().getApi();
            if (isMyTeam) {
                leaveTeam(api);
            } else {
                joinTeam(api, team.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void joinTeam(ApiService api, String teamName) {
        api.joinTeam(teamName, username).enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                reloadRanking();
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                reloadRanking();
            }
        });
    }

    private void leaveTeam(ApiService api) {
        api.leaveTeam(username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                reloadRanking();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                reloadRanking();
            }
        });
    }

    private void reloadRanking() {
        if (context instanceof TeamsActivity) {
            ((TeamsActivity) context).loadRanking();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPoints;
        ImageView ivAvatar;
        Button btnJoin;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvPoints = v.findViewById(R.id.tvPoints);
            ivAvatar = v.findViewById(R.id.ivAvatar);
            btnJoin = v.findViewById(R.id.btnJoin);
        }
    }
}
