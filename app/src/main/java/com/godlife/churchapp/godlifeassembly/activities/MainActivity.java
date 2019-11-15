package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.godlife.churchapp.godlifeassembly.BuildConfig;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.fragments.AudioMessages;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchArticles;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchSongs;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchUnits;
import com.godlife.churchapp.godlifeassembly.fragments.HomeFragment;
import com.godlife.churchapp.godlifeassembly.fragments.LocateChurch;
import com.godlife.churchapp.godlifeassembly.fragments.VideoMessages;
import com.godlife.churchapp.godlifeassembly.live_service.LiveService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        //navigationView.setItemBackground(R.drawable.box);
        getHomeFragment();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);


        return true;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;
        Class fragmentClass = null;


        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                getSupportActionBar().setTitle("Home");
                break;

            case R.id.nav_church_songs:

                fragmentClass = ChurchSongs.class;
                getSupportActionBar().setTitle("Church Songs");
                break;

            case R.id.nav_about_us:
                Intent my_profile = new Intent(MainActivity.this, AboutUs.class);
                startActivity(my_profile);
                break;

            case R.id.nav_audio_messages:
                fragmentClass = AudioMessages.class;
                getSupportActionBar().setTitle("Audio Messages");
                break;

            case R.id.nav_video_messages:
                fragmentClass = VideoMessages.class;
                getSupportActionBar().setTitle("Video Messages");
                break;

            case R.id.nav_articles:
                fragmentClass = ChurchArticles.class;
                getSupportActionBar().setTitle("Church Articles");
                break;

            case R.id.nav_live_service:
                fragmentClass = LiveService.class;
                getSupportActionBar().setTitle("Live Service");
                break;

            case R.id.nav_chat:

                Intent chats = new Intent(MainActivity.this, Chats.class);
                startActivity(chats);
                break;

            case R.id.nav_church_units:
                fragmentClass = ChurchUnits.class;
                getSupportActionBar().setTitle("Church Units");
                break;

            case R.id.nav_givings:

                Intent notices = new Intent(MainActivity.this, Notices.class);
                startActivity(notices);
                break;

            case R.id.nav_locate_church:
                fragmentClass = LocateChurch.class;
                getSupportActionBar().setTitle("Locate Church");
                break;

            case R.id.nav_share:

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "God-Life Assembly International");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                fragmentClass = HomeFragment.class;
                getSupportActionBar().setTitle("Home");
                break;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void getHomeFragment() {

        Fragment home = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        getSupportActionBar().setTitle("Home");
        fragmentTransaction.replace(R.id.frame_layout, home);
        fragmentTransaction.commit();
    }

}
