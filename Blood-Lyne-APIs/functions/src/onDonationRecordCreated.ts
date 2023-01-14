import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";

export const onDonationRecordCreated = functions.firestore.document('donations/{donationDoc}').onCreate((snap, context) => {
    const db = admin.firestore();
    const donationDocData: any = snap.data();
    const donationInfo = {
        medialCentreName: donationDocData.medialCentreName,
        donationId: donationDocData.donationId,
        requiredBloodGroup: donationDocData.requiredBloodGroup,
        noOfPintsNeeded: donationDocData.noOfPintsNeeded,
        requiredDate: donationDocData.requiredDate,
        requiredTime: donationDocData.requiredTime,
        notifiedDonors: donationDocData.notifiedDonors,
    }

    return new Promise((resolve, reject) => {
        syncNotifiedDonors(db, donationInfo)
            .then(() => resolve())
            .catch(error => {
                console.log(error);
                reject()
            })
    })
});


async function addDonationAlertToDonorProfile(db: any, donorsToBeSynced: any, donationInfo: any) {
    delete donationInfo['noOfPintsNeeded'];
    delete donationInfo['notifiedDonors'];
    donationInfo['isParticipating'] = false;
    let donationNotifications: any = [];
    for (const donor of donorsToBeSynced) {
        donationInfo['distanceToMedicalCentre'] = donor.distance;
        await db.collection('donors').doc(donor.email).get().then((doc: any) => {
            if (doc.data().donationNotifications) {
                donationNotifications = doc.data().donationNotifications;
            }
            donationNotifications.push(donationInfo);
            db.collection('donors').doc(donor.email).set({
                donationNotifications: donationNotifications,
            }, {merge: true});
        })
    }
    return;
}

async function syncNotifiedDonors(db: any, donationInfo: any) {
    return await addDonationAlertToDonorProfile(db, donationInfo.notifiedDonors, donationInfo);
}
