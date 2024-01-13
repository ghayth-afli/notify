package com.example.notify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Mettre à jour les vues avec les données de la note
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextview.setText(Utility.timestampToString(note.timestamp));

        // Définir un écouteur de clic sur l'élément du RecyclerView
        holder.itemView.setOnClickListener((v) -> {
            // Ouvrir NoteDetailsActivity avec les détails de la note
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            // Obtenir l'ID du document Firestore associé à la note
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Créer une nouvelle vue pour chaque élément du RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    // Classe interne pour représenter chaque élément du RecyclerView
    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, contentTextView, timestampTextview;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialiser les vues à partir de la mise en page recycler_note_item.xml
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextview = itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
