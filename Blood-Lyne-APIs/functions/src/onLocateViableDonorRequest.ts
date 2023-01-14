import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";
import * as APIUtils from "./utils/APIUtils";

let donorsInProximity: any = [];

export const onLocateViableDonorRequest = functions.https.onRequest((request, response) => {
        const reqData = request.body;
        if (request.method === 'POST' && reqData.country && reqData.medialCentreName && reqData.bloodGroup && reqData.latitude && reqData.longitude && reqData.noOfPintsNeeded && reqData.requiredDateTime) {
            const noOfPintsNeeded: number = parseInt(reqData.noOfPintsNeeded);
            const requiredBloodGroup: string = reqData.bloodGroup;
            const donationRequiredDateTime: number = parseInt(reqData.requiredDateTime.trim());
            const donationRequiredDate: string = new Date(donationRequiredDateTime).toLocaleDateString("en-US");
            const donationRequiredTime: string = new Date(donationRequiredDateTime).toLocaleTimeString("en-US");
            const eligibleDonors: any = [];
            const donationId: string = (new Date()).getTime().toString(36) + Math.random().toString(36).slice(2);
            const db = admin.firestore();
            db.collection('donors')
                .where('locationInfo.country', '==', reqData.country)
                .where('isActive', '==', true)
                .where('bloodGroup', 'in', ['O-', requiredBloodGroup])
                .where('isActiveParticipant', '==', false)
                .get()
                .then((snap) => {
                    snap.forEach((doc) => {
                        const donorData = doc.data();
                        const lastDonatedDate = donorData.lastDonatedDate;
                        const dateDiff = Math.floor((donationRequiredDateTime - lastDonatedDate) / 86400000); // Date difference between current date and last donated date
                        switch (reqData.donationType) {
                            case 'whole_blood' :
                                if (isEligibleWholeBloodDonation(donorData, dateDiff))
                                    eligibleDonors.push(donorData)
                                break;
                            case 'power_red' :
                                if (isEligibleForPowerRedDonation(donorData, dateDiff))
                                    eligibleDonors.push(donorData)
                                break;
                            case 'platelet' :
                                if (isEligibleForPlateletDonation(donorData, dateDiff))
                                    eligibleDonors.push(donorData)
                                break;
                            case 'ab_elite_plasma' :
                                if (isEligibleForABElitePlasmaDonation(donorData, dateDiff))
                                    eligibleDonors.push(donorData)
                                break;
                        }
                    });

                    getDonorsInProximity(eligibleDonors, reqData).then(donorArray => {
                        const donationInfo = {
                            medialCentreName: reqData.medialCentreName,
                            donationId: donationId,
                            requiredBloodGroup: requiredBloodGroup,
                            noOfPintsNeeded: noOfPintsNeeded,
                            requiredDate: donationRequiredDate,
                            requiredTime: donationRequiredTime
                        }
                        alertDonorsInProximity(donorArray, donationInfo, db).then(r2 => {
                            let donorWord: string = (r2.length > 1) ? 'donors' : 'donor';
                            response.status(200).send(r2.length + ` ${donorWord} has been notified`);
                        }).catch((error) => {
                            console.log(error);
                            response.status(400).send(error);
                        });
                    }).catch((error) => {
                        console.log(error);
                        response.status(400).send(error);
                    });

                }).catch((error) => {
                console.log(error);
                response.status(400).send(error);
            })
        } else {
            response.status(400).send({result: "Bad request"});
        }
    }
)

function isEligibleWholeBloodDonation(donorData: any, dateDiff: number) {
    return dateDiff >= 56 && donorData.age >= 16 && donorData.weight >= 110
}

function isEligibleForPowerRedDonation(donorData: any, dateDiff: number) {
    if (dateDiff >= 112) {
        if (donorData.gender === 'Female' && donorData.age >= 19 && donorData.height == 5.5 && donorData.weight >= 150) {
            return true;
        } else if (donorData.gender === 'Male' && donorData.age >= 17 && donorData.height == 5.1 && donorData.weight >= 130) {
            return true;
        }
    }
    return false;
}

function isEligibleForPlateletDonation(donorData: any, dateDiff: number) {
    return dateDiff >= 7 && donorData.age >= 17 && donorData.weight >= 110;
}

function isEligibleForABElitePlasmaDonation(donorData: any, dateDiff: number) {
    const bloodGroupWithoutSuffix = donorData.bloodGroup.replace(/[^a-zA-Z]+/g, '');
    return bloodGroupWithoutSuffix == 'AB' && dateDiff >= 28 && donorData.age >= 17 && donorData.weight >= 110
}

async function getDonorDistance(eligibleDonors: any, reqData: any) {
    let distanceReport;
    donorsInProximity = [];
    for (const donor of eligibleDonors) {
        distanceReport = await APIUtils.getDonorTravelDistance(donor.locationInfo.geoCoordinates.latitude, donor.locationInfo.geoCoordinates.longitude, reqData.latitude, reqData.longitude)
        let distance = distanceReport.data.rows[0].elements[0].distance.text;
        donorsInProximity.push({
            distance: distance.replace(/[^0-9.]+/, ''),
            fcmToken: donor.fcmToken,
            gender: donor.gender,
            phoneNumber: donor.phoneNumber,
            name: donor.displayName,
            bloodGroup: donor.bloodGroup,
            socialSecurityNumber: donor.socialSecurityNumber,
            age: donor.age,
            email: donor.email
        });
    }
    donorsInProximity.sort((a: any, b: any) => a.distance > b.distance ? 1 : -1);
    return donorsInProximity;
}

async function getDonorsInProximity(eligibleDonors: any, reqData: any) {
    return await getDonorDistance(eligibleDonors, reqData);
}

async function addNewDonationRecord(donors: any, db: any, donationInfo: any) {
    return await db.collection('donations').doc(donationInfo.donationId).set({
        medialCentreName: donationInfo.medialCentreName,
        donationId: donationInfo.donationId,
        requiredBloodGroup: donationInfo.requiredBloodGroup,
        noOfPintsNeeded: donationInfo.noOfPintsNeeded,
        requiredDate: donationInfo.requiredDate,
        requiredTime: donationInfo.requiredTime,
        notifiedDonors: donors,
        participants: [],
        isDonationActive: true,
        requestedDateAndTime : new Date().toISOString()
    }, {merge: true});
}

async function alertDonor(donorData: any, donationInfo : any) {
    const payload = {
        token: donorData.fcmToken,
        notification: {
            title: 'Donation Alert',
            body: `Donation request for blood group ${donationInfo.requiredBloodGroup} (Approx : ${donorData.distance} miles)`
        },
        data: {
            body: `Dear ${donorData.name}, donation message`
        }
    };
    return admin.messaging().send(payload);
}

async function alertDonorsInProximity(donorsInProximity: any, donationInfo: any, db: any) {
    let notifiedDonors: any = [];
    let notifiedDonorCount: number = 0;
    for (const donor of donorsInProximity) {
        if (notifiedDonorCount < donationInfo.noOfPintsNeeded) {
            await alertDonor(donor, donationInfo);
            delete donor['fcmToken'];
            donor['distance'] = donor.distance + ' miles'
            notifiedDonors.push(donor);
            notifiedDonorCount++;
        } else {
            break;
        }
    }
    await addNewDonationRecord(notifiedDonors, db, donationInfo);
    return notifiedDonors;
}
