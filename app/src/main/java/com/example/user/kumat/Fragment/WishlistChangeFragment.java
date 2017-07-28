package com.example.user.kumat.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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

    private LinearLayout llDelete, llNonDelete;
    private RelativeLayout relativeLayout;
    private TextView tvTitle, tvInput;
    private EditText etInput;
    private Button btnSaveDelete, btnSaveNonDelete, btnSave;
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
            btnSave = (Button) view.findViewById(R.id.btn_save);
            viewId = 1;
        } else {
            view = inflater.inflate(R.layout.fragment_wishlist_change_2, container, false);
            btnFromSaldo = (ImageButton) view.findViewById(R.id.ib_saldo);
            btnFromElse = (ImageButton) view.findViewById(R.id.ib_setoran);
            llDelete = (LinearLayout) view.findViewById(R.id.ll_delete);
            llNonDelete = (LinearLayout) view.findViewById(R.id.ll_non_delete);
            btnSaveNonDelete = (Button) view.findViewById(R.id.btn_save_1);
            btnSaveDelete = (Button) view.findViewById(R.id.btn_save_2);

            viewId = 2;
        }

        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_fragment_wishlist_change_text);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvInput = (TextView) view.findViewById(R.id.tv_input);
        etInput = (EditText) view.findViewById(R.id.et_input);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String title = "";
        int inputType = 0;
        if(dataId == 1) {
            title = "CHANGE NAMA";
            inputType = TYPE_CLASS_TEXT;
        } else if(dataId == 2) {
            title = "CHANGE TARGET";
            inputType = TYPE_CLASS_NUMBER;
        } else if(dataId == 3) {
            title = "TAMBAH TABUNGAN";
            inputType = TYPE_CLASS_NUMBER;
        } else if(dataId == 4) {
            title = "KURANG TABUNGAN";
            inputType = TYPE_CLASS_NUMBER;
        } else {
            title = "HAPUS TABUNGAN";
            tvInput.setText(String.valueOf(item.getTabungan()));
        }

        tvTitle.setText(title);
        etInput.setInputType(inputType);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        if(viewId == 1) {
            btnSave.setOnClickListener(new ClickListener(ClickListener.BTN_SAVE));
        } else {
            choice = FROM_SALDO;
            if(item.getId() == 1) {
                btnFromElse.setVisibility(View.GONE);
            }
            btnFromSaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(choice == 2) {
                        btnFromSaldo.setImageResource(R.drawable.ib_saldo);
                        btnFromElse.setImageResource(R.drawable.ib_setoran_non);
                        choice = 1;
                    }
                }
            });
            btnFromElse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(choice == 1) {
                        btnFromElse.setImageResource(R.drawable.ib_setoran);
                        btnFromSaldo.setImageResource(R.drawable.ib_saldo_non);
                        choice = 2;
                    }
                }
            });

            if(dataId == DATA_DELETE) {
                llNonDelete.setVisibility(View.GONE);
            } else {
                llDelete.setVisibility(View.GONE);
            }

            btnSaveNonDelete.setOnClickListener(new ClickListener(ClickListener.BTN_SAVE_NON_DELETE));
            btnSaveDelete.setOnClickListener(new ClickListener(ClickListener.BTN_SAVE_DELETE));
        }

    }

    private class ClickListener implements View.OnClickListener {
        static final int BTN_SAVE = 0;
        static final int BTN_SAVE_NON_DELETE = 1;
        static final int BTN_SAVE_DELETE = 2;

        int buttonId;

        public ClickListener(int buttonId) {
            this.buttonId = buttonId;
        }

        @Override
        public void onClick(View view) {
            if(buttonId == BTN_SAVE || buttonId == BTN_SAVE_NON_DELETE) {
                String strInput = etInput.getText().toString();
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
                    item.setTarget(intInput);
                } else if(dataId == DATA_TABUNGAN_PLUS) {
                    if(choice == FROM_SALDO) {
                        if(!item.addTabungan(intInput, true)) {
                            Toast.makeText(getContext(), "Invalid Action", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        item.addTabungan(intInput, false);
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
