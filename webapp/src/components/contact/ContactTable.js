import React, {useEffect, useState} from "react";
import {deleteContact, fetchContacts, searchContacts} from "../../api/contact";
import {fetchClients} from "../../api/client";
import {
    Box,
    Button,
    Dialog,
    IconButton,
    MenuItem,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Tooltip,
    Typography,
} from "@mui/material";
import {Add, Delete, Edit, Search} from "@mui/icons-material";
import ContactForm from "./ContactForm";

const drawerWidth = 240;

const ContactTable = () => {
    const [contacts, setContacts] = useState([]);
    const [clients, setClients] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedClient, setSelectedClient] = useState("");
    const [openDialog, setOpenDialog] = useState(false);
    const [editingContact, setEditingContact] = useState(null);

    useEffect(() => {
        const loadClients = async () => {
            try {
                const data = await fetchClients();
                setClients(data);
            } catch (error) {
                console.error("Failed to fetch clients:", error);
            }
        };

        const loadContacts = async () => {
            try {
                const data = await fetchContacts();
                setContacts(data);
            } catch (error) {
                console.error("Failed to fetch contacts:", error);
            }
        };

        loadClients();
        loadContacts();
    }, []);

    useEffect(() => {
        if (searchTerm || selectedClient) {
            handleSearch(selectedClient, searchTerm);
        } else {
            const loadContacts = async () => {
                try {
                    const data = await fetchContacts();
                    setContacts(data);
                } catch (error) {
                    console.error("Failed to fetch contacts:", error);
                }
            };
            loadContacts();
        }
    }, [searchTerm, selectedClient]);

    const handleDelete = async (id) => {
        try {
            await deleteContact(id);
            setContacts((prev) => prev.filter((contact) => contact.id !== id));
        } catch (error) {
            console.error("Failed to delete contact:", error);
        }
    };

    const handleOpenDialog = (contact = null) => {
        setEditingContact(contact);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setEditingContact(null);
        setOpenDialog(false);
    };

    const handleFormSuccess = async () => {
        try {
            const data = await fetchContacts();
            setContacts(data);
        } catch (error) {
            console.error("Failed to reload contacts:", error);
        }
        handleCloseDialog();
    };


    const handleSearch = async (clientId, term) => {
        try {
            const data = await searchContacts(clientId, term);
            setContacts(data);
        } catch (error) {
            console.error("Failed to search contacts:", error);
        }
    };

    return (
        <Box
            sx={{
                padding: 4,
                marginLeft: `${drawerWidth}px`,
            }}
        >
            <Typography variant="h4" component="h1" gutterBottom>
                Contact Management
            </Typography>

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginBottom: 2,
                }}
            >
                <TextField
                    label="Search contacts"
                    variant="outlined"
                    size="small"
                    fullWidth
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    InputProps={{
                        startAdornment: <Search sx={{marginRight: 1}}/>,
                    }}
                    sx={{marginRight: 2}}
                />
                <TextField
                    select
                    label="Filter by Client"
                    value={selectedClient}
                    onChange={(e) => setSelectedClient(e.target.value)}
                    variant="outlined"
                    size="small"
                    sx={{width: 200, marginRight: 2}}
                >
                    <MenuItem value="">All Clients</MenuItem>
                    {clients.map((client) => (
                        <MenuItem key={client.id} value={client.id}>
                            {client.companyName}
                        </MenuItem>
                    ))}
                </TextField>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<Add/>}
                    onClick={() => handleOpenDialog()}
                >
                    Add Contact
                </Button>
            </Box>

            <Paper elevation={3}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>First Name</TableCell>
                            <TableCell>Last Name</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Phone</TableCell>
                            <TableCell>Client</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {contacts.map((contact) => (
                            <TableRow key={contact.id} hover>
                                <TableCell>{contact.id}</TableCell>
                                <TableCell>{contact.firstName}</TableCell>
                                <TableCell>{contact.lastName}</TableCell>
                                <TableCell>{contact.email}</TableCell>
                                <TableCell>{contact.phoneNumber}</TableCell>
                                <TableCell>{contact.client?.companyName || "N/A"}</TableCell>
                                <TableCell align="right">
                                    <Tooltip title="Edit">
                                        <IconButton
                                            color="primary"
                                            onClick={() => handleOpenDialog(contact)}
                                        >
                                            <Edit/>
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Delete">
                                        <IconButton
                                            color="error"
                                            onClick={() => handleDelete(contact.id)}
                                        >
                                            <Delete/>
                                        </IconButton>
                                    </Tooltip>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>

            <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="sm">
                <ContactForm
                    contact={editingContact}
                    onSuccess={handleFormSuccess}
                    onCancel={handleCloseDialog}
                />
            </Dialog>
        </Box>
    );
};

export default ContactTable;
