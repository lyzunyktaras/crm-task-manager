import API from "./axiosConfig";

export const fetchUsers = async () => {
    const response = await API.get("/user");
    return response.data;
};

export const createUser = async (userDTO) => {
    const response = await API.post("/user", userDTO);
    return response.data;
};

export const updateUser = async (id, userDTO) => {
    const response = await API.put(`/user/${id}`, userDTO);
    return response.data;
};

export const subscribeClient = async (userId, clientId) => {
    const response = await API.put(`/user/subscribe/${userId}`, null, {
        params: { clientId },
    });
    return response.data;
};

export const deleteUser = async (id) => {
    const response = await API.delete(`/user/${id}`);
    return response.data;
};
