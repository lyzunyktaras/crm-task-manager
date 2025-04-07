import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

export const login = async (username, password) => {
    try {
        const response = await axios.post(`${API_URL}/login`, {
            username,
            password,
        });
        return response.data;
    } catch (error) {
        if (error.response && error.response.status === 401) {
            const { exceptionMessage } = error.response.data;
            throw new Error(exceptionMessage || "Unauthorized");
        } else {
            throw new Error("An unexpected error occurred. Please try again later.");
        }
    }
};
