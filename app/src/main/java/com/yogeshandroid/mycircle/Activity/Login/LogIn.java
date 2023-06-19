package com.yogeshandroid.mycircle.Activity.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.yogeshandroid.mycircle.Activity.MainActivity;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.databinding.ActivityLogInBinding;

public class LogIn extends AppCompatActivity {
    ActivityLogInBinding binding;
    public static final int NOTIFICATION = 357;
    private static final String TAG = "TAG_LogIn";
    FirebaseAuth auth;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("happyTalk", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        // the requestIdToken copied from client-id from google-services.json
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("112707830067-oubj9nvc4dnu6cvjvldnt813n804kq25.apps.googleusercontent.com")
                .requestEmail()
                .build();
//
        gsc = GoogleSignIn.getClient(LogIn.this, gso);

        binding.googleBtn.setOnClickListener(v -> {
//            Toast.makeText(this, "sign in with google", Toast.LENGTH_SHORT).show();
            Intent intent = gsc.getSignInIntent();
            startActivityForResult(intent, 100);
            binding.progressCard.setVisibility(View.VISIBLE);
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

//        it represent user information
//        app khulte se hi check kro k already koi user login he kya and login he to usko pahucha do ProfileActivity pe
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//        agar user already sign in he to ProfileActivity pr jao
//        startActivity(new Intent(LogIn.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }


        binding.clickForSignUp.setOnClickListener(v -> startActivity(new Intent(LogIn.this, SignUpActivity.class)));

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
                binding.progressCard.setVisibility(View.GONE);

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
                                editor.putString("google", "yes");
                                editor.commit();
                                editor.apply();
                                FirebaseUser user=auth.getCurrentUser();

                                User users = new User(user.getPhotoUrl().toString(), user.getDisplayName(), user.getEmail(), "",user.getUid(),"","","");

                                database.getReference().child("Users").child(user.getUid()).setValue(users);

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
            } else {
                Exception exception = signInAccountTask.getException();
                String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
                Toast.makeText(this, "Sign-in failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                binding.progressCard.setVisibility(View.GONE);
                Log.e(TAG, "Sign-in failed: " + errorMessage, exception);
            }
        }
    }
}