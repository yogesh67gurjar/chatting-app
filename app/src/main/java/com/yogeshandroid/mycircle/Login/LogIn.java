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

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityLogInBinding;

import java.io.IOException;
import java.util.Arrays;

public class LogIn extends AppCompatActivity {
    ActivityLogInBinding binding;
    public static final int NOTIFICATION = 357;
    private static final String TAG = "TAG_LogIn";
    FirebaseAuth auth;

//
//    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestRuntimePermissionFunc("notification");
//        } else {
//            getToken();
//        }

//        auth = FirebaseAuth.getInstance();


        // the requestIdToken copied from client-id from google-services.json
//        gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1011067988925-p90igopfruf8v167gnrt31bl1c2s1ng5.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//
//        gsc = GoogleSignIn.getClient(SignInActivity.this, gso);

        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = gsc.getSignInIntent();
//                startActivityForResult(intent, 100);
            }
        });

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog.show();
                String email = binding.emailEt.getText().toString();
                String password = binding.passwordEt.getText().toString();
                if (email.isEmpty()) {
                    binding.emailEt.setError("enter Email");
                    binding.emailEt.requestFocus();
                }
                if (password.isEmpty()) {
                    binding.passwordEt.setError("enter Password");
                    binding.passwordEt.requestFocus();
                }
                if (!email.isEmpty() && !password.isEmpty()) {
//                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            progressDialog.dismiss();
//                            if(task.isSuccessful())
//                            {
//                                startActivity(new Intent(SignInActivity.this,LogIn.class));
//                            }
//                            else
//                            {
//                                Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });

        // it represent user information
        // app khulte se hi check kro k already koi user login he kya and login he to usko pahucha do ProfileActivity pe
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
        // agar user already sign in he to ProfileActivity pr jao
        startActivity(new Intent(LogIn.this, LogIn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }

//        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LogIn.this, LogIn.class));
        }

        binding.clickforsignupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

    }  // onCreate khatam


    @Override
    protected void onResume() {
        super.onResume();



    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "getToken: failed" + task.getException());
                Toast.makeText(this, "error while generating token", Toast.LENGTH_SHORT).show();
                return;
            }

            String deviceToken = task.getResult();

            Log.d(TAG + "TOKEN ", deviceToken);

        });
    }

    private void requestRuntimePermissionFunc(String str) {
        if (str.equals("notification")) {

            if (ContextCompat.checkSelfPermission(LogIn.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                getToken();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(LogIn.this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                builder.setMessage("this permission is required for this and this")
                        .setTitle("notification required")
                        .setCancelable(false)
                        .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LogIn.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION);
                            }
                        })
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                ActivityCompat.requestPermissions(LogIn.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "accepted", Toast.LENGTH_SHORT).show();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(LogIn.this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                builder.setMessage("this feature is unavailable , now open settings ")
                        .setTitle("notification to chaiye")
                        .setCancelable(false)
                        .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                requestRuntimePermissionFunc("notification");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // task means asynchronous operations
            // mtlb signedInAccount lelo background me
//            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
//                    .getSignedInAccountFromIntent(data);
//
//            if (signInAccountTask.isSuccessful()) {
//                String s = "Google sign in successful";
//                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//
//                try {
//                    GoogleSignInAccount googleSignInAccount = signInAccountTask
//                            .getResult(ApiException.class);
//                    if (googleSignInAccount != null) {
//                        // When sign in account is not equal to null
//                        // Initialize auth credential
//                        AuthCredential authCredential = GoogleAuthProvider
//                                .getCredential(googleSignInAccount.getIdToken()
//                                        , null);
//                        // Login krdo id and password se
//                        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//
//                                    // login hua to jao ProfileActivity me
//                                    startActivity(new Intent(SignInActivity.this,LogIn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                                    Toast.makeText(SignInActivity.this, "Firebase authentication successful", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(SignInActivity.this, "Authentication Failed :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
        }

    }
}