package com.example.b07project;

public interface LoginCallback {
    interface AuthenticateAdminCallback {
        void authenticateAdminCallback();
    }
    interface AuthenticateUserCallback {
        void authenticateUserCallback();
    }
}
