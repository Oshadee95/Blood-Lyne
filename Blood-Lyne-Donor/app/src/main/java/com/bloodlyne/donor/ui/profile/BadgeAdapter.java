package com.bloodlyne.donor.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.R;
import com.bloodlyne.models.BadgeInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.NotificationViewHolder> {

    private Context context;
    private List<BadgeInfo> badgeList;
    private ProgressDialog progressDialog;

    BadgeAdapter(Context context, List<BadgeInfo> badgeList) {
        this.context = context;
        this.badgeList = badgeList;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.profile_badge_column, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Picasso.get().load(badgeList.get(position).getImageURL()).into(holder.badgeImage);
        holder.badgeTitle.setText(badgeList.get(position).getTitle());
        holder.achievedDate.setText(unixToDate(badgeList.get(position).getAchievedOn()));

    }

    private String unixToDate(long unixDate) {
        java.util.Date date = new java.util.Date((long) unixDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView badgeTitle, achievedDate;
        ImageView badgeImage;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badgeIV);
            badgeTitle = itemView.findViewById(R.id.badgeTitleTV);
            achievedDate = itemView.findViewById(R.id.badgeAchievedOnTV);
        }
    }
}
