import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";

export const onPointAllocationRequest = functions.https.onRequest((request, response) => {
    const reqData = request.body;
    if (request.method === 'POST' && reqData.donationId && reqData.donorEmail) {
        const donationId = reqData.donationId;
        const donorEmail = reqData.donorEmail;
        const db = admin.firestore();

        db.collection('donations').doc(donationId).get()
            .then((doc: any) => {
                const docData: any = doc.data();
                const oldParticipantList: [] = docData.participants;
                const updatedParticipantList: [] = [];

                for (const participant of oldParticipantList) {
                    // @ts-ignore
                    if (participant.email == donorEmail) {
                        // @ts-ignore
                        participant.hasDonated = true;
                    }
                    updatedParticipantList.push(participant);
                }

                db.collection('donations').doc(donationId).set({participants: updatedParticipantList}, {merge: true})
                    .then(() => {
                        db.collection('donors').doc(donorEmail).get()
                            .then((doc: any) => {
                                const docData: any = doc.data();
                                const oldDonationNotifications: [] = docData.donationNotifications;
                                const newDonationNotifications: [] = [];
                                let donationPoints: number = docData.donationPoints;
                                donationPoints += 10; // Incrementing donation points
                                const badges: any = docData.badges;

                                for (const notification of oldDonationNotifications) {
                                    // @ts-ignore
                                    if (notification.donationId != donationId) {
                                        newDonationNotifications.push(notification);
                                    }
                                }

                                db.collection('badges')
                                    .where('isActive', '==', true)
                                    .where('pointsRequired', '==', donationPoints)
                                    .limit(1).get()
                                    .then((querySnapshot) => {
                                        if (!querySnapshot.empty) {
                                            querySnapshot.forEach((querySnapshotDocument) => {
                                                const badgeDoc: any = querySnapshotDocument.data();
                                                badges.push({
                                                    id: badgeDoc.id,
                                                    imageURL: badgeDoc.imageURL,
                                                    title: badgeDoc.title,
                                                    achievedOn : new Date().getTime()
                                                })
                                            });
                                        }

                                        db.collection('donors').doc(donorEmail).set({
                                            donationNotifications: newDonationNotifications,
                                            isActiveParticipant: false,
                                            donationPoints: donationPoints,
                                            lastDonatedDate: new Date().getTime(),
                                            badges: badges
                                        }, {merge: true})
                                            .then(() => {
                                                response.status(200).send('Donation confirmed. Donation points has been added to donor\'s account');
                                            }).catch((error) => {
                                            console.log(error);
                                            response.status(400).send(error);
                                        });
                                    }).catch((error) => {
                                    console.log(error);
                                    response.status(400).send(error);
                                })
                            }).catch((error) => {
                            console.log(error);
                            response.status(400).send(error);
                        })
                    }).catch((error) => {
                    console.log(error);
                    response.status(400).send(error);
                })
            })
    } else {
        response.status(400).send("Bad request");
    }
})
