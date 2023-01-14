package com.bloodlyne.firebase.authentication;

import com.bloodlyne.models.Donor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {

    public FirebaseAuth auth;
    private static Donor authDonor;

    public AuthService(){
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return auth;
    }

    public static void setAuthDonor(Donor donor){
        authDonor = donor;
    }

    public static Donor getAuthDonor(){
        return authDonor;
    }

    public Task<AuthResult> signUp(String email, String password){
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String email, String password){
        return auth.signInWithEmailAndPassword(email, password);
    }

}
