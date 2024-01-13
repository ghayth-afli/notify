package com.example.notify;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;

public class Utility {
    // Méthode pour afficher un message toast
    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Méthode pour obtenir une référence à la collection Firestore des notes de l'utilisateur actuel
    static CollectionReference getCollectionReferenceForNotes() {
        // Obtenir l'utilisateur actuel connecté à Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Retourner la référence à la collection "My_notes" dans le document de l'utilisateur
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("My_notes");
    }

    // Convertir un objet Timestamp en une chaîne de caractères formatée
    static String timestampToString(Timestamp timestamp) {
        // Utiliser un format de date spécifique (MM/dd/yy) pour convertir le timestamp en chaîne
        return new SimpleDateFormat("MM/dd/yy").format(timestamp.toDate());
    }
}
