package com.example.android.testspoonacular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Prediction extends AppCompatActivity {
private static final String baseURL="https://api.spoonacular.com/recipes/findByIngredients?ingredients=";
private static final String KEY="&apiKey=5014df46d71648219de5bada69aa5ded";
spoonacularAdapter mAdapter;
ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prediction);
        ArrayList<Elements> data=new ArrayList<>();
        ListView list=(ListView) findViewById(R.id.prediction_list);
        pb=findViewById(R.id.progress_circular);
        TextView emptyView=(TextView) findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);
        ArrayList<String> ing_list=MainActivity.getList();
        String finalUrl=baseURL+getfinalURL(ing_list)+KEY;
        mAdapter=new spoonacularAdapter(this,data);
        list.setAdapter(mAdapter);
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        Boolean isConnecting=activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        if(isConnecting)
        {
            spoonacularAsyncTask task=new spoonacularAsyncTask();
            task.execute(finalUrl);
        }
        else
        {
            pb.setVisibility(View.GONE);
            emptyView.setText("No active internet connection found!");
        }

    }

    private String getfinalURL(ArrayList<String> ing_list) {
        String ing="";
        for(int i=0;i<ing_list.size();i++)
        {
            ing=ing+ing_list.get(i).toString()+",+";
        }
        return ing;
    }
    private class spoonacularAsyncTask extends AsyncTask<String,Void, List<Elements>>
{

    @Override
    protected List<Elements> doInBackground(String... strings) {
        List<Elements> data=new ArrayList<>();
        try {
             data=spoonacularUtils.fetchdata(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<Elements> data) {
        try {
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                pb.setVisibility(View.GONE);
            } else {
                Log.i("Error:", "in setting final data");
            }
        }
        catch (Exception e)
        {

        }

    }
}
}