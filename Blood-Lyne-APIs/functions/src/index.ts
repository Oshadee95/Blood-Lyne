import * as admin from "firebase-admin";

admin.initializeApp();

export {onLocateViableDonorRequest} from "./onLocateViableDonorRequest";
export {onDonationRecordCreated} from "./onDonationRecordCreated";
export {onDonationInviteRespondRequest} from "./onDonationInviteRespondRequest";
export {onPointAllocationRequest} from "./onPointAllocationRequest";
