package com.example.user.kumat.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.R;

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

public class WishlistChangeFragment extends Fragment {

    private final int FROM_SALDO = 1;
    private final int FROM_ELSE = 2;

    private LinearLayout llDelete, llNonDelete, llTarget, llNama;
    private RelativeLayout relativeLayout, cntChoice;
    private TextView tvTitle, tvInput, tvChoice;
    private EditText etNama, etTarget;
    private Button btnSave, btnCancel;
    private ImageButton btnFromSaldo, btnFromElse;

    private int dataId, viewId;
    private WishlistDatabase item;
    private OnItemSave listener;

    private int choice;

    public interface OnItemSave {
        void onSave(WishlistDatabase item);
    }

    public WishlistChangeFragment(WishlistDatabase item, int dataId) {
        this.item = item;
        this.dataId = dataId;
    }

    public void setOnItemSave(OnItemSave listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if(dataId == 1 || dataId == 2) {
            view = inflater.inflate(R.layout.fragment_wishlist_change_1, container, false);
            llTarget = (LinearLayout) view.findViewById(R.id.ll_target);
            llNama = (LinearLayout) view.findViewById(R.id.ll_nama);
            etTarget = (EditText) view.findViewById(R.id.edit_target);
            viewId = 1;
        } else {
            view = inflater.inflate(R.layout.fragment_wishlist_change_2, container, false);
            btnFromSaldo = (ImageButton) view.findViewById(R.id.ib_saldo);
            btnFromElse = (ImageButton) view.findViewById(R.id.ib_setoran);
            llDelete = (LinearLayout) view.findViewById(R.id.ll_delete);
            llNonDelete = (LinearLayout) view.findViewById(R.id.ll_non_delete);
            cntChoice = (RelativeLayout) view.findViewById(R.id.cnt_choice);
            viewId = 2;
        }

        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_fragment_wishlist_change_text);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvInput = (TextView) view.findViewById(R.id.tv_input);
        tvChoice = (TextView) view.findViewById(R.id.tv_choice);
        etNama = (EditText) view.findViewById(R.id.et_input);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String title = "";
        String strChoice = "";
        int inputType = 0;
        if (dataId == 1) {
            title = "Ubah Nama";
            inputType = TYPE_CLASS_TEXT;
        } else if (dataId == 2) {
            title = "Ubah Target";
            inputType = TYPE_CLASS_NUMBER;
        } else if (dataId == 3) {
            title = "Tambah Tabungan";
            strChoice = "Ambil uang dari:";
            inputType = TYPE_CLASS_NUMBER;
        } else if (dataId == 4) {
            title = "Kurangi Tabungan";
            strChoice = "Pindahkan uang ke:";
            inputType = TYPE_CLASS_NUMBER;
        } else {
            title = "Hapus Tabungan";
            tvInput.setText(String.valueOf(item.getTabungan()));
        }

        tvTitle.setText(title);
        etNama.setInputType(inputType);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(new ClickListener());
        if (viewId == 1) {
            if(dataId == 1) {
                llTarget.setVisibility(View.GONE);
            } else {
                llNama.setVisibility(View.GONE);
            }
        } else {
            choice = FROM_SALDO;
            tvChoice.setText(strChoice);
            if(item.getId() == 1) {
                btnFromElse.setVisibility(View.GONE);
            }
            btnFromSaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = 1;
                    btnFromSaldo.setImageResource(R.drawable.wishlist_saldo_clicked);
                    btnFromElse.setImageResource(R.drawable.wishlist_tabunganku_unclicked);
                }
            });
            btnFromElse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = 2;
                    btnFromElse.setImageResource(R.drawable.wishlist_tabunganku_clicked);
                    btnFromSaldo.setImageResource(R.drawable.wishlist_saldo_unclicked);
                }
            });

            if(dataId == DATA_DELETE) {
                llNonDelete.setVisibility(View.GONE);
            } else {
                llDelete.setVisibility(View.GONE);
            }
        }
    }

    private class ClickListener implements View.OnClickListener {
        static final int BTN_SAVE = 0;
        static final int BTN_SAVE_NON_DELETE = 1;
        static final int BTN_SAVE_DELETE = 2;

        @Override
        public void onClick(View view) {
            if(dataId != DATA_DELETE) {
                String strInput = etNama.getText().toString();
                Integer intInput = 0;
                try {
                    if(strInput.trim().length() > 0) {
                        intInput = Integer.parseInt(strInput);
                    }
                } catch (NumberFormatException e) {
                    Log.e(getClass().getSimpleName(), "Trying to convert non-numeric string to integer.");
                }
                if(dataId == DATA_NAMA) {
                    item.setNama(strInput);
                } else if(dataId == DATA_TARGET) {
                    String strTarget = etTarget.getText().toString();
                    try {
                        if(strTarget.trim().length() > 0) {
                            intInput = Integer.parseInt(strTarget);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(getClass().getSimpleName(), "Trying to convert non-numeric string to integer.");
                    }
                    item.setTarget(intInput);
                    listener.onSave(item);
                } else if(dataId == DATA_TABUNGAN_PLUS) {
                    if(choice == FROM_SALDO) {
                        if(!item.addTabungan(intInput, true)) {
                            Toast.makeText(getContext(), "Invalid Action", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(!item.addTabungan(intInput, false)) {
                            Toast.makeText(getContext(), "Invalid Action", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else if(dataId == DATA_TABUNGAN_MINUS) {
                    boolean isRequireSaldo = (choice == FROM_SALDO);
                    if(!item.decTabungan(intInput, isRequireSaldo)) {
                        Toast.makeText(getContext(), "Invalid Action", Toast.LENGTH_SHORT).show();
                    }
                }
                listener.onSave(item);
                getFragmentManager().popBackStack();
            } else {
                String strInput = tvInput.getText().toString();
                Integer intInput = 0;
                try {
                    if(strInput.trim().length() > 0) {
                        intInput = Integer.parseInt(strInput);
                    }
                } catch (NumberFormatException e) {
                    Log.e(getClass().getSimpleName(), "Trying to convert non-numeric string to integer.");
                }
                boolean isRequireSaldo = (choice == FROM_SALDO);
                if(!item.decTabungan(intInput, isRequireSaldo)) {
                    Toast.makeText(getContext(), "Invalid Action", Toast.LENGTH_SHORT).show();
                }
                listener.onSave(item);
                getFragmentManager().popBackStack();
            }
        }
    }
}
