package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    public interface OnInscribirseClick {
        void onClick(Evento evento);
    }

    public interface OnVerInscritosClick {
        void onClick(Evento evento);
    }

    private final Context context;
    private List<Evento> eventos;
    private final OnInscribirseClick inscribirseListener;
    private final OnVerInscritosClick verInscritosListener;

    private int expandedEventId = -1;
    private final Map<Integer, List<UserEvent>> usersByEventId = new HashMap<>();
    private final Set<Integer> loadingEventIds = new HashSet<>();

    public EventoAdapter(Context context, List<Evento> eventos,
                         OnInscribirseClick inscribirseListener,
                         OnVerInscritosClick verInscritosListener) {
        this.context = context;
        this.eventos = eventos;
        this.inscribirseListener = inscribirseListener;
        this.verInscritosListener = verInscritosListener;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
        expandedEventId = -1;
        usersByEventId.clear();
        loadingEventIds.clear();
        notifyDataSetChanged();
    }

    public void setExpandedEvent(int eventId) {
        this.expandedEventId = eventId;
        notifyDataSetChanged();
    }

    public void collapseExpandedEvent() {
        this.expandedEventId = -1;
        notifyDataSetChanged();
    }

    public void setLoadingUsers(int eventId, boolean loading) {
        if (loading) {
            loadingEventIds.add(eventId);
        } else {
            loadingEventIds.remove(eventId);
        }
        notifyDataSetChanged();
    }

    public void setEventUsers(int eventId, List<UserEvent> users) {
        usersByEventId.put(eventId, users != null ? users : new ArrayList<>());
        loadingEventIds.remove(eventId);
        expandedEventId = eventId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        int eventId = evento.getId();
        boolean isExpanded = eventId == expandedEventId;

        holder.nombre.setText(evento.getName());
        holder.descripcion.setText(evento.getDescription());
        holder.fechaInicio.setText("Inicio: " + evento.getStartDate());
        holder.fechaFin.setText("Fin: " + evento.getEndDate());

        Picasso.get()
                .load(evento.getImage())
                .placeholder(R.drawable.combined_logo)
                .error(R.drawable.combined_logo)
                .into(holder.imagen);

        holder.inscribirseBtn.setOnClickListener(v -> {
            if (inscribirseListener != null) {
                inscribirseListener.onClick(evento);
            }
        });

        holder.verInscritosBtn.setOnClickListener(v -> {
            if (isExpanded) {
                expandedEventId = -1;
                notifyDataSetChanged();
                return;
            }

            if (usersByEventId.containsKey(eventId)) {
                expandedEventId = eventId;
                notifyDataSetChanged();
                return;
            }

            if (verInscritosListener != null) {
                verInscritosListener.onClick(evento);
            }
        });

        bindUsersSection(holder, eventId, isExpanded, evento.getName());
    }

    private void bindUsersSection(EventoViewHolder holder, int eventId, boolean isExpanded, String eventName) {
        holder.usersSection.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.usersTitle.setText("Inscritos en " + eventName);

        if (!isExpanded) {
            return;
        }

        if (loadingEventIds.contains(eventId)) {
            holder.progressBarUsers.setVisibility(View.VISIBLE);
            holder.textViewUsersEmpty.setVisibility(View.GONE);
            holder.recyclerViewUsers.setVisibility(View.GONE);
            return;
        }

        holder.progressBarUsers.setVisibility(View.GONE);
        List<UserEvent> users = usersByEventId.get(eventId);

        if (users == null || users.isEmpty()) {
            holder.recyclerViewUsers.setVisibility(View.GONE);
            holder.textViewUsersEmpty.setVisibility(View.VISIBLE);
            return;
        }

        holder.textViewUsersEmpty.setVisibility(View.GONE);
        holder.recyclerViewUsers.setVisibility(View.VISIBLE);
        holder.recyclerViewUsers.setAdapter(new EventUserAdapter(users));
    }

    @Override
    public int getItemCount() {
        return eventos != null ? eventos.size() : 0;
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView descripcion;
        TextView fechaInicio;
        TextView fechaFin;
        Button inscribirseBtn;
        Button verInscritosBtn;
        LinearLayout usersSection;
        TextView usersTitle;
        ProgressBar progressBarUsers;
        TextView textViewUsersEmpty;
        RecyclerView recyclerViewUsers;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.eventImage);
            nombre = itemView.findViewById(R.id.eventName);
            descripcion = itemView.findViewById(R.id.eventDescription);
            fechaInicio = itemView.findViewById(R.id.eventStartDate);
            fechaFin = itemView.findViewById(R.id.eventEndDate);
            inscribirseBtn = itemView.findViewById(R.id.eventButton);
            verInscritosBtn = itemView.findViewById(R.id.eventUsersButton);
            usersSection = itemView.findViewById(R.id.eventUsersSection);
            usersTitle = itemView.findViewById(R.id.eventUsersTitle);
            progressBarUsers = itemView.findViewById(R.id.progressBarEventUsers);
            textViewUsersEmpty = itemView.findViewById(R.id.textViewEventUsersEmpty);
            recyclerViewUsers = itemView.findViewById(R.id.recyclerViewEventUsers);

            recyclerViewUsers.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerViewUsers.setNestedScrollingEnabled(false);
        }
    }
}
