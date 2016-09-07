package com.example.surmeet.statistics;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity2 extends AppCompatActivity
{

    Spinner states_spin,cat_spin;
    TextView user,logout;
    Button view,update,button;
    EditText number;
    Intent in;
    ArrayList<String> xlist;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        states_spin=(Spinner)findViewById(R.id.spinner);
        cat_spin=(Spinner)findViewById(R.id.spinner2);
        user=(TextView)findViewById(R.id.textView6);
        view=(Button)findViewById(R.id.view);
        update=(Button)findViewById(R.id.update);
        button=(Button)findViewById(R.id.button);
        number=(EditText)findViewById(R.id.number);
        logout=(TextView)findViewById(R.id.textView8);

        cat_spin.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        number.setVisibility(View.INVISIBLE);

        firebaseAuth=FirebaseAuth.getInstance();

        Snackbar.make(view,"Logged in as:"+firebaseAuth.getCurrentUser().getEmail().toString(),Snackbar.LENGTH_SHORT).show();

        if(firebaseAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(this,MainActivity.class));
        }

        firebaseUser=firebaseAuth.getCurrentUser();

        user.setText(firebaseUser.getEmail());
        Log.i("USER", firebaseUser.getEmail());

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setMessage("You sure,bitch?").setTitle("Gonna Log the Fuck out");
                builder.setPositiveButton("Yes, Bitch", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(MainActivity2.this,"Logging out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Log.i("LOG OUT","log out on logout");
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }
                });
                builder.setNegativeButton("Hell no!!!", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        in=new Intent(this,MainActivity.class);

        ArrayList<String> states= new ArrayList<String>();
        states.add("Assam");
        states.add("Bihar");
        states.add("Chandigarh");
        states.add("Chhattisgarh");
        states.add("Delhi");
        states.add("Goa");
        states.add("Gujarat");
        states.add("Haryana");
        states.add("Jharkhand");
        states.add("Karnataka");
        states.add("Lakshadweep");
        states.add("Kerala");
        states.add("Maharashtra");
        states.add("Manipur");
        states.add("Meghalaya");
        states.add("Mizoram");
        states.add("Nagaland");
        states.add("Orissa");
        states.add("Puducherry");
        states.add("Punjab");
        states.add("Rajasthan");
        states.add("Sikkim");
        states.add("Tripura");
        states.add("Uttarakhand");
        states.add("INDIA");

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,states);
        states_spin.setAdapter(dataAdapter);


        states_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                in.putExtra("state",states_spin.getSelectedItem().toString());
                in.setFlags(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        view.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                startActivity(in);
            }
    });

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                view.setVisibility(View.INVISIBLE);
                update.setVisibility(View.INVISIBLE);
                cat_spin.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
        });

        ArrayList<String> categories=new ArrayList<String>();
        categories.add("FullTimeTeachersMale");
        categories.add("FullTimeTeachersFemale");
        categories.add("ParaContractTeachersMale");
        categories.add("ParaContractTeachersFemale");

        ArrayAdapter<String> cat_data=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        cat_spin.setAdapter(cat_data);

        cat_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                in.putExtra("cat",cat_spin.getSelectedItem().toString());
                Log.i("ehllo",cat_spin.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                in.putExtra("data",number.getText().toString());
                in.setFlags(1);
                Log.i("data",number.getText().toString());
                Log.i("in string data",in.getStringExtra("data").toString());
                startActivity(in);

                number.setText(null);
                view.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                cat_spin.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                number.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"Logging out",Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Log.i("LOG OUT","log out on back press");
        //finish();
    }
}
