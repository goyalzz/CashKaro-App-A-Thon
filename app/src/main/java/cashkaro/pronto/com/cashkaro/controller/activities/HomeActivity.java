package cashkaro.pronto.com.cashkaro.controller.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.InjectView;
import cashkaro.pronto.com.cashkaro.R;
import cashkaro.pronto.com.cashkaro.adapters.HomeScreenRecyclerAdapter;
import cashkaro.pronto.com.cashkaro.controller.base.BaseActivity;
import cashkaro.pronto.com.cashkaro.dto.HomePageListViewDto;
import cashkaro.pronto.com.cashkaro.dto.HomePageListViewType;

@SuppressWarnings("deprecation")
public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeScreenRecyclerAdapter.ItemClick {

    @InjectView(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.home_logo)
    ImageView toolbarLogo;

    private List<HashMap<String, List<String>>> gridList = new ArrayList<HashMap<String, List<String>>>() {{
        add(new HashMap<String, List<String>>() {{
            put("Office Stationary Deals", Arrays.asList("Upto 70% Off all the \n books and stationary on \n Amazon", "Upto 70% Off all the \n books and stationary on \n Snapdeal"));
        }});

        add(new HashMap<String, List<String>>() {{
            put("Mobile Accessories under ₹500", Arrays.asList("Upto 20% Off Mobile \n Cases & Covers on \n Amazon", "Upto 20% Off Mobile \n Cases & Covers on \n Snapdeal"));
        }});

        add(new HashMap<String, List<String>>() {{
            put("Home & Kitchen", Arrays.asList("Upto 50% Off Pressure \n Cooker on Amazon", "Upto 50% Off Pressure \n Cooker on Snapdeal"));
        }});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("");
        toolbarLogo.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        List<HomePageListViewDto> list = new ArrayList<>();
        HomePageListViewDto data = new HomePageListViewDto();
        data.setType(HomePageListViewType.VIEW_PAGER);
        list.add(data);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        HomeScreenRecyclerAdapter adapter = new HomeScreenRecyclerAdapter(this, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        for (HashMap<String, List<String>> title: gridList) {
            HomePageListViewDto homePageListViewDto = new HomePageListViewDto();
            List<String> titles = new ArrayList<>();
            Set<String> key = title.keySet();
            List<String> keys = new ArrayList<>(key);
            titles.add(keys.get(0));
            int itemsCount = title.get(keys.get(0)).size();
            if(itemsCount == 2) {
                homePageListViewDto.setType(HomePageListViewType.GRID_VIEW);
                titles.addAll(title.get(keys.get(0)));
                homePageListViewDto.setTitle(titles);
            } else {
                homePageListViewDto.setType(HomePageListViewType.SINGLE_VIEW);
                titles.addAll(title.get(keys.get(0)));
                homePageListViewDto.setTitle(titles);
            }
            adapter.add(homePageListViewDto);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onItemClickListener(HomePageListViewDto data) {

    }
}
