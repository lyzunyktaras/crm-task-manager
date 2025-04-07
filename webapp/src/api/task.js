import API from "./axiosConfig";

export const fetchTasks = async () => {
    const response = await API.get("/task");
    return response.data;
};

export const fetchTasksByClient = async (clientId) => {
    const response = await API.get(`/task/client/${clientId}`);
    return response.data;
};

export const searchTasks = async (searchTerm) => {
    const response = await API.get("/task/search", { params: { searchTerm } });
    return response.data;
};

export const createTask = async (taskDTO) => {
    const response = await API.post("/task", taskDTO);
    return response.data;
};

export const updateTask = async (id, taskDTO) => {
    const response = await API.put(`/task/${id}`, taskDTO);
    return response.data;
};

export const deleteTask = async (id) => {
    const response = await API.delete(`/task/${id}`);
    return response.data;
};

export const updateTaskStatus = async (id, status) => {
    const response = await API.patch(`/task/status/${id}`, null, { params: { status } });
    return response.data;
};

export const assignTaskToContact = async (taskId, contactId) => {
    const response = await API.patch(`/task/assign/${taskId}`, null, { params: { contact: contactId } });
    return response.data;
};
