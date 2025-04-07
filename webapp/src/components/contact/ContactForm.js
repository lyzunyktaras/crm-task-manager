import React, { useState, useEffect } from "react";
import { fetchClients } from "../../api/client";
import { createContact, updateContact } from "../../api/contact";
import {
    TextField,
    Button,
    Box,
    MenuItem,
    Typography,
    FormControl,
    Grid,
    Paper,
} from "@mui/material";

const ContactForm = ({ contact, onSuccess, onCancel }) => {
    const [clients, setClients] = useState([]);
    const [firstName, setFirstName] = useState(contact?.firstName || "");
    const [lastName, setLastName] = useState(contact?.lastName || "");
    const [email, setEmail] = useState(contact?.email || "");
    const [phoneNumber, setPhoneNumber] = useState(contact?.phoneNumber || "");
    const [clientId, setClientId] = useState(contact?.client?.id || "");
    const [errors] = useState({});

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

    const handleSubmit = async (e) => {
        e.preventDefault();

        const contactDTO = {
            id: contact?.id || undefined,
            firstName,
            lastName,
            email,
            phoneNumber,
            client: {
                id: clientId,
                companyName: clients.find((client) => client.id === clientId)?.companyName || "",
                industry: clients.find((client) => client.id === clientId)?.industry || "",
                address: clients.find((client) => client.id === clientId)?.address || "",
            },
        };

        try {
            if (contact) {
                await updateContact(contact.id, contactDTO);
            } else {
                await createContact(contactDTO);
            }
            onSuccess();
        } catch (error) {
            console.error("Failed to save contact:", error);
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
                    {contact ? "Edit Contact" : "Create Contact"}
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="First Name"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            required
                            fullWidth
                            error={!!errors.firstName}
                            helperText={errors.firstName}
                        />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Last Name"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            required
                            fullWidth
                            error={!!errors.lastName}
                            helperText={errors.lastName}
                        />
                    </Grid>
                </Grid>
                <TextField
                    label="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    type="email"
                    required
                    fullWidth
                    error={!!errors.email}
                    helperText={errors.email}
                />
                <TextField
                    label="Phone Number"
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    type="tel"
                    required
                    fullWidth
                    error={!!errors.phoneNumber}
                    helperText={errors.phoneNumber}
                />
                <FormControl fullWidth>
                    <TextField
                        select
                        label="Assign to Client"
                        value={clientId}
                        onChange={(e) => setClientId(e.target.value)}
                        required
                        error={!!errors.client}
                        helperText={errors.client}
                    >
                        {clients.map((client) => (
                            <MenuItem key={client.id} value={client.id}>
                                {client.companyName}
                            </MenuItem>
                        ))}
                    </TextField>
                </FormControl>

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

export default ContactForm;
