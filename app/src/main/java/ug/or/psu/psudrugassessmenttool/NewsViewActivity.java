package ug.or.psu.psudrugassessmenttool;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsViewActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    CoordinatorLayout mRootLayout;
    ImageView image;
    TextView title, text, author, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        initInstances();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            /*pharmacy_name = extras.getString("pharmacy_name", "1");
            pharmacy_id = extras.getString("pharmacy_id", "1");
            pharmacy_name = extras.getString("pharmacy_name", "1");
            pharmacy_id = extras.getString("pharmacy_id", "1");
            pharmacy_name = extras.getString("pharmacy_name", "1");*/
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initInstances() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutAndroidExample);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorRootLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);
        mCollapsingToolbarLayout.setTitle("Conversions");
    }
}
