package com.example.user.kumat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.kumat.Database.QuickButtonDatabase;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 27/07/2017.
 */

public class ListViewWidgetService extends RemoteViewsService {

    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context mContext;

        private ArrayList<QuickButtonDatabase> records;



        public ListViewRemoteViewsFactory(Context context, Intent intent) {

            mContext = context;

        }

        public void onCreate() {

            // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,

            // for example downloading or creating content etc, should be deferred to onDataSetChanged()

            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

            records=new ArrayList<>();

        }

        // Given the position (index) of a WidgetItem in the array, use the item's text value in

        // combination with the app widget item XML file to construct a RemoteViews object.

        public RemoteViews getViewAt(int position) {

            // position will always range from 0 to getCount() - 1.

            // Construct a RemoteViews item based on the app widget item XML file, and set the

            // text based on the position.

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

            // feed row

            String data = "";

            if (records.size()!=0){

                data=records.get(position).getNama();

                String harga = editRupiah(String.valueOf(records.get(position).getHarga()));

                int icon = records.get(position).getIcon();

                rv.setTextViewText(R.id.item_harga,harga);

                rv.setTextViewText(R.id.item, data);

                rv.setImageViewResource(R.id.widget_icon, icon);

            }


            // end feed row

            // Next, set a fill-intent, which will be used to fill in the pending intent template

            // that is set on the collection view in ListViewWidgetProvider.

            Bundle extras = new Bundle();

            extras.putInt(MyWidgetProvider.EXTRA_ITEM, records.get(position).getId());

            Intent fillInIntent = new Intent();

            fillInIntent.putExtra("homescreen_meeting",data);

            fillInIntent.putExtras(extras);

            // Make it possible to distinguish the individual on-click



            // action of a given item

            rv.setOnClickFillInIntent(R.id.background_widget, fillInIntent);

            // Return the RemoteViews object.

            return rv;

        }

        public int getCount(){

            Log.e("size=",records.size()+"");

            return records.size();

        }

        public void onDataSetChanged(){

            // Fetching JSON data from server and add them to records arraylist

            records = new ArrayList<>();

            List<QuickButtonDatabase> quickSearch = new Select()
                    .from(QuickButtonDatabase.class)
                    .queryList();
            for (QuickButtonDatabase list : quickSearch){
                records.add(list);
            }
            Collections.reverse(records);

        }

        public int getViewTypeCount(){

            return 1;

        }

        public long getItemId(int position) {

            return position;

        }

        public void onDestroy(){

            records.clear();

        }

        public boolean hasStableIds() {

            return true;

        }

        public RemoteViews getLoadingView() {

            return null;

        }

    }

    private String editRupiah(String rupiah){
        String edtRuiah = rupiah;
        int length = edtRuiah.length();

        //100000
        while (length>3){
            edtRuiah = edtRuiah.substring(0,(length-3))+"."+edtRuiah.substring((length-3),edtRuiah.length());
            length = length-3;
        }


        return edtRuiah;
    }

}
