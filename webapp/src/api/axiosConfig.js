import axios from "axios";
import { globalShowSnackbar } from "../context/SnackbarContext";

const API = axios.create({
    baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        globalShowSnackbar("Request error. Please try again.", "error");
        return Promise.reject(error);
    }
);

API.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const status = error.response.status;
            const message =
                error.response.data?.message || "Something went wrong. Please try again.";
            globalShowSnackbar(`Error ${status}: ${message}`, "error");
        } else {
            globalShowSnackbar("Network error. Please check your connection.", "error");
        }
        return Promise.reject(error);
    }
);

export default API;
