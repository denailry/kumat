package com.example.user.kumat.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.kumat.Database.ProfilDatabase;
import com.example.user.kumat.Database.ProfilDatabase_Table;
import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.Database.WishlistDatabase_Table;
import com.example.user.kumat.KoinGetDialog;
import com.example.user.kumat.Listener.OnFragmentDestroyListener;
import com.example.user.kumat.Listener.OnItemSaveListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by denail on 17/07/13.
 */

public class WishlistDetailFragment extends Fragment {

    public final static int DATA_DELETE = 0;
    public final static int DATA_NAMA = 1;
    public final static int DATA_TARGET = 2;
    public final static int DATA_TABUNGAN_PLUS = 3;
    public final static int DATA_TABUNGAN_MINUS = 4;
    public final static int DATA_IMAGE = 5;
    public final static int DATA_SAVE = 6;
    public final static int DATA_FINISH = 7;

    private final int REQUEST_IMAGE_GET = 1;

    private RelativeLayout cntTarget, cntPersentase;
    private RelativeLayout relativeLayout;
    private ImageView ivIkon;
    private TextView tvNama, tvTarget, tvTabungan, tvPersentase;
    private Button btnMinus, btnPlus, btnDelete, btnSave, btnFinish;
    private WishlistDatabase item;
    private OnItemSaveListener onItemSaveListener;
    private OnFragmentDestroyListener onFragmentDestroyListener;
    private Boolean isDataChanged;
    private Bitmap image;

    public WishlistDetailFragment(WishlistDatabase item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_detail, container, false);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_fragment_wishlist_detail);
        cntTarget = (RelativeLayout) view.findViewById(R.id.cnt_target);
        cntPersentase = (RelativeLayout) view.findViewById(R.id.cnt_persentase);
        ivIkon = (ImageView) view.findViewById(R.id.iv_ikon);
        tvNama = (TextView) view.findViewById(R.id.tv_nama);
        tvTarget = (TextView) view.findViewById(R.id.tv_target);
        tvTabungan = (TextView) view.findViewById(R.id.tv_tabungan);
        tvPersentase = (TextView) view.findViewById(R.id.tv_persentase) ;
        btnMinus = (Button) view.findViewById(R.id.btn_minus);
        btnPlus = (Button) view.findViewById(R.id.btn_plus);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnFinish = (Button) view. findViewById(R.id.btn_finish);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isDataChanged = false;

        refreshItem(item);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        btnDelete.setOnClickListener(new ClickListener(DATA_DELETE));
        tvNama.setOnClickListener(new ClickListener(DATA_NAMA));
        cntTarget.setOnClickListener(new ClickListener(DATA_TARGET));
        btnMinus.setOnClickListener(new ClickListener(DATA_TABUNGAN_MINUS));
        btnPlus.setOnClickListener(new ClickListener(DATA_TABUNGAN_PLUS));
        btnSave.setOnClickListener(new ClickListener(DATA_SAVE));
        ivIkon.setOnClickListener(new ClickListener(DATA_IMAGE));
        btnFinish.setOnClickListener(new ClickListener(DATA_FINISH));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)  {
            Uri fullPhotoUri = data.getData();

            try {
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        fullPhotoUri);
            } catch (IOException e) {
                Log.d(getClass().getSimpleName(), "Failed to get image.");
            }

            ivIkon.setImageBitmap(image);
        }
    }

    @Override
    public void onDestroy() {
        if(isDataChanged) {
            onItemSaveListener.onItemSave();
        }
        onFragmentDestroyListener.onFragmentDestroy();
        super.onDestroy();
    }

    private void refreshItem(WishlistDatabase item) {
        this.item = item;

        tvNama.setText(item.getNama());
        if(item.getTarget() == 0 || item.getTabungan() < item.getTarget()) {
            Log.d("TEST", "GONE");
            btnFinish.setVisibility(View.GONE);
        } else {
            Log.d("TEST", "VIS");
            btnFinish.setVisibility(View.VISIBLE);
        }

        if(item.getTarget() != 0) {
            tvTarget.setText(String.valueOf(item.getTarget()));
            tvPersentase.setText(item.getPersentase());
        }

        tvTabungan.setText(item.getTabungan().toString());
        if(item.getId() == 1) {
            btnDelete.setVisibility(View.GONE);
            cntTarget.setVisibility(View.GONE);
            cntPersentase.setVisibility(View.GONE);
            ivIkon.setImageResource(R.drawable.wishlist_detail_tabunganku);
        } else {
            Bitmap image = item.getImage();
            if(image == null) {
                ivIkon.setImageResource(R.drawable.icon_add);
            } else {
                ivIkon.setImageBitmap(image);
            }
        }
    }

    public void setOnItemSaveListener(OnItemSaveListener onItemSaveListener) {
        this.onItemSaveListener = onItemSaveListener;
    }
    public void setOnFragmentDestroyListener(OnFragmentDestroyListener onFragmentDestroyListener) {
        this.onFragmentDestroyListener = onFragmentDestroyListener;
    }

    private class ClickListener implements View.OnClickListener {
        private int dataId;

        public ClickListener(int dataId) {
            this.dataId = dataId;
        }

        @Override
        public void onClick(View view) {
            if (dataId == DATA_DELETE) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                WishlistChangeFragment fragment = new WishlistChangeFragment(item, dataId);
                fragment.setOnItemSave(new WishlistChangeFragment.OnItemSave() {
                    @Override
                    public void onSave(WishlistDatabase item) {
                        item.commit();
                        item.delete();
                        isDataChanged = true;
                        getFragmentManager().popBackStack();
                    }
                });
                ft.add(R.id.rl_fragment_wishlist_detail, fragment);
                ft.addToBackStack(null);
                ft.commit();
            } else if (dataId == DATA_IMAGE) {
                if(item.getId() != 1) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    }
                    isDataChanged = true;
                }
            } else if (dataId == DATA_SAVE) {
                if (image != null) {
                    item.setImage(image);
                }
                item.commit();
                isDataChanged = true;
                getFragmentManager().popBackStack();
            } else if (dataId == DATA_FINISH){
                if(item.getTabungan() != item.getTarget()) {
                    int difference = item.getTabungan() - item.getTarget();
                    WishlistDatabase tabunganku = new Select()
                            .from(WishlistDatabase.class)
                            .where(WishlistDatabase_Table.id.eq(1L))
                            .querySingle();
                    tabunganku.addTabungan(difference, false);
                    tabunganku.save();
                }
                ProfilDatabase profile = new Select()
                        .from(ProfilDatabase.class)
                        .where(ProfilDatabase_Table.Username.eq(((MainActivity) getActivity()).usernameAktif))
                        .querySingle();
                if(profile != null) {
                    float koinfromTarget = item.getTarget()/25000;
                    int newKoin = profile.getKoin() + (int) koinfromTarget;
                    profile.setKoin(newKoin);
                    profile.save();
                }
                item.delete();
                isDataChanged = true;
                ((MainActivity) getActivity()).showKoinDialog();
                getFragmentManager().popBackStack();
            } else {
                if(dataId != DATA_NAMA || item.getId() != 1) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    WishlistChangeFragment fragment = new WishlistChangeFragment(item, dataId);
                    fragment.setOnItemSave(new WishlistChangeFragment.OnItemSave() {
                        @Override
                        public void onSave(WishlistDatabase item) {
                            refreshItem(item);
                        }
                    });
                    ft.add(R.id.rl_fragment_wishlist_detail, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        }
    }
}