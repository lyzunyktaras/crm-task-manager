import React, { useState } from "react";
import { createClient, updateClient } from "../../api/client";
import {
    TextField,
    Button,
    Box,
    Typography,
    Paper,
    Grid,
} from "@mui/material";

const ClientForm = ({ client, onSuccess, onCancel }) => {
    const [companyName, setCompanyName] = useState(client?.companyName || "");
    const [industry, setIndustry] = useState(client?.industry || "");
    const [address, setAddress] = useState(client?.address || "");
    const [errors, setErrors] = useState({});

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newClient = {
            companyName,
            industry,
            address,
        };

        try {
            if (client) {
                await updateClient(client.id, newClient);
            } else {
                await createClient(newClient);
            }
            onSuccess();
        } catch (error) {
            if (error.response && error.response.data && error.response.data.errors) {
                setErrors(error.response.data.errors);
            } else {
                console.error("Failed to save client:", error);
            }
        }
    };

    return (
        <Paper elevation={3} sx={{ padding: 3, borderRadius: 2 }}>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{ display: "flex", flexDirection: "column", gap: 2 }}
            >
                <Typography variant="h5" align="center" gutterBottom>
                    {client ? "Edit Client" : "Create Client"}
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            label="Company Name"
                            value={companyName}
                            onChange={(e) => setCompanyName(e.target.value)}
                            required
                            fullWidth
                            error={!!errors.companyName}
                            helperText={errors.companyName}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="Industry"
                            value={industry}
                            onChange={(e) => setIndustry(e.target.value)}
                            required
                            fullWidth
                            error={!!errors.industry}
                            helperText={errors.industry}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="Address"
                            value={address}
                            onChange={(e) => setAddress(e.target.value)}
                            required
                            fullWidth
                            error={!!errors.address}
                            helperText={errors.address}
                        />
                    </Grid>
                </Grid>
                <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2 }}>
                    <Button type="submit" variant="contained" color="primary">
                        Save
                    </Button>
                    {onCancel && (
                        <Button variant="outlined" color="secondary" onClick={onCancel}>
                            Cancel
                        </Button>
                    )}
                </Box>
            </Box>
        </Paper>
    );
};

export default ClientForm;
