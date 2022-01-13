package com.lucas.signpost.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lucas.signpost.MainApplication;
import com.lucas.signpost.R;
import com.lucas.signpost.data.MessageRepository;
import com.lucas.signpost.databinding.ActivityLoginBinding;

import javax.inject.Inject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Inject
    MessageRepository messageRepository;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainApplication)getApplicationContext()).applicationComponent.injectLogin(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signInWithGoogle();
    }


    private void signInWithGoogle() {
        String googleClientId = getResources().getString(R.string.google_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleClientId)
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        ActivityResultLauncher<Intent> resultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Task<GoogleSignInAccount> task =
                                    GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignInResult(task);
                        });
        resultLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()) {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String token = account.getIdToken();
                Credentials googleCredentials =
                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
                String realmAppId = getResources().getString(R.string.realm_app_id);
                App app = new App(new AppConfiguration.Builder(realmAppId).build());
                app.loginAsync(googleCredentials, it -> {
                    if (it.isSuccess()) {
                        User login = it.get();
                        if (login.isLoggedIn()) {
                            messageRepository.init(app);
                            Intent intent = new Intent(this, MapsActivity.class);
                            startActivity(intent);
                        } else {
                            this.finishAndRemoveTask();
                        }
                    } else {
                        this.finishAndRemoveTask();
                    }
                });
            } else {
                Log.e("AUTH", "Google Auth failed: "
                        + completedTask.getException().toString());
            }
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
        }
    }


}