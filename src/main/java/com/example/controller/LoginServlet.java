package com.example.controller;

import com.example.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final String CLIENT_ID = "your-client-id";
    private static final String CLIENT_SECRET = "your-client-secret";
    private static final String TENANT_ID = "your-tenant-id";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.getWriter().write("Username and Password are required.");
            return;
        }

        try {
            // Authenticate using Microsoft Entra ID
            String tokenResponse = TokenUtils.getTokenFromAzureAD(username, password, CLIENT_ID, CLIENT_SECRET, TENANT_ID);

            if (tokenResponse.contains("access_token")) {
                // Handle successful authentication
                request.getSession().setAttribute("username", username);
                response.getWriter().write("Login successful! Tokens: " + tokenResponse);
            } else {
                // Handle failed authentication
                response.getWriter().write("Login failed. Response: " + tokenResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}

