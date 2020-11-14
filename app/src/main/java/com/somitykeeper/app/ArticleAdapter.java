package com.somitykeeper.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;

    private Context mContext;
    private WebView articleView;

    public ArticleAdapter(List<Article> list, Context context) {
        this.articles = list;
        this.mContext = context;
    }

    public List<Article> getArticleList() {
        return articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        Article currentArticle = articles.get(position);

        String pubDateString = currentArticle.getPubDate();

        try {
            String sourceDateString = currentArticle.getPubDate();

            if (sourceDateString != null) {
                SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Date date = sourceSdf.parse(sourceDateString);
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    pubDateString = sdf.format(date);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.title.setText(currentArticle.getTitle());

        Picasso.get()
                .load(currentArticle.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);

        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < currentArticle.getCategories().size(); i++) {
            if (i == currentArticle.getCategories().size() - 1) {
                categories.append(currentArticle.getCategories().get(i));
            } else {
                categories.append(currentArticle.getCategories().get(i)).append(", ");
            }
        }

        viewHolder.category.setText(categories.toString());

        String finalPubDateString = pubDateString;
        viewHolder.itemView.setOnClickListener(view -> {




            //show article content inside a dialog
//            articleView = new WebView(mContext);
//
//            articleView.getSettings().setLoadWithOverviewMode(true);

            String title = articles.get(viewHolder.getAdapterPosition()).getTitle();
            String Description = articles.get(viewHolder.getAdapterPosition()).getDescription();
            String Author = articles.get(viewHolder.getAdapterPosition()).getAuthor();
            String content = articles.get(viewHolder.getAdapterPosition()).getContent();
            String images = currentArticle.getImage();
            String pubdate = finalPubDateString;

//            articleView.getSettings().setJavaScriptEnabled(true);
//            articleView.setHorizontalScrollBarEnabled(false);
//            articleView.setWebChromeClient(new WebChromeClient());
//            articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +
//
//                    "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content, null, "utf-8", null);
//
//            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mContext).create();
//            alertDialog.setTitle(title);
//            alertDialog.setView(articleView);
//            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//
//            ((TextView) Objects.requireNonNull(alertDialog.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());

            Intent secondIntent = new Intent(mContext,SecondActivity.class);
            secondIntent.putExtra("title",title);
            secondIntent.putExtra("content",content);
            secondIntent.putExtra("pub_date",pubdate);
            secondIntent.putExtra("Images_link",images);
            secondIntent.putExtra("Description",Description);
            secondIntent.putExtra("Author",Author);

            mContext.startActivity(secondIntent);


        });
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        ImageView image;
        TextView category;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.categories);
        }
    }
}
