import React, {useEffect, useState} from "react";
import {deleteUser, fetchUsers, subscribeClient,} from "../../api/user";
import {fetchClients, fetchClientsForUser} from "../../api/client";
import {
    Box,
    Button,
    Dialog,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Tooltip,
    Typography,
} from "@mui/material";
import {Add, Delete, Edit, PersonAdd, Search} from "@mui/icons-material";
import UserForm from "./UserForm";

const drawerWidth = 240;

const UserTable = () => {
    const [users, setUsers] = useState([]);
    const [clients, setClients] = useState([]);
    const [userClients, setUserClients] = useState({});
    const [searchTerm, setSearchTerm] = useState("");
    const [openDialog, setOpenDialog] = useState(false);
    const [openSubscribeDialog, setOpenSubscribeDialog] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [selectedClientId, setSelectedClientId] = useState("");

    useEffect(() => {
        loadUsers();
        loadClients();
    }, []);

    const loadUsers = async () => {
        try {
            const data = await fetchUsers();
            setUsers(data);
            await loadUserClients(data);
        } catch (error) {
            console.error("Failed to fetch users:", error);
        }
    };

    const loadClients = async () => {
        try {
            const data = await fetchClients();
            setClients(data);
        } catch (error) {
            console.error("Failed to fetch clients:", error);
        }
    };

    const loadUserClients = async (users) => {
        try {
            const clientsMap = {};
            for (const user of users) {
                clientsMap[user.id] = await fetchClientsForUser(user.id);
            }
            setUserClients(clientsMap);
        } catch (error) {
            console.error("Failed to fetch clients for users:", error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteUser(id);
            setUsers((prev) => prev.filter((user) => user.id !== id));
        } catch (error) {
            console.error("Failed to delete user:", error);
        }
    };

    const handleOpenDialog = (user = null) => {
        setEditingUser(user);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setEditingUser(null);
        setOpenDialog(false);
    };

    const handleFormSuccess = async () => {
        await loadUsers();
        handleCloseDialog();
    };

    const handleOpenSubscribeDialog = (userId) => {
        setSelectedUserId(userId);
        setOpenSubscribeDialog(true);
    };

    const handleSubscribe = async () => {
        try {
            if (selectedUserId && selectedClientId) {
                await subscribeClient(selectedUserId, selectedClientId);
                await loadUsers();
                setOpenSubscribeDialog(false);
                setSelectedClientId("");
                setSelectedUserId(null);
            }
        } catch (error) {
            console.error("Failed to subscribe client:", error);
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
                User Management
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
                    label="Search users"
                    variant="outlined"
                    size="small"
                    fullWidth
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    InputProps={{
                        startAdornment: <Search sx={{ marginRight: 1 }} />,
                    }}
                    sx={{ marginRight: 2 }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<Add />}
                    onClick={() => handleOpenDialog()}
                >
                    Add User
                </Button>
            </Box>

            <Paper elevation={3}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Username</TableCell>
                            <TableCell>Clients</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id} hover>
                                <TableCell>{user.id}</TableCell>
                                <TableCell>{user.username}</TableCell>
                                <TableCell>
                                    {userClients[user.id]?.map((client) => client.companyName).join(", ") || "No clients"}
                                </TableCell>
                                <TableCell align="right">
                                    <Tooltip title="Edit">
                                        <IconButton
                                            color="primary"
                                            onClick={() => handleOpenDialog(user)}
                                        >
                                            <Edit />
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Subscribe to Client">
                                        <IconButton
                                            color="info"
                                            onClick={() => handleOpenSubscribeDialog(user.id)}
                                        >
                                            <PersonAdd />
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Delete">
                                        <IconButton
                                            color="error"
                                            onClick={() => handleDelete(user.id)}
                                        >
                                            <Delete />
                                        </IconButton>
                                    </Tooltip>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>

            <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="sm">
                <UserForm
                    user={editingUser}
                    onSuccess={handleFormSuccess}
                    onCancel={handleCloseDialog}
                />
            </Dialog>

            <Dialog
                open={openSubscribeDialog}
                onClose={() => setOpenSubscribeDialog(false)}
                fullWidth
                maxWidth="sm"
            >
                <Box sx={{ padding: 3 }}>
                    <Typography variant="h6" gutterBottom>
                        Subscribe User to Client
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="select-client-label">Select Client</InputLabel>
                        <Select
                            labelId="select-client-label"
                            value={selectedClientId}
                            onChange={(e) => setSelectedClientId(e.target.value)}
                        >
                            {clients.map((client) => (
                                <MenuItem key={client.id} value={client.id}>
                                    {client.companyName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Box sx={{ display: "flex", justifyContent: "flex-end", mt: 3 }}>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleSubscribe}
                            disabled={!selectedClientId}
                        >
                            Subscribe
                        </Button>
                        <Button
                            variant="outlined"
                            color="secondary"
                            onClick={() => setOpenSubscribeDialog(false)}
                            sx={{ ml: 2 }}
                        >
                            Cancel
                        </Button>
                    </Box>
                </Box>
            </Dialog>
        </Box>
    );
};

export default UserTable;
