package com.example.android.testspoonacular;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.util.ArrayList;

public class spoonacularAdapter extends ArrayAdapter<Elements> {
    public spoonacularAdapter(Activity context, ArrayList<Elements> name) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, name);
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.elements, parent, false);
        }
        Elements currentElement=getItem(position);
        TextView Title=(TextView) listItemView.findViewById(R.id.title_name);
        String title=currentElement.getTitle();
        Title.setText(title);
        ListView missing_list=(ListView) listItemView.findViewById(R.id.missing_ing_list);
        ArrayList<String> miss_list=currentElement.getMissing_list();
        ArrayAdapter<String> miss_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,miss_list);
        missing_list.setAdapter(miss_adapter);
        ListView available_list=(ListView) listItemView.findViewById(R.id.required_ing_list);
        ArrayList<String> ava_list=currentElement.getAvailable_list();
        ArrayAdapter<String> available_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,ava_list);
        available_list.setAdapter(available_adapter);
        ImageView thumbnail_img=(ImageView) listItemView.findViewById(R.id.element_img);
        String img_url=currentElement.getFood_img_url();
        DownloadImageTask task=new DownloadImageTask(thumbnail_img);
        task.execute(img_url);
        return listItemView;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
