import React, { useState, useEffect } from "react";
import { Badge, Box, IconButton, Menu, MenuItem, Typography, Avatar, Button } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import webSocketService from "../../websocket/WebSocketService";
import {dismissNotification, fetchNotificationsForUser, updateNotification} from "../../api/notification";

const NotificationMenu = ({ username }) => {
    const [notifications, setNotifications] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);

    useEffect(() => {
        const loadNotifications = async () => {
            try {
                const data = await fetchNotificationsForUser(username);
                setNotifications(data);
            } catch (error) {
                console.error("Failed to fetch notifications:", error);
            }
        };

        loadNotifications();

        const handleNewNotification = (notification) => {
            setNotifications((prev) => [...prev, notification]);
        };

        webSocketService.connect(handleNewNotification);

        return () => {
            webSocketService.disconnect();
        };
    }, [username]);

    const generateNotificationText = (type, params) => {
        switch (type) {
            case "TASK_STATUS_CHANGED":
                return `Task "${params.task_description}" status has been changed to "${params.task_status}".`;

            case "TASK_NEW_COMMENT":
                return `New comment ontTask "${params.task_description}".`;

            case "TASK_DUE_DATE":
                return `Task "${params.task_description}" has a new due date: ${params.task_due_date}.`;

            default:
                return "You have a new notification.";
        }
    };

    const handleMenuOpen = async (event) => {
        setAnchorEl(event.currentTarget);

        const updatedNotifications = notifications.map((notification) =>
            !notification.viewed
                ? { ...notification, viewed: true }
                : notification
        );
        setNotifications(updatedNotifications);

        const unreadNotifications = notifications.filter((notification) => !notification.viewed);
        await Promise.all(
            unreadNotifications.map((notification) =>
                updateNotification(notification.id, { ...notification, viewed: true })
            )
        );
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const dismiss = async (id) => {
        const updatedNotifications = notifications.map((notification) =>
            notification.id === id
                ? { ...notification, dismissed: true }
                : notification
        );
        setNotifications(updatedNotifications);

        await dismissNotification(id, { dismissed: true });
    };

    const renderNotification = (notification) => {
        const { id, type, params, dismissed } = notification;

        if (dismissed) return null;

        const notificationText = generateNotificationText(type, params);

        return (
            <MenuItem key={id}>
                <Avatar sx={{ bgcolor: "#1976d2", marginRight: 2 }}>{type[0]}</Avatar>
                <Box
                    sx={{
                        flex: 1,
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "normal",
                        wordWrap: "break-word",
                    }}
                >
                    <Typography
                        variant="body2"
                        color="text.primary"
                        sx={{
                            whiteSpace: "normal",
                            wordWrap: "break-word",
                            overflow: "hidden",
                        }}
                    >
                        {notificationText}
                    </Typography>
                </Box>

                <Button size="small" onClick={() => dismiss(id)}>
                    Dismiss
                </Button>
            </MenuItem>
        );
    };

    return (
        <div>
            <IconButton onClick={handleMenuOpen}>
                <Badge
                    badgeContent={notifications.filter((notification) => !notification.viewed).length}
                    color="error"
                >
                    <NotificationsIcon />
                </Badge>
            </IconButton>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                PaperProps={{
                    style: { maxHeight: 300, width: "350px" },
                }}
            >
                <Box sx={{ padding: 2, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography sx={{ fontWeight: "bold" }}>Notifications</Typography>
                </Box>
                {notifications.length > 0 ? (
                    notifications
                        .filter((notification) => !notification.dismissed)
                        .map((notification) => renderNotification(notification))
                ) : (
                    <Typography sx={{ padding: 2 }}>No notifications</Typography>
                )}
            </Menu>
        </div>
    );
};

export default NotificationMenu;


