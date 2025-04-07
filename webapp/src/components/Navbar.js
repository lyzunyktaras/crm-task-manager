import React from "react";
import {
    AppBar,
    Box,
    Button,
    CssBaseline,
    Drawer,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";
import {Business, Home, Person, TaskAlt, Group} from "@mui/icons-material";
import {useNavigate} from "react-router-dom";
import {useSnackbar} from "../context/SnackbarContext";
import {useAuth} from "../context/AuthContext";
import NotificationMenu from "./notification/NotificationMenu";

const drawerWidth = 240;

const Navbar = () => {
    const navigate = useNavigate();
    const showSnackbar = useSnackbar();
    const {isAuthenticated, logout} = useAuth();

    const menuItems = [
        {text: "Dashboard", icon: <Home/>, path: "/dashboard"},
        {text: "Clients", icon: <Business/>, path: "/clients"},
        {text: "Contacts", icon: <Person/>, path: "/contacts"},
        {text: "Tasks", icon: <TaskAlt/>, path: "/tasks"},
        {text: "Users", icon: <Group/>, path: "/users"},
    ];

    const handleLogout = () => {
        logout();
        showSnackbar("You have logged out.", "info");
        navigate("/");
    };

    if (!isAuthenticated) return null;

    return (
        <Box sx={{display: "flex"}}>
            <CssBaseline/>
            <AppBar
                position="fixed"
                sx={{
                    width: `calc(100% - ${drawerWidth}px)`,
                    ml: `${drawerWidth}px`,
                }}
            >
                <Toolbar>
                    <Typography variant="h6" noWrap component="div">
                        CRM System
                    </Typography>
                    <Box sx={{ml: "auto", display: "flex", alignItems: "center", gap: 2}}>
                        <NotificationMenu username={localStorage.getItem("username")}/>
                        <Button
                            variant="outlined"
                            sx={{
                                color: "white",
                                borderColor: "white",
                                "&:hover": {
                                    backgroundColor: "rgba(255, 255, 255, 0.2)",
                                },
                            }}
                            onClick={handleLogout}
                        >
                            Logout
                        </Button>
                    </Box>
                </Toolbar>
            </AppBar>
            <Drawer
                variant="permanent"
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    [`& .MuiDrawer-paper`]: {
                        width: drawerWidth,
                        boxSizing: "border-box",
                    },
                }}
            >
                <Toolbar/>
                <Box sx={{overflow: "auto"}}>
                    <List>
                        {menuItems.map((item) => (
                            <ListItem
                                key={item.text}
                                disablePadding
                                onClick={() => navigate(item.path)}
                            >
                                <ListItemButton>
                                    <ListItemIcon>{item.icon}</ListItemIcon>
                                    <ListItemText primary={item.text}/>
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Box>
            </Drawer>
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    bgcolor: "background.default",
                    marginLeft: `${drawerWidth}px`,
                }}
            >
                <Toolbar/>
            </Box>
        </Box>
    );
};

export default Navbar;
