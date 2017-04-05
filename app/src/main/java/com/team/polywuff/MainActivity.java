package com.team.polywuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.team.polywuff.Messenger.GroupChannelListFragment;
import com.team.polywuff.Utils.PreferenceUtils;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // sendbird
    private static final String APP_ID = "A41D56FF-D61E-46ED-A1A4-09B46FD304FD";

    //Firebase variables
    private FirebaseAuth firebaseAuth;

    private Toolbar mToolbar;
    private NavigationView navigationView;


    // sendbird email
    private String email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sendbird auth
        SendBird.init(APP_ID,getApplicationContext());


        // we can fetch users details by making a firebase user object
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();

        //if user is equal to null go back
        if (user == null)
        {
            backToLogin();
        }else{
            // get the email from firebase
            email = user.getEmail();
        }


        // behind the scenes with auth with sendbird
        SendBird.connect(email, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, "sendbird error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
                new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                        if(e!=null){
                            Toast.makeText(MainActivity.this, "error sendbird push token", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        SendBird.updateCurrentUserInfo(email, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if(e!=null){
                    Toast.makeText(MainActivity.this, "update handler error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        String channelUrl = getIntent().getStringExtra("groupChannelUrl");




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Logout button TEMP!
    public void getMeOut(){
        // Facebook already has a logout method
        // which can be called anywhere in our application!
        LoginManager.getInstance().logOut();
        // logout firebase
        FirebaseAuth.getInstance().signOut();
        backToLogin();

        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT)
                        .show();

                SendBird.disconnect(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(MainActivity.this, false);
                        Intent intent = new Intent(getApplicationContext(), FacebookLogin.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void backToLogin() {
        Intent intent = new Intent(this,FacebookLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Intent intent;

        if (id == R.id.nav_home) {
            // Handle the action
        } else if (id == R.id.nav_profile) {
            //this is the code for opening activities

            //Intent pIntent = new Intent(this, Profile.class);
            //startActivity(pIntent);

        }
        else if (id == R.id.nav_search)
        {
            Fragment fragment = GroupChannelListFragment.newInstance();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction().replace(R.id.container_main,fragment).commit();



        }
        else if (id == R.id.nav_messenger) {
            // messeneger
            Fragment fragment = GroupChannelListFragment.newInstance();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.container_main,fragment)
                    .commit();



        } else if (id == R.id.nav_map) {
            intent = new Intent(this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_pedometer) {
            // If user presses pedometer go to the pedometer activity
            intent = new Intent(this,Pedometer.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            //brings the user back to the login activity
            getMeOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
