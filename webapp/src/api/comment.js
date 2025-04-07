import API from "./axiosConfig";

export const fetchCommentsForTask = async (taskId) => {
    const response = await API.get(`/comments/${taskId}`);
    return response.data;
};

export const addCommentToTask = async (taskId, commentDTO) => {
    const response = await API.post(`/comments/${taskId}`, commentDTO);
    return response.data;
};

