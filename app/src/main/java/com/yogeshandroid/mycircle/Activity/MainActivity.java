package com.yogeshandroid.mycircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yogeshandroid.mycircle.Adapters.MainFragmentStateAdapter;
import com.yogeshandroid.mycircle.Activity.Login.LogIn;
import com.yogeshandroid.mycircle.Fragment.Home.HomeFragment;
import com.yogeshandroid.mycircle.Fragment.Notification.NotificationFragment;
import com.yogeshandroid.mycircle.Fragment.Search.SearchFragment;
import com.yogeshandroid.mycircle.Fragment.Setting.SettingFragment;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    User u;
    String uId;
    public static final int NOTIFICATION = 357;
    public static final String TAG = "TAG_MAINACTIVITY";
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInOptions gso;
    User theUser;
    GoogleSignInClient gsc;
    String topic;

    private String[] titles = {"Chats", "Stories", "Calls"};
    MainFragmentStateAdapter adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("happyTalk", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        openDrawer();
        bottomNavigation();

//        setSupportActionBar(binding.toolBar);
        binding.txtHeading.setText("Happy Talk");

        // fragment setup
        adapter = new MainFragmentStateAdapter(this);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeFragment()).commit();
//        binding.viewPager.setAdapter(adapter);
//        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (((tab, position) -> tab.setText(titles[position])))).attach();


        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("112707830067-98rubfk7u7940g5ssc1d0c21giqtdret.apps.googleusercontent.com")
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(MainActivity.this, gso);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.floatingActionButton.setOnClickListener(v -> {
            Toast.makeText(this, "open profile activity here", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), .class));
        });


    }

    private void bottomNavigation() {
        binding.navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                binding.txtHeading.setText("Happy Talk");

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
            } else if (item.getItemId() == R.id.search) {
                binding.txtHeading.setText("Search");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();
            } else if (item.getItemId() == R.id.notification) {
                binding.txtHeading.setText("Notification");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
            } else if (item.getItemId() == R.id.setting) {
                binding.txtHeading.setText("Setting");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestRuntimePermissionFunc("notification");
        } else {
            getTopic();
        }
    }

    private void openDrawer() {

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar
                , 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        binding.drawernavigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.instagram) {
                Toast.makeText(this, "Instagram", Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
//                    binding.textView.setText("Home");
            } else if (item.getItemId() == R.id.logout) {
                properlySignout();
            }
            return true;
        });
    }

    private void getTopic() {

        FirebaseUser firebaseUser = auth.getCurrentUser();
        String tipic = firebaseUser.getEmail().split("@")[0];
        FirebaseMessaging.getInstance().subscribeToTopic(tipic).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "TOPIC FAILED " + task.getException().toString());
            }
        });


//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.d(TAG, "getToken: failed" + task.getException());
//                Toast.makeText(this, "error while generating token", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            topic = task.getResult();
//
//            Log.d(TAG + "ToKeN ", topic);
//
//            FirebaseUser firebaseUser = auth.getCurrentUser();
//            assert firebaseUser != null;
//            uId = firebaseUser.getUid();
////            database.getReference().child("Users").child(id).setValue(user);
//
//            DatabaseReference reference = database.getReference().child("Users");
//
//// Attach a listener to read the data at our posts reference
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        u = snapshot.getValue(User.class);
//                        if (uId.equals(u.getUserId())) {
//                            theUser = u;
//                            break;
//                        }
//                    }
//
//                    theUser = new User(u.getProfilePic(), u.getUserName(), u.getMail(), u.getPassword(), u.getUserId(), u.getLastMsg(), u.getTopic());
//                    if (!theUser.getTopic().equals(topic)) {
//                        //  update krna he to bhi hashmap ki help se krenge
//                        database.getReference().child("Users").child(uId).child("deviceToken").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                HashMap<String, Object> map = new HashMap<>();
//                                map.put("deviceToken", topic);
//                                database.getReference().child("Users").child(uId).updateChildren(map);
//                                Log.d(TAG + "TOKEN_UPDATED", topic);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    System.out.println("The read failed: " + databaseError.getCode());
//                }
//            });
//
//        });
    }

    private void requestRuntimePermissionFunc(String str) {
        if (str.equals("notification")) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                getTopic();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("this permission is required for this and this")
                        .setTitle("notification required")
                        .setCancelable(false)
                        .setPositiveButton("accept", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION))
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getTopic();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


    private void properlySignout() {
        String tipic = auth.getCurrentUser().getEmail().split("@")[0];
        FirebaseMessaging.getInstance().unsubscribeFromTopic(tipic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (sharedPreferences.contains("google")) {
                    editor.remove("google");
                    gsc.signOut();
                }
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LogIn.class));
                finish();


            }
        });
        // Attach a listener to read the data at our posts reference
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//        DatabaseReference reference = database.getReference().child("Users");
//
//                database.getReference().child("Users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("deviceToken", "");
//                        database.getReference().child("Users").child(uId).updateChildren(map);
//                        Log.d(TAG + "TOKEN_REMOVED", topic);
//
//                        auth.signOut();
//                        startActivity(new Intent(MainActivity.this, LogIn.class));
//                        finish();
////                gsc.signOut();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });

    }
}