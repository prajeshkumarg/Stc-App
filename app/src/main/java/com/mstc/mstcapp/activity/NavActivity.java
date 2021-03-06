package com.mstc.mstcapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.fragments.ExclusiveFragment;
import com.mstc.mstcapp.fragments.FeedFragment;
import com.mstc.mstcapp.fragments.HighlightFragment;
import com.mstc.mstcapp.fragments.InformationFragment;
import com.mstc.mstcapp.fragments.ResourcesFragment;
import com.mstc.mstcapp.fragments.highlights.EventFragment;

public class NavActivity extends AppCompatActivity {

    public static TextView appBarTitle;
    public static ConstraintLayout appBar;
    private int backButtonCount = 0;
    private int prevPage=0;
    private int currentPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        appBarTitle = findViewById(R.id.appBarTitle);
        appBar = findViewById(R.id.appBar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            bottomNavigationView.getMenu().removeItem(R.id.nav_exclusive);
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new FeedFragment())
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item)
            {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Fragment selectedFragment= new FeedFragment();
                                backButtonCount = 0;
                                switch (item.getItemId()){
                                    case R.id.nav_home:
                                        selectedFragment = new FeedFragment();
                                        currentPage=0;
                                        break;
                                    case R.id.nav_resources:
                                        selectedFragment = new ResourcesFragment();
                                        currentPage=1;
                                        break;
                                    case R.id.nav_archive:
                                        selectedFragment = new HighlightFragment();
                                        currentPage=2;
                                        break;
                                    case R.id.nav_exclusive:
                                        selectedFragment = new ExclusiveFragment();
                                        currentPage=3;
                                        break;
                                    case R.id.nav_info:
                                        selectedFragment = new InformationFragment();
                                        currentPage=4;
                                        break;
                                }

                                if(prevPage<currentPage)
                                {
                                    prevPage=currentPage;
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                                            .replace(R.id.nav_host_fragment, selectedFragment)
                                            .commit();
                                }
                                else if(currentPage<prevPage)
                                {
                                    prevPage=currentPage;
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                                            .replace(R.id.nav_host_fragment, selectedFragment)
                                            .commit();
                                }

                            }
                        });
                return true;
            }
        });

       // bottomNavigationView.getMenu().removeItem(R.id.nav_exclusive);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(android.R.id.content),"Press back again to exit.",Snackbar.LENGTH_SHORT).setAnchorView(R.id.nav_view).setBackgroundTint(getColor(R.color.colorPrimary)).setActionTextColor(getColor(R.color.white)).show();
            backButtonCount++;
        }
    }
}