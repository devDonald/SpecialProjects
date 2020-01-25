package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.godlife.churchapp.godlifeassembly.fragments.ComingMeetings;
import com.godlife.churchapp.godlifeassembly.fragments.GivingFragment;
import com.godlife.churchapp.godlifeassembly.fragments.LoveNotes;
import com.godlife.churchapp.godlifeassembly.fragments.Notices;
import com.godlife.churchapp.godlifeassembly.fragments.PraiseReport;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.webkit.WebSettings;

import com.godlife.churchapp.godlifeassembly.BuildConfig;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchArticles;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchSongs;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchUnits;
import com.godlife.churchapp.godlifeassembly.fragments.HomeFragment;
import com.godlife.churchapp.godlifeassembly.fragments.LocateChurch;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseMessaging.getInstance().subscribeToTopic("General");
        FirebaseMessaging.getInstance().subscribeToTopic("Birthdays");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        //navigationView.setItemBackground(R.drawable.box);
        if (savedInstanceState==null){
            getHomeFragment();

        }
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
                getSupportActionBar().setTitle("Lyrics");
                break;

            case R.id.nav_about_us:
                Intent my_profile = new Intent(MainActivity.this, AboutUs.class);
                startActivity(my_profile);
                break;

            case R.id.nav_praise_report:
                fragmentClass = PraiseReport.class;
                getSupportActionBar().setTitle("Praise Report");
                break;

            case R.id.nav_love_note:
                fragmentClass = LoveNotes.class;
                getSupportActionBar().setTitle("Love Notes");
                break;
            case R.id.nav_gallery:
                toGallery();
                break;

            case R.id.nav_articles:
                fragmentClass = ChurchArticles.class;
                getSupportActionBar().setTitle("Church Articles");
                break;

            case R.id.nav_upcoming:
                fragmentClass = ComingMeetings.class;
                getSupportActionBar().setTitle("Upcoming Meetings");
                break;

            case R.id.nav_notices:

                fragmentClass = Notices.class;
                getSupportActionBar().setTitle("Notices");
                break;

            case R.id.nav_church_units:
                fragmentClass = ChurchUnits.class;
                getSupportActionBar().setTitle("Church Units");
                break;

            case R.id.nav_givings:

                fragmentClass = GivingFragment.class;
                getSupportActionBar().setTitle("Give");
                break;

            case R.id.nav_locate_church:
                fragmentClass = LocateChurch.class;
                getSupportActionBar().setTitle("Locate Church");
                break;

            case R.id.nav_contact:
                Intent toContact = new Intent(MainActivity.this, ContactUs.class);
                //toContact.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toContact);
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

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void toGallery(){
        String url = "https://photos.app.goo.gl/XuXxQrqDA43Lpk2c9";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        boolean canOpen = browserIntent.resolveActivity(getPackageManager()) != null;
        if (canOpen) {
            startActivity(browserIntent);
        }

    }
}
