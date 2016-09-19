package com.example.surmeet.statistics;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
public class MainActivity extends AppCompatActivity //implements AdapterView.OnItemSelectedListener
{
    Fragment fragment;
    String item;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    TextView textView;
    Bundle bundle=new Bundle();
    mySyncTask syncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        Intent out=getIntent();

        syncTask=new mySyncTask();
        Log.i("FLAG",Integer.toString(out.getFlags()));
        if(out.getFlags()==0)
            syncTask.execute(out.getStringExtra("state"));
        else
            syncTask.execute(out.getStringExtra("state"),out.getStringExtra("cat"),out.getStringExtra("data"));

        ArrayList<String> xlist=new ArrayList<>();
        try
        {
            xlist = syncTask.get();
            //if(xlist!=null)
                Log.i("xlist",xlist.toString());
            //else
              //  Toast.makeText(this,"Error,xlist is empty",Toast.LENGTH_LONG);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();

        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        bundle.putStringArrayList("list",xlist);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }
    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        try {
                            selectDrawerItem(menuItem);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) throws ExecutionException, InterruptedException
    {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId())
        {
            case R.id.nav_first_fragment:
                fragmentClass = BarFragment.class;
                textView.setText("Bar Chart");
                break;
            case R.id.nav_second_fragment:
                fragmentClass = PieFragment.class;
                textView.setText("Pie Chart");
                break;
            case R.id.nav_third_fragment:
                fragmentClass = LineFragment.class;
                textView.setText("Line Chart");
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = ScatterFragment.class;
                textView.setText("Scatter Chart");
                break;
            case R.id.nav_five_fragment:
                fragmentClass = RadarFragment.class;
                textView.setText("Radar Chart");
                break;
            default:
                fragmentClass = BarFragment.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.i("bundle",bundle.toString());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.placeholder, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
}
class mySyncTask extends AsyncTask<String, Void, ArrayList>
{
    @Override
    protected ArrayList doInBackground(String... params)
    {

        String state=params[0];
        String index=null,data=null;
        if(params.length>1)
        {
            index=params[1];
            data=params[2];
        }

//        Log.i("state: ",state);
        Log.i("BLAH","inside class");
        String API_KEY = "aae2cc6508d1ef7ab8d77423b69b452d";
        try
        {
            //ALL DATA FROM API KEY

            Log.i("BLAH","inside try catch");
            URL url = new URL("https://data.gov.in/api/datastore/resource.json?resource_id=3ad652a3-df6d-45b4-82e3-dca8b2cd6172&api-key="+API_KEY+"&filters[STATEUT]="+state);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try
            {
                Log.i("BLAH","inside second try catch");

                ArrayList<String> list=new ArrayList<>();
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                JSONObject jsonObject=new JSONObject(reader.readLine());
                JSONArray jsonArray=jsonObject.getJSONArray("records");
                Log.i("BLAH",jsonArray.toString());

                if (index!=null)
                {
                    jsonArray.getJSONObject(0).putOpt(index, data);
                }

                Log.i("blah",jsonArray.toString());

                list.add(jsonArray.getJSONObject(0).get("FullTimeTeachersMale").toString());
                list.add(jsonArray.getJSONObject(0).get("FullTimeTeachersFemale").toString());
                list.add(jsonArray.getJSONObject(0).get("ParaContractTeachersMale").toString());
                list.add(jsonArray.getJSONObject(0).get("ParaContractTeachersFemale").toString());

                Log.i("Array list in MySyncTa",list.toString());

                reader.close();
                return list;
            }
            finally
            {
                urlConnection.disconnect();
            }
        }
        catch(Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}