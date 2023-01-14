import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";

export const onDonationInviteRespondRequest = functions.https.onRequest((request, response) => {
        const reqData = request.body;
        if (request.method === 'POST' && reqData.donationId && reqData.donorEmail && reqData.donorResponse) {
            const donationId = reqData.donationId;
            const donorEmail = reqData.donorEmail;
            const donorResponse = reqData.donorResponse;
            const db = admin.firestore();

            console.log(donorResponse);
            if (donorResponse == 'true') {
                db.collection('donations').doc(donationId).get().then((doc: any) => {
                    const docData = doc.data();
                    const notifiedDonors: [] = docData.notifiedDonors;
                    const participants: [] = docData.participants;

                    for (const notifiedDonor of notifiedDonors) {
                        // @ts-ignore
                        if (notifiedDonor.email == donorEmail) {
                            // @ts-ignore
                            notifiedDonor['hasDonated'] = false;
                            participants.push(notifiedDonor);
                            break;
                        }
                    }

                    db.collection('donations').doc(donationId).set({participants: participants}, {merge: true})
                        .then(() => {
                            db.collection('donors').doc(donorEmail).get().then((doc: any) => {
                                const docData = doc.data();
                                const oldDonationNotifications: [] = docData.donationNotifications;
                                const newDonationNotifications: [] = [];

                                for (const notification of oldDonationNotifications) {
                                    // @ts-ignore
                                    if (notification.donationId == donationId) {
                                        // @ts-ignore
                                        notification['isParticipating'] = true;
                                    }
                                    newDonationNotifications.push(notification);
                                }

                                db.collection('donors').doc(donorEmail).set({
                                    donationNotifications: newDonationNotifications,
                                    isActiveParticipant: true
                                }, {merge: true})
                                    .then(() => {
                                        response.status(200).send('Thank you for accepting. Participation confirmed as attending')
                                    }).catch((error) => {
                                    console.log(error);
                                    response.status(400).send(error)
                                })
                            })
                        }).catch((error) => {
                        console.log(error);
                        response.status(400).send(error);
                    })
                }).catch((error) => {
                    console.log(error);
                    response.status(400).send(error);
                })
            } else {
                db.collection('donors').doc(donorEmail).get().then((doc: any) => {
                    const docData = doc.data();
                    const oldDonationNotifications: [] = docData.donationNotifications;
                    const newDonationNotifications: [] = [];

                    for (const notification of oldDonationNotifications) {
                        // @ts-ignore
                        if (notification.donationId != donationId) {
                            newDonationNotifications.push(notification);
                        }
                    }

                    db.collection('donors').doc(donorEmail).set({donationNotifications: newDonationNotifications}, {merge: true})
                        .then(() => {
                            response.status(200).send('Thank you for informing. Participation confirmed as not attending')
                        }).catch((error) => {
                        console.log(error);
                        response.status(400).send(error);
                    })
                })
            }
        } else {
            response.status(400).send({result: "Bad request"});
        }
    }
)
