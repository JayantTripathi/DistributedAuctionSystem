package com.example.superuser.distributedauctionsystemreal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.io.ByteStreams;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Blob;


public class post_an_ad extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_an_ad);
        Spinner type_ItemCondition=(Spinner)findViewById(R.id.type_Item_Condition);
        ArrayAdapter Adapter_type_ItemCondition= ArrayAdapter.createFromResource(this, R.array.Item_Condition_options,android.R.layout.simple_spinner_dropdown_item);
        type_ItemCondition.setAdapter(Adapter_type_ItemCondition);
        type_ItemCondition.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ChooseImgBtnClick(View V)
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
            } catch (Exception E) {
                E.printStackTrace();
            }
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.img_vw_upload);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Bundle InstanceState=null;
          //  InstanceState.putString("FileLocation",picturePath);
           // onSaveInstanceState(InstanceState);


        }
    }


    public void SubmitAdvertisement(View V)
    {
        URI uri = null;
        URL send_img_URL=null;
        EditText product_name_obj=(EditText)findViewById(R.id.text_Product_name);
        EditText txt_day_limit=(EditText)findViewById(R.id.txt_day_limit);
        EditText txt_seller_price=(EditText)findViewById(R.id.txt_seller_price);
        Spinner type_Item_Condition=(Spinner)findViewById(R.id.type_Item_Condition);
        String anti=picturePath;
        try {
            uri = new URI("http", "210373d.ngrok.com", "/DistributedAuctionSystem/SaveInformation",
                    "ProductName=" + product_name_obj.getText() + "&DayLimit=" + txt_day_limit.getText() + "&SellerPrice=" + txt_seller_price.getText() + "&ItemCondition="+ type_Item_Condition.getSelectedItem() , null);


        } catch (URISyntaxException E) {
            E.printStackTrace();
        }

        GetStringFromUrl url = new GetStringFromUrl(getBaseContext(),anti);
        url.execute(uri.toASCIIString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_an_ad, menu);
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
    class GetStringFromUrl extends AsyncTask<String, Void, String> {
        String result;
        String UniversityID = null;
        ProgressDialog dialog;
        Context con;
        File imgfile=null;
        String ProductName=null;
        JSONObject json_obj=null;
        InputStream in_image=null;
        String ImageLocation=null;
        public GetStringFromUrl(Context baseContext, String Location) {
            con = baseContext;
            EditText product_name_obj=(EditText)findViewById(R.id.text_Product_name);
            String ProductName=product_name_obj.getText().toString();
            ImageLocation=Location;
            imgfile= new File(ImageLocation);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream in=null;
            URL url_image=null;
            try {
                url_image = new URL(params[0]);
            }
            catch(MalformedURLException M_exp)
            {
                M_exp.printStackTrace();
            }


                try {

                    HttpClient httpclient=new DefaultHttpClient();
                    HttpPost http_post=new HttpPost(url_image.toString());

                    MultipartEntity entity1=new MultipartEntity();
                    InputStreamEntity se=new InputStreamEntity(new FileInputStream(imgfile),-1);


                    http_post.setEntity(se);
                    HttpResponse response= httpclient.execute(http_post);



                }
                catch(Exception E)
                {
                    E.printStackTrace();
                }




            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try
            {
                Toast.makeText(getBaseContext(),"Request has been Submitted",Toast.LENGTH_LONG).show();
                EditText product_name_obj=(EditText)findViewById(R.id.text_Product_name);
                product_name_obj.setText("");
                EditText txt_day_limit=(EditText)findViewById(R.id.txt_day_limit);
                txt_day_limit.setText("");
                EditText txt_seller_price=(EditText)findViewById(R.id.txt_seller_price);
                txt_seller_price.setText("");
                ImageView imageView = (ImageView) findViewById(R.id.img_vw_upload);
                imageView.setImageBitmap(null);
            }
            catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
}
