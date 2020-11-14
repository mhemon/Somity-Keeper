package com.somitykeeper.app.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.somitykeeper.app.BarCodeDetector;
import com.somitykeeper.app.MainActivity2;
import com.somitykeeper.app.R;
import com.somitykeeper.app.WelcomeActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class ManageFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12;
    private View activestatus;
    private boolean isactive;

    public ManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        GifImageView gifImageView = view.findViewById(R.id.gif_images);
        btn1 = view.findViewById(R.id.constraintLayout1);
        btn2 = view.findViewById(R.id.constraintLayout2);
        btn3 = view.findViewById(R.id.constraintLayout3);
        btn4 = view.findViewById(R.id.constraintLayout4);
        btn5 = view.findViewById(R.id.constraintLayout5);
        btn6 = view.findViewById(R.id.constraintLayout6);
        btn7 = view.findViewById(R.id.constraintLayout7);
        btn8 = view.findViewById(R.id.constraintLayout8);
        btn9 = view.findViewById(R.id.constraintLayout9);
        btn10 = view.findViewById(R.id.constraintLayout10);
        btn11 = view.findViewById(R.id.constraintLayout11);
        btn12 = view.findViewById(R.id.constraintLayout12);
        activestatus = view.findViewById(R.id.active_status);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);

        int from = 900;
        int to = 1800;
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.setTime(date);
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        isactive = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);


        if (isactive) {
            activestatus.setVisibility(View.VISIBLE);
            if (day == Calendar.FRIDAY) {
                isactive = false;
                activestatus.setVisibility(View.INVISIBLE);
            }
        }else {
            activestatus.setVisibility(View.INVISIBLE);
        }


        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareIntent(getResources().getString(R.string.referal_program));
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayout1:
                //btn1 click
            if (isactive){
                //we are open to receive call
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                showdialog();
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(getContext(), "We can't make call happend without permission.", Toast.LENGTH_SHORT).show();
                            }
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }else {
                String data = Prefs.getString("NAME","Guest");
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext()).create();
                alertDialog.setTitle("Hello, "+data);
                alertDialog.setMessage(Html.fromHtml(getActivity().getString(R.string.not_available) +
                        " <a href='https://www.facebook.com/shomitikeepr'>Facebook.</a>" +
                        getActivity().getString(R.string.availity)));
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                ((TextView) Objects.requireNonNull(alertDialog.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
            }
                break;
            case R.id.constraintLayout2:
            shareIntent(getResources().getString(R.string.complain_box));
                break;
            case R.id.constraintLayout3:
                shareIntent(getResources().getString(R.string.facebook_page));
                break;
            case R.id.constraintLayout4:
                shareIntent(getResources().getString(R.string.tutorial));
                break;
            case R.id.constraintLayout5:
                shareIntent(getResources().getString(R.string.question));
                break;
            case R.id.constraintLayout6:
                shareIntent(getResources().getString(R.string.pricing));
                break;
            case R.id.constraintLayout7:
                shareIntent(getResources().getString(R.string.testomonial));
                break;
            case R.id.constraintLayout8:
                shareIntent(getResources().getString(R.string.success_story));
                break;
            case R.id.constraintLayout9:
                shareIntent(getResources().getString(R.string.timeline));
                break;
            case R.id.constraintLayout10:
                shareIntent(getResources().getString(R.string.recently_added_somity));
                break;
            case R.id.constraintLayout11:
                Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(welcomeIntent);
                break;
            case R.id.constraintLayout12:
                Intent qrIntent = new Intent(getActivity(), BarCodeDetector.class);
                startActivity(qrIntent);
                break;

            default:
                break;
        }
    }

    private void showdialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_for_phone);
        Button call1,call2,nothnks,whatapp,imo;

        call1 = dialog.findViewById(R.id.call1);
        call2 = dialog.findViewById(R.id.call2);
        nothnks = dialog.findViewById(R.id.no_thnks);
        whatapp = dialog.findViewById(R.id.whatsapp);
        imo = dialog.findViewById(R.id.imo);

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = getResources().getString(R.string.call1);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                requireActivity().startActivity(intent);
            }
        });
        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = getResources().getString(R.string.call2);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                requireActivity().startActivity(intent);
            }
        });
        nothnks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        whatapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://wa.me/+8801689655055";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        imo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imo.android.imoim");
                try {
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getContext(), "Imo Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void shareIntent(String url){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
    }

}