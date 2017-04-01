package katheria.vhp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import katheria.vhp.Fragment.HomeFragment;
import katheria.vhp.Fragment.ProfileFragment;
import katheria.vhp.Http.DataParser;
import katheria.vhp.Http.HttpCall;
import katheria.vhp.Model.Model_userDetails;
import katheria.vhp.R;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String useremail;
    Context context = AccountActivity.this;
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    TextView nav_name,nav_email;
    public static DataParser dp;
    String Name ,Email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Bundle bundle =getIntent().getExtras();
        useremail=bundle.getString("Email");
        Toast.makeText(AccountActivity.this,"Welcome you are logged in using,\n" + useremail,Toast.LENGTH_LONG).show();
        new HttpCall().getUserDetails(context, useremail);
        HomeFragment fragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dp=new DataParser();
        //Email= dp.getArrayList().get(0).email;
        //Name = dp.getArrayList().get(0).name;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        nav_name = (TextView) header.findViewById(R.id.nav_name);
        nav_email = (TextView) header.findViewById(R.id.nav_email);
//        nav_name.setText(dp.getArrayList().get(0).name);
      //  nav_email.setText(Email);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            HomeFragment fragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.profile) {
            ProfileFragment fragment = new ProfileFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.share) {

        } else if (id == R.id.feedback) {

        }
        else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
