package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder> {

    private List<GameObject> gameObjects;
    private LayoutInflater inflater;
    private OnItemBuyClickListener buyClickListener;

    public interface OnItemBuyClickListener {
        void onBuyClick(GameObject gameObject);
    }

    public TiendaAdapter(Context context, List<GameObject> gameObjects, OnItemBuyClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.gameObjects = gameObjects;
        this.buyClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_tienda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameObject gameObject = gameObjects.get(position);
        holder.tvObjectName.setText(gameObject.getNombre());
        holder.tvObjectPrice.setText("Precio: " + gameObject.getPrecio() + " monedas");

        holder.btnComprar.setOnClickListener(v -> {
            if (buyClickListener != null) {
                buyClickListener.onBuyClick(gameObject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObjectName;
        TextView tvObjectPrice;
        Button btnComprar;

        ViewHolder(View itemView) {
            super(itemView);
            tvObjectName = itemView.findViewById(R.id.textViewObjectName);
            tvObjectPrice = itemView.findViewById(R.id.textViewObjectPrice);
            btnComprar = itemView.findViewById(R.id.btnComprar);
        }
    }
}