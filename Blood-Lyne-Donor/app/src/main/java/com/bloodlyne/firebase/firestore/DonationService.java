package com.bloodlyne.firebase.firestore;

import com.bloodlyne.models.NotificationInfo;

public class DonationService extends CollectionService<NotificationInfo> {

    public DonationService() {
        super("donations");
    }
}
