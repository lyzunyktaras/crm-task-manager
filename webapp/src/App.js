import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Navbar from "./components/Navbar";
import LoginPage from "./pages/LoginPage";
import Dashboard from "./pages/Dashboard";
import ClientPage from "./pages/ClientPage";
import ContactPage from "./pages/ContactPage";
import TaskPage from "./pages/TaskPage";
import ProtectedRoute from "./components/ProtectedRoute";
import {CssBaseline, ThemeProvider} from "@mui/material";
import theme from "./theme/theme";
import {SnackbarProvider} from "./context/SnackbarContext";
import {AuthProvider, useAuth} from "./context/AuthContext";
import UserPage from "./pages/UserPage";

const AppContent = () => {
    const {isAuthenticated} = useAuth();
    return (
        <>
            {isAuthenticated && <Navbar/>}
            <Routes>
                <Route path="/" element={<LoginPage/>}/>
                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Dashboard/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/clients"
                    element={
                        <ProtectedRoute>
                            <ClientPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/contacts"
                    element={
                        <ProtectedRoute>
                            <ContactPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/tasks"
                    element={
                        <ProtectedRoute>
                            <TaskPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/users"
                    element={
                        <ProtectedRoute>
                            <UserPage/>
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </>
    );
};

const App = () => (
    <ThemeProvider theme={theme}>
        <SnackbarProvider>
            <AuthProvider>
                <CssBaseline/>
                <Router>
                    <AppContent/>
                </Router>
            </AuthProvider>
        </SnackbarProvider>
    </ThemeProvider>
);

export default App;
