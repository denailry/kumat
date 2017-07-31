package com.example.user.kumat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.PengaturanDatabase;
import com.example.user.kumat.Database.SaldoDatabase;
import com.example.user.kumat.Database.SaldoDatabase_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by User on 14/07/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = new GregorianCalendar();
        int month = calendar.get(Calendar.MONTH);
        int year  = calendar.get(Calendar.YEAR);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        int id = intent.getIntExtra("id",0);
        String nama = intent.getStringExtra("nama");
        long harga = intent.getLongExtra("harga",0);
        int tanggal = intent.getIntExtra("tanggal",0);
        boolean isAllowed = true;

        PengaturanDatabase setting = new Select()
                .from(PengaturanDatabase.class)
                .querySingle();
        if(setting != null) {
            isAllowed = setting.isNotifPOAllowed();
        }

        Log.d("TANGGAL", String.valueOf(thisDay + " " + tanggal));
        if (thisDay==tanggal){
            List<SaldoDatabase> saldoSearch = new Select()
                    .from(SaldoDatabase.class)
                    .where(SaldoDatabase_Table.id.is(1))
                    .queryList();
            SaldoDatabase saldoAktiv = saldoSearch.get(0);

            long saldo = saldoAktiv.getSaldo();
            int idxAktiv = saldoAktiv.getIndex();
            int idxQuick = saldoAktiv.getIndexQuick();

            String title = "Pengeluaran Otomatis";
            String content = nama+" telah tercatat! jangan lupa bayar kewajiban kamu.";

            if (harga>saldo){
                content=nama+" gagal dicatat karena saldo tidak mencukupi.";
            }else{
                //pengeluaran
                AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
                aktivitasKeuangan.setId(idxAktiv);
                aktivitasKeuangan.setNamaBarang(nama);
                aktivitasKeuangan.setHargaBarang(harga);
                aktivitasKeuangan.setTipe(0);
                aktivitasKeuangan.setTanggal(tanggal);
                aktivitasKeuangan.setBulan(month+1);
                aktivitasKeuangan.setTahun(year);
                aktivitasKeuangan.save();

                idxAktiv++;

                saldo = saldo-harga;
                SaldoDatabase sald = new SaldoDatabase();
                sald.setId(1);
                sald.setSaldo(saldo);
                sald.setIndex(idxAktiv);
                sald.setIndexQuick(idxQuick);
                sald.save();

            }

            if(isAllowed) {
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                Intent resultIntent = new Intent(context, MainActivity.class);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(context,id, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

                mBuilder.setSmallIcon(R.drawable.icon_kumat_kecil);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(content);
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);

                notificationManager.notify(id,mBuilder.build());
            }
        }
    }
}
