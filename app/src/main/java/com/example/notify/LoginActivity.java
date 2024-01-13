package com.example.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    // Déclarer les éléments de l'interface utilisateur (UI)
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser les éléments de l'interface utilisateur
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        // Définir les écouteurs de clic pour les boutons
        // loginBtn.setOnClickListener((v) -> loginUser()); // Commenté car la méthode n'est pas utilisée ici
        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    // Méthode pour connecter l'utilisateur
    void loginUser() {
        System.out.println("email+password");
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        System.out.println(email + password);
        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }
        System.out.println(email + password);
        loginAccountInFirebase(email, password);
    }

    // Méthode pour connecter l'utilisateur à Firebase
    void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    // La connexion est réussie
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // Aller à MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Utility.showToast(LoginActivity.this, "Email non vérifié, veuillez vérifier votre email");
                    }

                } else {
                    // Échec de la connexion
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }

            }
        });
    }

    // Méthode pour changer l'état de progression
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    // Méthode pour valider les données saisies par l'utilisateur
    boolean validateData(String email, String password) {
        // Valider les données saisies par l'utilisateur.

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("L'e-mail est invalide");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Le mot de passe est invalide");
            return false;
        }
        // validations ont réussi
        return true;
    }
}
