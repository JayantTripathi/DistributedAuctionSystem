package com.example.superuser.distributedauctionsystemreal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void LoginBtnClick(View V) throws MalformedURLException,URISyntaxException
    {
        String Username = ((EditText) findViewById(R.id.edtTxtStdID)).getText().toString();
        String Password = ((EditText) findViewById(R.id.edtTxtPass)).getText().toString();

        URI uri = null;
        try {
            uri = new URI("http", "210373d.ngrok.com", "/DistributedAuctionSystem/Login",
                    "Username=" + Username + "&Password=" + Password , null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        GetStringFromUrl url = new GetStringFromUrl(getBaseContext(),Username);
        url.execute(uri.toASCIIString());

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
}

class GetStringFromUrl extends AsyncTask<String, Void, String> {
    String result;
    String UniversityID = null;
    ProgressDialog dialog;
    Context con;

    public GetStringFromUrl(Context baseContext, String StudentID) {
        con = baseContext;
        UniversityID = StudentID;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {


        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

            InputStream is = buf.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            result = total.toString();
        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
            result = "null";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONObject obj_data = null;
        String CustomerID = null;
        if (result.equals("failed")) {
            Toast.makeText(con,"User Does Not Exist",Toast.LENGTH_LONG).show();

        } else {


            try {
                obj_data = new JSONObject(result);
                CustomerID = (obj_data.get("CustomerID")).toString();
            } catch (JSONException E) {
                E.printStackTrace();
            }
            Intent intent = new Intent(con, activity_after_login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("CustomerID", CustomerID);
            con.startActivity(intent);

        }
    }
}