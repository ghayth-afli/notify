package com.example.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    // Déclarer les éléments de l'interface utilisateur (UI)
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    // Adapter pour les notes
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les éléments de l'interface utilisateur
        addNoteBtn = findViewById(R.id.add_note_button);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        // Définir les écouteurs de clic pour les boutons
        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));
        menuBtn.setOnClickListener((v) -> showMenu());

        // Configurer la vue du recycler (liste) des notes
        setupRecyclerView();
    }

    // Méthode pour afficher le menu contextuel
    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Déconnexion");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Déconnexion")) {
                    // Déconnecter l'utilisateur et rediriger vers l'écran de connexion
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                return false;
            }
        });
    }

    // Méthode pour configurer la vue du recycler des notes
    private void setupRecyclerView() {
        // Définir la requête pour récupérer les notes triées par horodatage décroissant
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);

        // Configurer les options pour l'adaptateur FirestoreRecycler
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();

        // Configurer le gestionnaire de disposition et l'adaptateur pour le recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Commencer à écouter les modifications de la base de données Firebase
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Arrêter d'écouter les modifications de la base de données Firebase
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualiser l'adaptateur lors de la reprise de l'activité
        noteAdapter.notifyDataSetChanged();
    }
}
