package com.somitykeeper.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {

    private List<NotificationModel> list;

    public NotificationAdapter(List<NotificationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(list.get(position).getTitle(),list.get(position).getDescription(),list.get(position).getImg_url(),list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView Description;
        TextView Date;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.noti_img_view);
            title = itemView.findViewById(R.id.noti_title);
            Description = itemView.findViewById(R.id.noti_description);
            Date = itemView.findViewById(R.id.date);

        }
        private void setData(final String title, final String description, final String url, final String date){
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);
            this.Description.setText(description);
            this.Date.setText(date);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
//                    setIntent.putExtra("title",title);
//                    setIntent.putExtra("position",position);
//                    setIntent.putExtra("ImageUrl",url);
//                    itemView.getContext().startActivity(setIntent);
//                }
//            });
        }
    }

}
