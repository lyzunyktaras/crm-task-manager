import React, { useState } from "react";
import { TextField, Button, Box, Typography } from "@mui/material";

const LoginForm = ({ onLogin }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setError("");
            await onLogin(username, password);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                height: "100vh",
                backgroundColor: "background.default",
            }}
        >
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{
                    width: "300px",
                    padding: 4,
                    boxShadow: 3,
                    borderRadius: 2,
                    backgroundColor: "#fff",
                }}
            >
                <Typography variant="h5" gutterBottom>
                    Login
                </Typography>
                {error && (
                    <Typography color="error" variant="body2" gutterBottom>
                        {error}
                    </Typography>
                )}
                <TextField
                    label="Username"
                    fullWidth
                    margin="normal"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <TextField
                    label="Password"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ marginTop: 2 }}
                >
                    Login
                </Button>
            </Box>
        </Box>
    );
};

export default LoginForm;
