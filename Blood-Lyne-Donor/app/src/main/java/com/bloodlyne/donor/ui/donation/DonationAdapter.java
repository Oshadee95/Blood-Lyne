package com.bloodlyne.donor.ui.donation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.R;
import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.firebase.function.InviteResponseService;
import com.bloodlyne.models.InviteResponse;
import com.bloodlyne.models.NotificationInfo;

import java.net.URL;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.NotificationViewHolder> {

    private Context context;
    private List<NotificationInfo> notificationInfoList;
    private ProgressDialog progressDialog;

    DonationAdapter(Context context, List<NotificationInfo> notificationInfoList) {
        this.context = context;
        this.notificationInfoList = notificationInfoList;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.donation_info_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.medicalCentreNameTV.setText(notificationInfoList.get(position).getMedialCentreName());
        holder.distanceToMedicalCentreTV.setText(notificationInfoList.get(position).getDistanceToMedicalCentre());
        holder.bloodGroupTV.setText("Blood Group " + notificationInfoList.get(position).getRequiredBloodGroup());
        holder.donationDateAndTimeTV.setText(notificationInfoList.get(position).getRequiredDate() + " at " + notificationInfoList.get(position).getRequiredTime());

        holder.cancelInviteBtn.setClickable(true);
        holder.cancelInviteBtn.setOnClickListener(v -> {
            InviteResponse inviteResponse = new InviteResponse(notificationInfoList.get(position).getDonationId(), AuthService.getAuthDonor().getEmail(), false);
            progressDialog.setCancelable(false); //is used so that ProgressDialog cannot be cancelled until the work is done
            progressDialog.setMessage("Cancelling donation invite");
            progressDialog.show(); //To start the ProgressDialog

            try {
                URL url = new URL("https://us-central1-blood-lyne.cloudfunctions.net/onDonationInviteRespondRequest");

                //  Reference : https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
                new InviteResponseService(url, inviteResponse.toString(), s -> {
                    progressDialog.dismiss(); //To stop the ProgressDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    AlertDialog dialog = builder.create();
                    dialog.setMessage(s);
                    CharSequence charSequence = "Close";
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, charSequence, (dialog1, which) -> {
                        dialog1.cancel();
                        notificationInfoList.remove(position);
                        notifyItemRemoved(position);
                    });
                    dialog.show();
                }).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        holder.acceptInviteBtn.setClickable(true);
        holder.acceptInviteBtn.setOnClickListener(v -> {
            InviteResponse inviteResponse = new InviteResponse(notificationInfoList.get(position).getDonationId(), AuthService.getAuthDonor().getEmail(), true);
            progressDialog.setCancelable(false); //is used so that ProgressDialog cannot be cancelled until the work is done
            progressDialog.setMessage("Confirming donation invite");
            progressDialog.show(); //To start the ProgressDialog

            try {
                URL url = new URL("https://us-central1-blood-lyne.cloudfunctions.net/onDonationInviteRespondRequest");

                //  Reference : https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
                new InviteResponseService(url, inviteResponse.toString(), s -> {
                    progressDialog.dismiss(); //To stop the ProgressDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    AlertDialog dialog = builder.create();
                    dialog.setMessage(s);
                    CharSequence charSequence = "Close";
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, charSequence, (dialog1, which) -> {
                        dialog1.cancel();
                        notificationInfoList.get(position).setParticipating(true);
                        notifyItemChanged(position, notificationInfoList.get(position));
                    });
                    dialog.show();
                }).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (notificationInfoList.get(position).isParticipating()) {
            holder.acceptInviteBtn.setVisibility(View.INVISIBLE);
            holder.cancelInviteBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notificationInfoList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView medicalCentreNameTV, distanceToMedicalCentreTV, bloodGroupTV, donationDateAndTimeTV;
        ImageView cancelInviteBtn, acceptInviteBtn;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            medicalCentreNameTV = itemView.findViewById(R.id.medicalCentreNameTV);
            distanceToMedicalCentreTV = itemView.findViewById(R.id.distanceToMedicalCentreTV);
            bloodGroupTV = itemView.findViewById(R.id.bloodGroupTV);
            donationDateAndTimeTV = itemView.findViewById(R.id.donationDateAndTimeTV);
            cancelInviteBtn = itemView.findViewById(R.id.cancelInviteBtn);
            acceptInviteBtn = itemView.findViewById(R.id.acceptInviteBtn);
        }
    }
}
