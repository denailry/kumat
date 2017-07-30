package com.example.user.kumat.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Adapter.WishIconAdapter;
import com.example.user.kumat.Adapter.WishlistAdapter;
import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.Listener.WishIconListener;
import com.example.user.kumat.R;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_DELETE;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_NAMA;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_TABUNGAN_MINUS;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_TABUNGAN_PLUS;
import static com.example.user.kumat.Fragment.WishlistDetailFragment.DATA_TARGET;

/**
 * Created by denail on 17/07/13.
 */

public class WishlistChangeImageFragment extends Fragment implements WishIconListener {

    private RecyclerView rvIcon;
    private WishIconAdapter adapter;
    private ImageGetListener imageGetListener;
    private Bitmap image;

    public WishlistChangeImageFragment(ImageGetListener listener) {
        this.imageGetListener = listener;
    }

    public interface ImageGetListener {
        void onGet(Bitmap image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_change_image, container, false);

        ArrayList<Integer> dummy = new ArrayList<>();
        for(int i = 0; i <= 7; i++) {
            dummy.add(i);
        }

        rvIcon = (RecyclerView) view.findViewById(R.id.rv_icon);
        adapter = new WishIconAdapter(dummy, this, getContext());
        rvIcon.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvIcon.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onItemClick(int position) {
        switch(position) {
            case 0:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
                break;
            case 1:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_book);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
            case 2:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_laptop);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
            case 3:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_makanan);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
            case 4:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_mobil);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
            case 5:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_motor);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
            case 6:
                image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlist_ic_tourism);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)  {
            Uri fullPhotoUri = data.getData();

            try {
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        fullPhotoUri);
                image = WishlistDatabase.fitImageSize(image);
                imageGetListener.onGet(image);
                getFragmentManager().popBackStack();
            } catch (IOException e) {
                Log.d(getClass().getSimpleName(), "Failed to get image.");
            }
        }
    }
}
