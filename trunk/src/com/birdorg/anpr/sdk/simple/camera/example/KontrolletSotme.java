package com.birdorg.anpr.sdk.simple.camera.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static com.birdorg.anpr.sdk.simple.camera.example.R.drawable.cam;
import static com.birdorg.anpr.sdk.simple.camera.example.R.drawable.cap;

/**
 * Created by john on 7/22/2014.
 */
public class KontrolletSotme extends ListActivity{
    private ProgressDialog pDialog;
    private static final String TARGA_KONTROLLET_SOTME_URL = "http://www.comport.first.al/anpr/kontrollet_sotme.php";


    private static final String TAG_DATA = "data";
    private static final String TAG_PAJISJE_ID= "pajisje_id";
    private static final String TAG_USER_ID= "user_id";
    private static final String TAG_CAPTURE_ID= "capture_image";

    private static final String TAG_TARGA= "targa";
    private static final String TAG_POSTS = "posts";


    private JSONArray mPlates = null;

    // manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mPlatesList;

    ItemKontrollo item;
    private Vector<ItemKontrollo> VECITEM = new Vector<ItemKontrollo>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading the comments via AsyncTask
        new LoadComments().execute();
    }

    public void updateJSONdata() {
        mPlatesList = new ArrayList<HashMap<String, String>>();
        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFromUrl(TARGA_KONTROLLET_SOTME_URL);
        item  = new ItemKontrollo();
        try {
            mPlates = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mPlates.length(); i++) {
                JSONObject c = mPlates.getJSONObject(i);

                // gets the content of each tag
                String targa = c.getString(TAG_TARGA);
                String data = c.getString(TAG_DATA);
                String pajisje_id = c.getString(TAG_PAJISJE_ID);

                String user_id = c.getString(TAG_USER_ID);
                String capture_image = c.getString(TAG_CAPTURE_ID);

                item.setTarga(targa);
                item.setData(data);
                item.setUser(user_id);
                item.setImgpath(capture_image);

                VECITEM.add(item);
                item = new ItemKontrollo();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateKontrollo() {

        ListView list;

        Set<String> unionSet = new HashSet<String>();
        for (HashMap<String, String> hashMap : mPlatesList) {
            for (String key : hashMap.keySet())
                if (key.equals(TAG_TARGA))
                    unionSet.add(hashMap.get(key));
        }


        setListAdapter(new KontrolletSotmeAdapter(this, VECITEM));
    }


public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KontrolletSotme.this);
            pDialog.setMessage("Targat po ngarkohen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            updateKontrollo();

        }
    }

}
