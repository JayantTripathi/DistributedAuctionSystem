package com.example.superuser.distributedauctionsystemreal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Iterables;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;



public class activity_after_login extends ActionBarActivity {

    String Calltype=null;
    String CustomerID=null;
    String SellerPrice=null;
    String result;
    String UniversityID = null;
    ProgressDialog dialog;
    Context con;
    JSONObject data_obj=null;
    String ProductID="2";
    String ProductName=null;
    String TimeLimit=null;
    String ItemCondition=null;
    String DateSubmitted=null;
    String BidAmount=null;
    String TotalBids=null;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_after_login);
        String urlPath = "";
        URI uri = null;
        try {
            uri = new URI("http", "210373d.ngrok.com", "/AuctionSystem/Useless", null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            CustomerID=extras.getString("CustomerID");
        }
      //  handler = new Handler(Looper.getMainLooper());

/* Update  */
       // Runnable timer = new Runnable() {
          //  @Override
          //  public void run() {
                Calltype="GetInformationAlone";
                GetStringFromUrl url = new GetStringFromUrl(getBaseContext());
                url.execute();
         //       handler.postDelayed(this, 10000);
         //   }
      //  };
       // handler.post(timer);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_after_login, menu);
        return true;
    }

    public void PlaceyourBid(View V) throws URISyntaxException
    {
        Calltype="SaveBidData";

        GetStringFromUrl url = new GetStringFromUrl(getBaseContext());
        url.execute();

    }

    public void GetInformation() throws IOException
    {
        InputStream in=null;
        URL url_image=null;


        FileOutputStream stream=null;
        try{
            // Get data related to the product which is being displayed
            URI uri_get_information = null;

            uri_get_information = new URI("http", "210373d.ngrok.com", "/DistributedAuctionSystem/GetInformation" ,
                         "ProductID="+ProductID,null);
            HttpClient httpclient=new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri_get_information);

            HttpResponse response = httpclient.execute(httpGet);
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

            if(result != "failed") {
                data_obj=new JSONObject(result);
                ProductID= data_obj.get("ProductID").toString();
                ProductName=data_obj.get("ProductName").toString();
                SellerPrice=data_obj.get("SellerPrice").toString();
                TimeLimit=data_obj.get("TimeLimit").toString();
                ItemCondition=data_obj.get("ItemCondition").toString();
                DateSubmitted=data_obj.get("DateSubmitted").toString();
                BidAmount=data_obj.get("BidAmount").toString();
                TotalBids=data_obj.get("TotalBids").toString();
            }




            // Image recreation in the android filesystem


            String filename=null;
            filename=ProductName +".jpg";
            File newpath=new File("/sdcard/AuctionSystem/");
            newpath.mkdirs();
            File newpath1=new File("/sdcard/AuctionSystem/ProductImages/");
            newpath1.mkdirs();
            File filename_obj=new File(newpath1,filename);
            if(filename_obj.exists())
            {

            }
            else
            {
                URI uri = new URI("http", "210373d.ngrok.com", "/DistributedAuctionSystem/Useless",
                        "ProductID="+ProductID+"&ProductName="+ProductName,null);
                url_image = new URL(uri.toASCIIString());

                HttpURLConnection Conn=(HttpURLConnection)url_image.openConnection();
                in=Conn.getInputStream();
                int bufferSize = 1024;
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                byte[] buffer = new byte[bufferSize];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }

                stream = new FileOutputStream(filename_obj);
                stream.write(byteBuffer.toByteArray());
            }


        }
        catch (Exception Ex_IO){
            Ex_IO.printStackTrace();
        }


    }
    public void SaveBid() throws URISyntaxException,IOException
    {
        URI uri_get_information = null;

        TextView product_name_obj=(TextView)findViewById(R.id.txt_view_product_name);
        URI uri = new URI("http", "210373d.ngrok.com", "/DistributedAuctionSystem/SaveBid",
                "ProductName=" + product_name_obj.getText()+ "&CustomerID=" + CustomerID+"&SellerPrice=" + SellerPrice , null);
        HttpClient httpclient=new DefaultHttpClient();
        HttpPost http_post=new HttpPost(uri.toASCIIString());
        HttpResponse response= httpclient.execute(http_post);



    }

    public void PostanAd(View V)
    {
        Intent intent = new Intent(getBaseContext(), post_an_ad.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    public void RefreshData()
    {


       /* handler = new Handler(Looper.getMainLooper());

*//* Update  *//*
        Runnable timer = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(con, activity_after_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CustomerID", CustomerID);
                con.startActivity(intent);
                handler.postDelayed(this,10000);
            }
        };
        handler.post(timer);*/
    }

    public void NextProduct(View V)
    {
        int id=Integer.parseInt(ProductID);
        id=id+1;
        ProductID=Integer.toString(id);
        Calltype="GetInformationAlone";
        GetStringFromUrl url = new GetStringFromUrl(getBaseContext());
        url.execute();
    }

    public void PreviousProduct(View V)
    {
        int id = Integer.parseInt(ProductID);
        id=id-1;
        ProductID=Integer.toString(id);
        Calltype="GetInformationAlone";
        GetStringFromUrl url = new GetStringFromUrl(getBaseContext());
        url.execute();
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

    class GetStringFromUrl extends AsyncTask<String, Void, String> {
        public GetStringFromUrl(Context baseContext) {
            con = baseContext;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
          if(Calltype=="SaveBidData"){
              try {
                  try {
                      SaveBid();
                      GetInformation();
                  }
                  catch(URISyntaxException E)
                  {
                      E.printStackTrace();
                  }
              }
              catch(IOException E)
              {
                  E.printStackTrace();
              }
          }
            else
          {
              try {
                  GetInformation();
              }
              catch(IOException E)
              {
                  E.printStackTrace();
              }
          }
            return null;
        }




        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

                try {
                    TextView Productname_txt = (TextView) findViewById(R.id.txt_view_product_name);
                    Productname_txt.setText(ProductName);
                    TextView ItemCondition_txt = (TextView) findViewById(R.id.txt_vw_itemCondition);
                    ItemCondition_txt.setText(ItemCondition);
                    TextView Timeleft_txt = (TextView) findViewById(R.id.txt_vw_timeleft);
                    Timeleft_txt.setText(TimeLimit + " Days");
                    TextView Currentbid_txt = (TextView) findViewById(R.id.txt_vw_currentbid);
                    TextView totalbids_txt = (TextView) findViewById(R.id.txt_vw_Totalbids);
                    if (BidAmount != "null") {
                        Currentbid_txt.setText(BidAmount);
                    } else {
                        Currentbid_txt.setText("No Bids Placed");
                    }
                    totalbids_txt.setText(TotalBids);
                    ImageView img_product_image = (ImageView) findViewById(R.id.image_vw_Product_image);
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/AuctionSystem/ProductImages/" + ProductName + ".jpg");
                    img_product_image.setImageBitmap(bitmap);


                } catch (Exception E) {
                    E.printStackTrace();
                }

        }
    }
}
