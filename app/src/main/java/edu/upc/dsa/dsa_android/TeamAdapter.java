package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

    private List<Team> data;
    private Context context;
    private String myUsername;
    private String myTeamName;

    public TeamAdapter(List<Team> data, Context context, String username, String myTeamName) {
        this.data = data;
        this.context = context;
        this.myUsername = username;
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
        Team t = data.get(position);
        holder.tvName.setText(t.getName());
        holder.tvPoints.setText(t.getPoints() + " pts");

        if (t.getAvatar() != null && !t.getAvatar().isEmpty()) {
            Picasso.get().load(t.getAvatar()).into(holder.ivAvatar);
        }

        boolean isMyTeam = (myTeamName != null && myTeamName.equals(t.getName()));

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
                api.leaveTeam(myUsername).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Has abandonado el equipo " + t.getName(), Toast.LENGTH_SHORT).show();
                            if (context instanceof RankingActivity) {
                                ((RankingActivity) context).loadRanking();
                            }
                        } else {
                            Toast.makeText(context, "Error al abandonar: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                api.joinTeam(t.getName(), myUsername).enqueue(new Callback<Team>() {
                    @Override
                    public void onResponse(Call<Team> call, Response<Team> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Te has unido a " + t.getName(), Toast.LENGTH_SHORT).show();
                            if (context instanceof RankingActivity) {
                                ((RankingActivity) context).loadRanking();
                            }
                        } else {
                            Toast.makeText(context, "Error al unirse: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Team> call, Throwable t) {
                        Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() { return data.size(); }

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