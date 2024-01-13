package com.example.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
    // Déclarer les éléments de l'interface utilisateur (UI)
    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        // Initialiser les éléments de l'interface utilisateur
        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view);

        // Recevoir les données passées depuis l'activité précédente
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        // Vérifier si l'édition est activée
        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        // Afficher les données dans les champs d'édition
        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode) {
            pageTitleTextView.setText("Modifier votre note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        // Définir les écouteurs de clic pour les boutons
        saveNoteBtn.setOnClickListener((v) -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener((v) -> deleteNoteFromFirebase());
    }

    // Méthode pour sauvegarder la note
    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Le titre est requis");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    // Méthode pour sauvegarder la note dans Firebase
    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {
            // Mettre à jour la note existante
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            // Créer une nouvelle note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // La note est ajoutée avec succès
                    Utility.showToast(NoteDetailsActivity.this, "Note ajoutée avec succès");
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Échec de l'ajout de la note, veuillez réessayer");
                }
            }
        });
    }

    // Méthode pour supprimer la note de Firebase
    void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // La note est supprimée avec succès
                    Utility.showToast(NoteDetailsActivity.this, "Note supprimée avec succès");
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Échec de la suppression de la note, veuillez réessayer");
                }
            }
        });
    }
}
