import API from "./axiosConfig";

export const fetchClients = async () => {
    const response = await API.get("/client");
    return response.data;
};


export const fetchClientsForUser = async (id) => {
    const response = await API.get(`/client/user/${id}`);
    return response.data;
};

export const searchClients = async (searchTerm) => {
    const response = await API.get("/client/search", { params: { searchTerm } });
    return response.data;
};

export const createClient = async (clientDTO) => {
    const response = await API.put("/client", clientDTO);
    return response.data;
};

export const updateClient = async (id, clientDTO) => {
    const response = await API.put(`/client/${id}`, clientDTO);
    return response.data;
};

export const deleteClient = async (id) => {
    const response = await API.delete(`/client/${id}`);
    return response.data;
};
