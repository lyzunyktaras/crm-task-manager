import React, { useEffect, useState } from "react";
import { Box, TextField, MenuItem, Typography } from "@mui/material";
import TaskBoard from "../components/task/TaskBoard";
import { fetchClients } from "../api/client";

const drawerWidth = 240;

const TaskPage = () => {
    const [clients, setClients] = useState([]);
    const [selectedClientId, setSelectedClientId] = useState("");

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

    return (
        <Box
            sx={{
                padding: 4,
                marginLeft: `${drawerWidth}px`,
            }}
        >
            <Typography variant="h4" gutterBottom>
                Task Management
            </Typography>

            <Box sx={{ marginBottom: 4 }}>
                <TextField
                    select
                    label="Select Client"
                    variant="outlined"
                    size="medium"
                    value={selectedClientId}
                    onChange={(e) => setSelectedClientId(e.target.value)}
                    fullWidth
                >
                    <MenuItem value="">Select a Client</MenuItem>
                    {clients.map((client) => (
                        <MenuItem key={client.id} value={client.id}>
                            {client.companyName}
                        </MenuItem>
                    ))}
                </TextField>
            </Box>
            {selectedClientId ? (
                <TaskBoard clientId={selectedClientId} />
            ) : (
                <Typography variant="h6" color="textSecondary">
                    Please select a client to view tasks.
                </Typography>
            )}
        </Box>
    );
};

export default TaskPage;
