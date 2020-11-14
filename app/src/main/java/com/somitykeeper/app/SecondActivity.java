package com.somitykeeper.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView titletext;
    private WebView mWebview;
    private ImageView imageView;

    //////////////////////////////////// new code

//    private ImageView ivPreview;
//    private TextView tvTitle,tvSource,tvTime,tvDescrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //////////////////////////////////// new code

        //ivPreview = findViewById(R.id.ivPreview);
//        tvTitle = findViewById(R.id.tvTitle);
//        tvSource = findViewById(R.id.tvSource);
//        tvTime = findViewById(R.id.tvTime);
        //tvDescrition = findViewById(R.id.tvDescrition);

        titletext = findViewById(R.id.title_txt);
        mWebview = findViewById(R.id.mWebview);
        imageView = findViewById(R.id.back);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String pub_date = getIntent().getStringExtra("pub_date");
        String Images_link = getIntent().getStringExtra("Images_link");
        String Description = getIntent().getStringExtra("Description");
        String Author = getIntent().getStringExtra("Author");

//////////////////////////////////// new code
//        Picasso.get()
//                .load(Images_link)
//                .placeholder(R.drawable.placeholder)
//                .into(ivPreview);

//        tvTitle.setText(title);
//        tvSource.setText(Author);
//        tvTime.setText(pub_date);
//        tvTitle.setText(title);
        //tvDescrition.setText(Description);


            titletext.setText(title);

            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.setHorizontalScrollBarEnabled(false);
            mWebview.setWebChromeClient(new WebChromeClient());
            mWebview.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                    "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content, null, "utf-8", null);
    }
}