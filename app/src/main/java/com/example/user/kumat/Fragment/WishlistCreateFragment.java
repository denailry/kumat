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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.IdGen;
import com.example.user.kumat.Listener.OnFragmentDestroyListener;
import com.example.user.kumat.Listener.OnItemSaveListener;
import com.example.user.kumat.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_TABUNGAN_PLUS;

/**
 * Created by denail on 17/07/13.
 */

public class WishlistCreateFragment extends Fragment {

    private final int REQUEST_IMAGE_GET = 1;

    private RelativeLayout relativeLayout;
    private EditText etNama, etTarget;
    private TextView tvTabungan;
    private Button btnSave, btnCancel;
    private ImageView ivIkon;
    private OnItemSaveListener onItemSaveListener;
    private OnFragmentDestroyListener onFragmentDestroyListener;
    private Bitmap newImage;
    private WishlistDatabase newItem;

    public void setOnItemSaveListener(OnItemSaveListener onItemSaveListener) {
        this.onItemSaveListener = onItemSaveListener;
    }

    public void setOnFragmentDestroyListener(OnFragmentDestroyListener onFragmentDestroyListener) {
        this.onFragmentDestroyListener = onFragmentDestroyListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_create, container, false);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_fragment_wishlist_create);
        etNama = (EditText) view.findViewById(R.id.edit_nama);
        etTarget = (EditText) view.findViewById(R.id.edit_target);
        tvTabungan = (TextView) view.findViewById(R.id.tv_tabungan);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        ivIkon = (ImageView) view.findViewById(R.id.iv_ikon_tabungan);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        tvTabungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newItem = new WishlistDatabase(IdGen.generateId());
                WishlistChangeFragment fragment = new WishlistChangeFragment(newItem, DATA_TABUNGAN_PLUS);
                fragment.setOnItemSave(new WishlistChangeFragment.OnItemSave() {
                    @Override
                    public void onSave(WishlistDatabase item) {
                        tvTabungan.setText(item.getTabungan().toString());
                    }
                });
                ft.add(R.id.rl_fragment_wishlist_create, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer target = 0;
                if(etTarget.getText().toString().length() > 0) {
                    target = Integer.parseInt(etTarget.getText().toString());
                }

//                Integer tabungan = 0;
//                if(tvTabungan.getText().toString().length() > 0) {
//                    tabungan = Integer.parseInt(tvTabungan.getText().toString());
//                }

                if(newItem == null) {
                    newItem = new WishlistDatabase(IdGen.generateId());
                }
                createNewItem(etNama.getText().toString(), target);

                onItemSaveListener.onItemSave();
                getFragmentManager().popBackStack();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        ivIkon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)  {
            Uri fullPhotoUri = data.getData();

            try {
                newImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fullPhotoUri);
                newImage = WishlistDatabase.fitImageSize(newImage);
            } catch (IOException e) {
                Log.d(getClass().getSimpleName(), "Failed to get image.");
            }

            ivIkon.setImageBitmap(newImage);
        }
    }

    @Override
    public void onDestroy() {
        onFragmentDestroyListener.onFragmentDestroy();
        super.onDestroy();
    }

    public void createNewItem(String nama, int target) {
        newItem.setNama(nama);
        newItem.setTarget(target);
        newItem.setImage(newImage);
        newItem.commit();
    }
}
