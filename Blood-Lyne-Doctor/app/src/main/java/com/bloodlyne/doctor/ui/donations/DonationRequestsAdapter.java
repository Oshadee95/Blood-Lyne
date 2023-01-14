package com.bloodlyne.doctor.ui.donations;

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

import com.bloodlyne.doctor.CloudFunctionService;
import com.bloodlyne.doctor.R;
import com.bloodlyne.models.DonationConfirmation;
import com.bloodlyne.models.DonationParticipant;

import java.net.URL;
import java.util.List;

public class DonationRequestsAdapter extends RecyclerView.Adapter<DonationRequestsAdapter.DonationRequestViewHolder> {

    private Context context;
    private List<DonationParticipant> donationParticipantList;
    private ProgressDialog progressDialog;

    DonationRequestsAdapter(Context context, List<DonationParticipant> notificationInfoList) {
        this.context = context;
        this.donationParticipantList = notificationInfoList;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    public DonationRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.donation_participant_row, parent, false);
        return new DonationRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationRequestViewHolder holder, int position) {
        holder.name.setText(donationParticipantList.get(position).getName());
        holder.distanceToMedicalCentre.setText(donationParticipantList.get(position).getDistanceToMedicalCentre() + " away");
        holder.bloodGroup.setText(donationParticipantList.get(position).getBloodGroup() + " blood group");
        holder.genderAndAge.setText(donationParticipantList.get(position).getGender() + " | " + donationParticipantList.get(position).getAge() + " years");
        holder.phoneNumber.setText(donationParticipantList.get(position).getPhoneNumber());
        holder.confirmParticipationBtn.setOnClickListener(v -> {
            progressDialog.setCancelable(false); //is used so that ProgressDialog cannot be cancelled until the work is done
            progressDialog.setMessage("Confirming donation");
            progressDialog.show(); //To start the ProgressDialog

            try {
                URL url = new URL("https://us-central1-blood-lyne.cloudfunctions.net/onPointAllocationRequest");
                DonationConfirmation donationConfirmation = new DonationConfirmation(donationParticipantList.get(position).getDonationId(), donationParticipantList.get(position).getEmail());

                //  Reference : https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
                new CloudFunctionService(url, donationConfirmation.toString(), s -> {
                    progressDialog.dismiss(); //To stop the ProgressDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    AlertDialog dialog = builder.create();
                    dialog.setMessage(s);
                    CharSequence charSequence = "Close";
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, charSequence, (dialog1, which) -> {
                        holder.confirmParticipationBtn.setVisibility(View.INVISIBLE);
                        dialog1.cancel();
                    });
                    dialog.show();
                }).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (donationParticipantList.get(position).getHasDonated()) {
            holder.confirmParticipationBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return donationParticipantList.size();
    }

    public class DonationRequestViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroup, distanceToMedicalCentre, name, genderAndAge, phoneNumber;
        ImageView confirmParticipationBtn;

        public DonationRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroup = itemView.findViewById(R.id.donorBloodGroupTV);
            distanceToMedicalCentre = itemView.findViewById(R.id.donorDistanceTV);
            name = itemView.findViewById(R.id.donorNameTV);
            genderAndAge = itemView.findViewById(R.id.donorGenderAndAgeTV);
            phoneNumber = itemView.findViewById(R.id.donorPhoneNumberTV);
            confirmParticipationBtn = itemView.findViewById(R.id.donorConfirmDonation);
            confirmParticipationBtn.setClickable(true);
        }
    }
}
