package com.example.notify;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Utiliser un gestionnaire pour retarder l'exécution du code après un certain délai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Vérifier si l'utilisateur est connecté dans Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Si l'utilisateur n'est pas connecté, rediriger vers l'écran de connexion
                if (currentUser == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    // Si l'utilisateur est connecté, rediriger vers l'écran principal
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

                // Fermer cette activité
                finish();
            }
        }, 1000);
    }
}
