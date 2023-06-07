package com.yogeshandroid.mycircle.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yogeshandroid.mycircle.MainActivity;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityLogInBinding;

import java.io.IOException;
import java.util.Arrays;

public class LogIn extends AppCompatActivity {
    ActivityLogInBinding binding;
    public static final int NOTIFICATION = 357;
    private static final String TAG = "TAG_LogIn";
    FirebaseAuth auth;

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();


        // the requestIdToken copied from client-id from google-services.json
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("112707830067-98rubfk7u7940g5ssc1d0c21giqtdret.apps.googleusercontent.com")
                .requestEmail()
                .build();
//
        gsc = GoogleSignIn.getClient(LogIn.this, gso);

        binding.googleBtn.setOnClickListener(v -> {
            Intent intent = gsc.getSignInIntent();
            startActivityForResult(intent, 100);
        });

        binding.logInBtn.setOnClickListener(v -> {

            String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();
            if (email.isEmpty()) {
                binding.emailEt.setError("enter Email");
                binding.emailEt.requestFocus();
            } else if (password.isEmpty()) {
                binding.passwordEt.setError("enter Password");
                binding.passwordEt.requestFocus();
            } else {
                binding.progressCard.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    binding.progressCard.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(LogIn.this, "Login Successful....", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogIn.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // it represent user information
        // app khulte se hi check kro k already koi user login he kya and login he to usko pahucha do ProfileActivity pe
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
////         agar user already sign in he to ProfileActivity pr jao
//            startActivity(new Intent(LogIn.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
//


        binding.clickForSignUp.setOnClickListener(v -> startActivity(new Intent(LogIn.this, SignUp.class)));

    }  // onCreate khatam


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // task means asynchronous operations
            // mtlb signedInAccount lelo background me
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()) {
                String s = "Google sign in successful";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Login krdo id and password se
                        auth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // login hua to jao ProfileActivity me
                                startActivity(new Intent(LogIn.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                Toast.makeText(LogIn.this, "Firebase authentication successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogIn.this, "Authentication Failed :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}