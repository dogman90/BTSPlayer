package io.swnomad.btsplayer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import io.swnomad.btsplayer.fragments.FavoriteFragment;
import io.swnomad.btsplayer.fragments.LiveFragment;
import io.swnomad.btsplayer.fragments.LogFragment;
import io.swnomad.btsplayer.fragments.MuvieFragment;
import io.swnomad.btsplayer.fragments.VideoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;

    VideoFragment muvieFragment;
    VideoFragment liveFragment;
    VideoFragment logFragment;

    FavoriteFragment favoriteFragment;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = openOrCreateDatabase("favorites", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
        String sql = "create table if not exists videos(title text, videoId text, pubDate text, thumbnailUrl text);";

        db.execSQL(sql);
        db.close();

        /* 액션바로 직접 만든 toolbar를 적용 */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* 액션바 타이틀 가림 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        VideoInfoReader.load();

        muvieFragment = new MuvieFragment();
        liveFragment = new LiveFragment();
        logFragment = new LogFragment();

        favoriteFragment = new FavoriteFragment();

        /* 첫 번째 프래그먼트가 보이도록 설정 */
        getSupportFragmentManager().beginTransaction().replace(R.id.container, liveFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        TextView titleView = findViewById(R.id.titleView);

        switch(id){
            case R.id.nav_muvie:
                titleView.setText(R.string.muvie);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, muvieFragment).commit();
                break;
            case R.id.nav_live:
                titleView.setText(R.string.live);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, liveFragment).commit();
                break;
            case R.id.nav_log:
                titleView.setText(R.string.vlog);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, logFragment).commit();
                break;
            case R.id.nav_favorite:
                titleView.setText(R.string.favorites);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, favoriteFragment).commit();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=io.swnomad.btsplayer"); // 공유할 text
                String strShareAppLink = getResources().getString(R.string.shareAppLink);
                Intent chooser = Intent.createChooser(intent, strShareAppLink);//intent 제목
                startActivity(chooser);
                break;
            case R.id.nav_remove_ad:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(
                        "market://details?id=io.swnomad.noadbtsplayer"));
                startActivity(intent2);
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}