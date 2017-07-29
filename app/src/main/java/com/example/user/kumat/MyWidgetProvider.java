package com.example.user.kumat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.QuickButtonDatabase;
import com.example.user.kumat.Database.QuickButtonDatabase_Table;
import com.example.user.kumat.Database.SaldoDatabase;
import com.example.user.kumat.Database.SaldoDatabase_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.GregorianCalendar;

/**
 * Created by User on 27/07/2017.
 */

public class MyWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_MEETING_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String TOAST = "KIRIM";

    public static final String EXTRA_ITEM = ".EXTRA_ITEM";


    public void onReceive(Context context, Intent intent) {

        Log.d("cektoast", "onReceive: "+intent.getAction());

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_MEETING_ACTION)) {

            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context,MyWidgetProvider.class));

            Log.e("received", intent.getAction());

            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);


        }else if (intent.getAction().equals(TOAST)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int idQuick = intent.getIntExtra(EXTRA_ITEM, 0);

            Log.e("received", intent.getAction());

            EksekusiQuickButton(context,idQuick);


        }

        super.onReceive(context, intent);

    }

    private void EksekusiQuickButton(Context context,int idQuick) {

        QuickButtonDatabase quick = new Select()
                .from(QuickButtonDatabase.class)
                .where(QuickButtonDatabase_Table.id.is(idQuick))
                .querySingle();

        SaldoDatabase dataSaldo = new Select()
                .from(SaldoDatabase.class)
                .where(SaldoDatabase_Table.id.is(1))
                .querySingle();

        int saldo = dataSaldo.getSaldo();
        int hargaBarang = quick.getHarga();

        if (hargaBarang>saldo){
            Toast.makeText(context, "Nominal melebihi jumlah saldo", Toast.LENGTH_SHORT).show();
        }else{

            java.util.Calendar calendar = new GregorianCalendar();
            int day = calendar.get(calendar.DAY_OF_MONTH);
            int month = calendar.get(calendar.MONTH);
            int year = calendar.get(calendar.YEAR);

            AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
            aktivitasKeuangan.setId(dataSaldo.getIndex());
            aktivitasKeuangan.setNamaBarang(quick.getNama());
            aktivitasKeuangan.setHargaBarang(hargaBarang);
            aktivitasKeuangan.setTipe(0);
            aktivitasKeuangan.setTanggal(day);
            aktivitasKeuangan.setBulan(month+1);
            aktivitasKeuangan.setTahun(year);
            aktivitasKeuangan.save();

            saldo = saldo - hargaBarang;
            dataSaldo.setIndex(dataSaldo.getIndex()+1);
            dataSaldo.setSaldo(saldo);

            dataSaldo.save();

            Toast.makeText(context, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show();

        }

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,

                         int[] appWidgetIds){

        // update each of the app widgets with the remote adapter

        for (int i = 0; i < appWidgetIds.length; ++i){

            // Set up the intent that starts the ListViewService, which will

            // provide the views for this collection.

            Intent intent = new Intent(context, ListViewWidgetService.class);

            // Add the app widget ID to the intent extras.

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set up the RemoteViews object to use a RemoteViews adapter.

            // This adapter connects

            // to a RemoteViewsService  through the specified intent.

            // This is how you populate the data.

            rv.setRemoteAdapter(appWidgetIds[i], R.id.lv_widget, intent);

            // Trigger listview item click

            Intent startActivityIntent = new Intent(context,MyWidgetProvider.class);

            startActivityIntent.setAction(MyWidgetProvider.TOAST);
            startActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            Log.d("cektoast", "onUpdate: "+MyWidgetProvider.TOAST);

            PendingIntent startActivityPendingIntent = PendingIntent.getBroadcast(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.lv_widget, startActivityPendingIntent);

            // The empty view is displayed when the collection has no items.

            // It should be in the same layout used to instantiate the RemoteViews  object above.

            rv.setEmptyView(R.id.lv_widget, R.id.empty_view);

            //

            // Do additional processing specific to this app widget...

            //

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

}
