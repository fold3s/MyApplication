package com.example.hao.smarthome;

import com.example.hao.smarthome.R;
import com.example.hao.smarthome.fragments.OneFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    BackGround backGround;
    public void listener(){
        BackGround.listener=new Emitter.Listener(){

            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj =  new JSONObject((String)args[0]);
                            String status=obj.getString("status");
                            String node=obj.getString("nodeCode");
                            Log.d("SYNC",obj.toString());
                            if(node.equals("1H1")){
                                if(status.equals("1")){
                                    OneFragment.fan.setImageResource(R.mipmap.fan_on);
                                    OneFragment.fan_flag=true;
                                    History.myIconset.add(R.mipmap.fan_on);
                                    History.myDataset.add("FAN 2");
                                    History.mAdapter.notifyDataSetChanged();

                                }
                                else if(status.equals("0")){
                                    OneFragment.fan.setImageResource(R.mipmap.fan_off);
                                    OneFragment.fan_flag=false;
                                    History.myIconset.add(R.mipmap.fan_off);
                                    History.myDataset.add("FAN 2");
                                    History.mAdapter.notifyDataSetChanged();

                                }
                            }
                            else if(node.equals("1H2")){
                                if(status.equals("1")){
                                    OneFragment.led.setImageResource(R.mipmap.led_on);
                                    OneFragment.led_flag=true;
                                    History.myIconset.add(R.mipmap.led_on);
                                    History.myDataset.add("LED 2");
                                    History.mAdapter.notifyDataSetChanged();
                                }
                                else if(status.equals("0")){
                                    OneFragment.led.setImageResource(R.mipmap.led_off);
                                    OneFragment.led_flag=false;
                                    History.myIconset.add(R.mipmap.led_off);
                                    History.myDataset.add("LED 2");
                                    History.mAdapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //backGround = new BackGround();
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.controller);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        BackGround.mSocket.connect();
        listener();
        BackGround.mSocket.on("SYNC",BackGround.listener);
        /**
         * Setup click events on the Navigation View Items.
         */
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //  mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.logout) {
                    Intent intent = new Intent(MainActivity.this, Sign_In.class);
                    startActivity(intent);
                    //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    // fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                }else if(menuItem.getItemId()==R.id.home){
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.history) {
                    Intent intent = new Intent(MainActivity.this, History.class);
                    startActivity(intent);
                } /*else if (menuItem.getItemId() == R.id.help) {
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.setting) {
                    Intent intent = new Intent(MainActivity.this, Setting.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.automode) {
                    Intent intent = new Intent(MainActivity.this, Mode.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.setting) {
                    Intent intent = new Intent(MainActivity.this, Setting.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.permissions) {
                    Intent intent = new Intent(MainActivity.this, Permission.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.Control) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.user) {
                    Intent intent = new Intent(MainActivity.this, Account.class);
                    startActivity(intent);
                }*/


                return false;
            }

        });


        /*mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);



        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu,menu);
        return true;
    }



}