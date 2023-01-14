package com.bloodlyne.donor.ui.insurance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.R;
import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.models.Insurance;

import java.util.List;

public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.NotificationViewHolder> {

    private Context context;
    private List<Insurance> insuranceList;
    private int donorDonationPoints;

    InsuranceAdapter(Context context, List<Insurance> insuranceList) {
        this.context = context;
        this.insuranceList = insuranceList;
        this.donorDonationPoints = Math.round(AuthService.getAuthDonor().getDonationPoints());
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.insurance_package_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.packageName.setText(insuranceList.get(position).getPackageName());
        holder.providerName.setText(insuranceList.get(position).getProviderName());
        holder.description.setText(insuranceList.get(position).getDescription());

        if(insuranceList.get(position).getPointsNeededToClaim() > donorDonationPoints){
            holder.claimButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return insuranceList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView providerName, packageName, description;
        Button claimButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.insurancePackageNameTV);
            providerName = itemView.findViewById(R.id.insuranceProviderTV);
            description = itemView.findViewById(R.id.insurancePackageDescription);
            claimButton = itemView.findViewById(R.id.insuranceClaimBtn);
        }
    }
}
