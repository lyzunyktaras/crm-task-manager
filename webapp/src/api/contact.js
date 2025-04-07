import API from "./axiosConfig";

export const fetchContacts = async () => {
    const response = await API.get("/contact");
    return response.data;
};

export const searchContacts = async (clientId, searchTerm) => {
    const response = await API.get("/contact/search", { params: { clientId, searchTerm } });
    return response.data;
};

export const createContact = async (contactDTO) => {
    const response = await API.post("/contact", contactDTO);
    return response.data;
};

export const updateContact = async (id, contactDTO) => {
    const response = await API.put(`/contact/${id}`, contactDTO);
    return response.data;
};

export const deleteContact = async (id) => {
    const response = await API.delete(`/contact/${id}`);
    return response.data;
};
