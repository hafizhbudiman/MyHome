package com.kecepret.myhome.adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.kecepret.myhome.R;
import com.kecepret.myhome.model.Notification;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private List<Notification> items = new ArrayList<>();
    private int itemLayout = R.layout.item_notification;
    Context context;

    public NotificationAdapter(List<Notification> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false), context);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void addItems(List<Notification> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        // TODO: add itemView component

        private de.hdodenhof.circleimageview.CircleImageView notificationImage;
        private TextView message;
        private Button share;
        Context context;

        public NotificationHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            bindView();
        }

        private void bindView() {
            message = itemView.findViewById(R.id.notification_title);
            notificationImage = itemView.findViewById(R.id.notification_image);
            share = itemView.findViewById(R.id.share_button);
        }

        public void bind(Notification item) {
            if (item.getTipe() == 2) {
                String msg = "You have received IDR " + item.getNominal() + " token reward";
                message.setText(msg);
                notificationImage.setImageResource(R.drawable.diamond);
                share.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
            } else {
                String msg = "Unauthorized activity detected on front door";
                message.setText(msg);
                notificationImage.setImageResource(R.drawable.padlock);
                share.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRed)));
            }
        }
    }
}