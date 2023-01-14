package com.bloodlyne.firebase;

import com.bloodlyne.models.Doctor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {

    public FirebaseAuth auth;
    private static Doctor authDoctor;

    public AuthService() {
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return auth;
    }

    public static void setAuthDoctor(Doctor doctor) {
        authDoctor = doctor;
    }

    public static Doctor getAuthDoctor() {
        return authDoctor;
    }

    public Task<AuthResult> signUp(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }
}
