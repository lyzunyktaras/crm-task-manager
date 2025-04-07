import React, {useEffect, useState} from "react";
import {deleteClient, fetchClients, searchClients} from "../../api/client";
import {
    Box,
    Button,
    Dialog,
    IconButton,
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
import ClientForm from "./ClientForm";

const drawerWidth = 240;

const ClientTable = () => {
    const [clients, setClients] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [openDialog, setOpenDialog] = useState(false);
    const [editingClient, setEditingClient] = useState(null);

    useEffect(() => {
        const loadClients = async () => {
            try {
                const data = await fetchClients();
                setClients(data);
            } catch (error) {
                console.error("Failed to fetch clients:", error);
            }
        };

        loadClients();
    }, []);

    const handleSearch = async (term) => {
        try {
            const data = await searchClients(term);
            setClients(data);
        } catch (error) {
            console.error("Failed to search clients:", error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteClient(id);
            setClients((prev) => prev.filter((client) => client.id !== id));
        } catch (error) {
            console.error("Failed to delete client:", error);
        }
    };

    const handleOpenDialog = (client = null) => {
        setEditingClient(client);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setEditingClient(null);
        setOpenDialog(false);
    };

    const handleFormSuccess = async () => {
        const data = await fetchClients();
        setClients(data);
        handleCloseDialog();
    };

    useEffect(() => {
        if (searchTerm) {
            handleSearch(searchTerm);
        } else {
            const loadClients = async () => {
                try {
                    const data = await fetchClients();
                    setClients(data);
                } catch (error) {
                    console.error("Failed to fetch clients:", error);
                }
            };
            loadClients();
        }
    }, [searchTerm]);

    return (
        <Box
            sx={{
                padding: 4,
                marginLeft: `${drawerWidth}px`,
            }}
        >
            <Typography variant="h4" component="h1" gutterBottom>
                Client Management
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
                    label="Search clients"
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
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<Add/>}
                    onClick={() => handleOpenDialog()}
                >
                    Add Client
                </Button>
            </Box>

            <Paper elevation={3}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Company Name</TableCell>
                            <TableCell>Industry</TableCell>
                            <TableCell>Address</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {clients.map((client) => (
                            <TableRow key={client.id} hover>
                                <TableCell>{client.id}</TableCell>
                                <TableCell>{client.companyName}</TableCell>
                                <TableCell>{client.industry}</TableCell>
                                <TableCell>{client.address}</TableCell>
                                <TableCell align="right">
                                    <Tooltip title="Edit">
                                        <IconButton
                                            color="primary"
                                            onClick={() => handleOpenDialog(client)}
                                        >
                                            <Edit/>
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Delete">
                                        <IconButton
                                            color="error"
                                            onClick={() => handleDelete(client.id)}
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
                <ClientForm
                    client={editingClient}
                    onSuccess={handleFormSuccess}
                    onCancel={handleCloseDialog}
                />
            </Dialog>
        </Box>
    );
};

export default ClientTable;
