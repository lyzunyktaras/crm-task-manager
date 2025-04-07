import React from "react";
import LoginForm from "../components/LoginForm";
import { login } from "../api/auth";
import { useSnackbar } from "../context/SnackbarContext";

const LoginPage = () => {
    const showSnackbar = useSnackbar();

    const handleLogin = async (username, password) => {
        try {
            const token = await login(username, password);
            localStorage.setItem("token", token);
            localStorage.setItem("username", username);
            showSnackbar("Login successful!", "success");
            window.location.href = "/dashboard";
        } catch (error) {
            showSnackbar(error.message, "error");
        }
    };

    return <LoginForm onLogin={handleLogin} />;
};

export default LoginPage;
