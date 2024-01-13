package com.example.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    // Déclarer les éléments de l'interface utilisateur (UI)
    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialiser les éléments de l'interface utilisateur
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        // Définir les écouteurs de clic pour les boutons
        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));
    }

    // Méthode pour initier le processus de création de compte
    void createAccount() {
        // Récupérer les saisies de l'utilisateur
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Valider les saisies de l'utilisateur
        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated) {
            return;
        }

        // Appeler la méthode pour créer un compte dans Firebase
        createAccountInFirebase(email, password);
    }

    // Méthode pour créer un compte dans Firebase
    void createAccountInFirebase(String email, String password) {
        // Afficher la progression pendant la création du compte
        changeInProgress(true);

        // Obtenir l'instance d'authentification Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Créer un utilisateur avec l'e-mail et le mot de passe à l'aide de l'authentification Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Masquer la progression après la tentative de création du compte
                changeInProgress(false);

                // Vérifier si la création du compte a réussi
                if (task.isSuccessful()) {
                    // Compte créé avec succès
                    Utility.showToast(CreateAccountActivity.this, "Compte créé avec succès, Vérifiez l'e-mail pour la vérification");

                    // Envoyer une vérification par e-mail à l'utilisateur
                    firebaseAuth.getCurrentUser().sendEmailVerification();

                    // Déconnecter l'utilisateur après la création du compte
                    firebaseAuth.signOut();
                } else {
                    // Échec de la création du compte, afficher le message d'erreur
                    Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    // Méthode pour basculer la visibilité de la barre de progression et du bouton de création de compte
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            // Afficher la barre de progression et masquer le bouton de création de compte
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else {
            // Masquer la barre de progression et afficher le bouton de création de compte
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    // Méthode pour valider les données saisies par l'utilisateur
    boolean validateData(String email, String password, String confirmPassword) {
        // Valider les données saisies par l'utilisateur.

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Afficher une erreur pour un e-mail invalide
            emailEditText.setError("L'e-mail est invalide");
            return false;
        }
        if (password.length() < 6) {
            // Afficher une erreur pour un mot de passe trop court
            passwordEditText.setError("Le mot de passe est trop court");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            // Afficher une erreur pour une non-correspondance des mots de passe
            confirmPasswordEditText.setError("Les mots de passe ne correspondent pas");
            return false;
        }
        // Toutes les validations ont réussi
        return true;
    }
}
