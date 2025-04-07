import React, { useState } from "react";
import { createUser, updateUser } from "../../api/user";
import {
    TextField,
    Button,
    Box,
    Typography,
    Paper,
    Grid,
} from "@mui/material";

const UserForm = ({ user, onSuccess, onCancel }) => {
    const [username, setUsername] = useState(user?.username || "");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userDTO = {
            username,
            password: user ? undefined : password,
        };

        try {
            if (user) {
                await updateUser(user.id, userDTO);
            } else {
                await createUser(userDTO);
            }
            onSuccess();
        } catch (error) {
            console.error("Failed to save user:", error);
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
                    {user ? "Edit User" : "Create User"}
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            label="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            fullWidth
                        />
                    </Grid>
                    {!user && (
                        <Grid item xs={12}>
                            <TextField
                                label="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                type="password"
                                required
                                fullWidth
                            />
                        </Grid>
                    )}
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

export default UserForm;
