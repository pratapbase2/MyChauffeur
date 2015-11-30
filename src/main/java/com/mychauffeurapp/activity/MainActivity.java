package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
//import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.adapter.myRideAdapter;
import com.mychauffeurapp.fragments.BookingFragment;
import com.mychauffeurapp.fragments.MapFragmnt;
import com.mychauffeurapp.navdrawer.NavDrawerItem;
import com.mychauffeurapp.navdrawer.NavDrawerListAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mDrawerRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private myRideAdapter rideAdapter;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.app.ActionBar ab= getActionBar();
       /* try {*/

            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((TextView) v.findViewById(R.id.title)).setText(this.getTitle());

            this.getActionBar().setCustomView(v);

            mTitle = mDrawerTitle = getTitle();

            Bundle extras = getIntent().getExtras();
            userid = extras.getString("UserId");



        /*assert ab != null;
        ab.show();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);


        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((TextView)v.findViewById(R.id.title)).setText(this.getTitle());

        ab.setCustomView(v);

        //Log.e("MainActivity",ab+"");
        mTitle = mDrawerTitle = getTitle();*/

            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

            // nav drawer icons from resources
            navMenuIcons = getResources()
                    .obtainTypedArray(R.array.nav_drawer_icons);

            mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

            navDrawerItems = new ArrayList<NavDrawerItem>();

            // adding nav drawer items to array
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            // Find People
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));

            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
            // Photos
            //navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

            //navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

            //navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
            // Communities, Will add a counter here
        /*navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		*/

            // Recycle the typed array
            navMenuIcons.recycle();


            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems);
            mDrawerList.setAdapter(adapter);

            // enabling action bar app icon and behaving it as toggle button
       /* getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);*/

        /*mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){*/
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.menu_icon, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    hideKayboard();
                    getActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    hideKayboard();
                    getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

            if (savedInstanceState == null) {
                // on first time display view for first nav item
                displayView(0);
            }

        /*}
        catch (Exception e){
            generateNoteOnSD("MainActivity"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }

   /* public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Mychauffeur Logs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
		/*switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}*/
        return super.onOptionsItemSelected(item);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerRelativeLayout);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                Bundle bundle1=new Bundle();
                bundle1.putString("UserId", userid);
                fragment = new MapFragmnt();
                fragment.setArguments(bundle1);
                break;
            /*case 1:
                fragment = new UserFragment();
                break;*/
            case 1:
                Bundle bundle=new Bundle();
                bundle.putString("UserId", userid);
                fragment = new BookingFragment();
                fragment.setArguments(bundle);

                break;
            case 2:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Do you want to Logout?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteDatabase("MychauffeurAppProfile");

                        Intent checkIntent = new Intent(getApplicationContext(), LoginScreen.class);
                        checkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(checkIntent);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                /*Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                //nbutton.setBackgroundColor(Color.rgb(204, 204, 204));
                //nbutton.setTextColor(Color.rgb(0, 0, 0));
                Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                //pbutton.setBackgroundColor(Color.rgb(59, 144, 208));
                //pbutton.setTextColor(Color.rgb(0,0,0));

                pbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
                break;
            /*case 2 :
                Bundle bundle1=new Bundle();
                bundle1.putString("UserId", userid);
                fragment = new LogoutFragment();
                fragment.setArguments(bundle1);
                break;
            /*case 3:
                fragment = new ReviewFragment();
                break;
            case 3:
                fragment = new InviteFragment();
                break;*/

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack("Frag1")
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            hideKayboard();
            displayView(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag("Frag1") != null) {
            getFragmentManager().popBackStackImmediate("Frag1",0);
        } else {
            //super.onBackPressed();
            /*Intent in = new Intent(MainActivity.this,MainActivity.class);
            in.putExtra("UserId",userid);
            startActivity(in);*/
            finish();
        }
    }
    private void hideKayboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


}
