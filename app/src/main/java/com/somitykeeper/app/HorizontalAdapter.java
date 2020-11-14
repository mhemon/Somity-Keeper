package com.somitykeeper.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private List<Article> articles;

    private Context mContext;

    public HorizontalAdapter(List<Article> list, Context context) {
        this.articles = list;
        this.mContext = context;
    }

    public List<Article> getArticleList() {
        return articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item, viewGroup, false);
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

        //viewHolder.category.setText(categories.toString());

        String finalPubDateString = pubDateString;
        viewHolder.itemView.setOnClickListener(view -> {
            String title = articles.get(viewHolder.getAdapterPosition()).getTitle();
            String Description = articles.get(viewHolder.getAdapterPosition()).getDescription();
            String Author = articles.get(viewHolder.getAdapterPosition()).getAuthor();
            String content = articles.get(viewHolder.getAdapterPosition()).getContent();
            String images = currentArticle.getImage();
            String pubdate = finalPubDateString;

            Intent secondIntent = new Intent(mContext,SecondActivity.class);
            secondIntent.putExtra("title",title);
            secondIntent.putExtra("content",content);
            secondIntent.putExtra("pub_date",pubdate);
            secondIntent.putExtra("Images_link",images);
            secondIntent.putExtra("Description",Description);
            secondIntent.putExtra("Author",Author);

            mContext.startActivity(secondIntent);

        });

        viewHolder.detailsbtn.setOnClickListener(view -> {
            String title = articles.get(viewHolder.getAdapterPosition()).getTitle();
            String Description = articles.get(viewHolder.getAdapterPosition()).getDescription();
            String Author = articles.get(viewHolder.getAdapterPosition()).getAuthor();
            String content = articles.get(viewHolder.getAdapterPosition()).getContent();
            String images = currentArticle.getImage();
            String pubdate = finalPubDateString;

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
        Button detailsbtn;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.h_s_product_title);
            pubDate = itemView.findViewById(R.id.h_s_time);
            image = itemView.findViewById(R.id.h_s_product_images);
            detailsbtn = itemView.findViewById(R.id.h_s_details);
        }
    }
}
