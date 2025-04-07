import API from "./axiosConfig";

export const fetchNotificationsForUser = async (username) => {
    const response = await API.get(`/notifications/${username}`);
    return response.data;
};

export const updateNotification = async (id, notificationDTO) => {
    const response = await API.put(`/notifications/${id}`, notificationDTO);
    return response.data;
};

export const dismissNotification = async (id) => {
    const response = await API.patch(`/notifications/${id}`);
    return response.data;
};

