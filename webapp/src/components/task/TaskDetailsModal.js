import React, {useEffect, useState} from "react";
import {
    Avatar,
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    List,
    ListItem,
    ListItemAvatar,
    ListItemText,
    MenuItem,
    Select,
    TextField,
    Typography,
} from "@mui/material";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFns";
import {DesktopDatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {updateTask} from "../../api/task";
import webSocketService from "../../websocket/WebSocketService";
import {addCommentToTask, fetchCommentsForTask} from "../../api/comment";

const TaskDetailsModal = ({open, onClose, task, contacts, onUpdateTask}) => {
    const [editedDescription, setEditedDescription] = useState(task?.description || "");
    const [selectedContact, setSelectedContact] = useState(task?.contact?.id || "");
    const [newComment, setNewComment] = useState("");
    const [selectedStatus, setSelectedStatus] = useState(task?.status || "");
    const [dueDate, setDueDate] = useState(task?.dueDate ? new Date(task?.dueDate) : null);
    const [comments, setComments] = useState(task?.comments || []);

    useEffect(() => {
        const loadComments = async () => {
            try {
                const data = await fetchCommentsForTask(task.id);
                setComments(data);
            } catch (error) {
                console.error("Failed to load comments:", error);
            }
        };

        loadComments();

        const onCommentReceived = (comment) => {
            setComments((prevComments) => [...prevComments, comment]);
        };

        webSocketService.subscribeToTaskChat(task.id, onCommentReceived);

        return () => {
            webSocketService.unsubscribeFromTaskChat(task.id);
        };
    }, [task.id]);

    const handleSaveChanges = async () => {
        const taskDTO = {
            id: task.id,
            description: editedDescription,
            contact: contacts.find((contact) => contact.id === selectedContact),
            client: {
                id: task.client.id,
                companyName: task.client.companyName,
                address: task.client.address,
                industry: task.client.industry,
            },
            status: selectedStatus,
            dueDate: dueDate,
        };

        try {
            const updatedTask = await updateTask(task.id, taskDTO);

            if (onUpdateTask) {
                onUpdateTask(updatedTask);
            }
        } catch (error) {
            console.error("Error updating task:", error);
        }

        onClose();
    };

    const handleAddComment = async () => {
        if (!newComment.trim()) return;

        const commentDTO = {
            content: newComment,
            username: localStorage.getItem("username"),
        };

        const savedComment = await addCommentToTask(task.id, commentDTO);

        webSocketService.sendMessageToTaskChat(task.id, savedComment);

        setNewComment("");
    };

    const getInitials = (name) => {
        if (!name || typeof name !== "string") {
            return "";
        }

        const parts = name.trim().split(" ").filter((part) => part.trim().length > 0);
        if (parts.length === 1) {
            return parts[0].charAt(0).toUpperCase();
        } else if (parts.length === 2) {
            return `${parts[0].charAt(0)}${parts[1].charAt(0)}`.toUpperCase();
        } else {
            return `${parts[0].charAt(0)}${parts[parts.length - 1].charAt(0)}`.toUpperCase();
        }
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="lg" fullWidth>
            <DialogTitle>Task Details</DialogTitle>
            <DialogContent>
                <Box sx={{display: "flex", height: "500px"}}>
                    <Box sx={{flex: 2, paddingRight: 2, borderRight: "1px solid #ccc"}}>
                        <Typography variant="h6" gutterBottom>
                            Task Information
                        </Typography>
                        <TextField
                            label="Description"
                            variant="outlined"
                            fullWidth
                            value={editedDescription}
                            onChange={(e) => setEditedDescription(e.target.value)}
                            sx={{marginBottom: 2}}
                        />
                        <FormControl fullWidth sx={{marginBottom: 2}}>
                            <InputLabel>Assign Contact</InputLabel>
                            <Select
                                value={selectedContact}
                                onChange={(e) => setSelectedContact(e.target.value)}
                                label="Assign Contact"
                            >
                                {contacts.map((contact) => (
                                    <MenuItem key={contact.id} value={contact.id}>
                                        <Box sx={{display: "flex", alignItems: "center"}}>
                                            <Avatar
                                                sx={{
                                                    width: 24,
                                                    height: 24,
                                                    bgcolor: "#1976d2",
                                                    color: "#fff",
                                                    fontSize: "0.75rem",
                                                    marginRight: "8px",
                                                }}
                                            >
                                                {getInitials(contact.firstName + " " + contact.lastName)}
                                            </Avatar>
                                            <Typography variant="body2">
                                                {contact.firstName} {contact.lastName}
                                            </Typography>
                                        </Box>
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <FormControl fullWidth sx={{marginBottom: 2}}>
                            <InputLabel>Status</InputLabel>
                            <Select
                                value={selectedStatus}
                                onChange={(e) => setSelectedStatus(e.target.value)}
                                label="Status"
                            >
                                <MenuItem value="TODO">To Do</MenuItem>
                                <MenuItem value="IN_PROGRESS">In Progress</MenuItem>
                                <MenuItem value="DONE">Done</MenuItem>
                            </Select>
                        </FormControl>
                        <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DesktopDatePicker
                                label="Due Date"
                                inputFormat="MM/dd/yyyy"
                                value={dueDate}
                                onChange={(newValue) => setDueDate(newValue)}
                                renderInput={(params) => (
                                    <TextField {...params} fullWidth sx={{marginBottom: 2}}/>
                                )}
                            />
                        </LocalizationProvider>
                    </Box>

                    <Box sx={{flex: 1, paddingLeft: 2, overflowY: "auto"}}>
                        <Typography variant="h6" gutterBottom>
                            Comments (Working via WS)
                        </Typography>
                        <List sx={{maxHeight: "380px", overflowY: "auto", marginBottom: 2}}>
                            {comments.map((comment, index) => (
                                <ListItem key={index} alignItems="flex-start">
                                    <ListItemAvatar>
                                        <Avatar sx={{width: 32, height: 32}}>
                                            {getInitials(comment.username)}
                                        </Avatar>
                                    </ListItemAvatar>
                                    <ListItemText
                                        primary={
                                            <Typography variant="body2">
                                                {comment.username}
                                            </Typography>
                                        }
                                        secondary={
                                            <>
                                                <Typography variant="body2">
                                                    {comment.content}
                                                </Typography>
                                                <Typography
                                                    variant="caption"
                                                    color="textSecondary"
                                                >
                                                    {new Date(comment.sentAt).toLocaleString()}
                                                </Typography>
                                            </>
                                        }
                                    />
                                </ListItem>
                            ))}
                        </List>
                        <Box sx={{display: "flex", gap: 1}}>
                            <TextField
                                fullWidth
                                label="Add a comment"
                                variant="outlined"
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        e.preventDefault();
                                        handleAddComment();
                                    }
                                }}
                            />
                            <Button variant="contained" onClick={handleAddComment}>
                                Send
                            </Button>

                        </Box>
                    </Box>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="secondary">
                    Cancel
                </Button>
                <Button onClick={handleSaveChanges} variant="contained" color="primary">
                    Save Changes
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default TaskDetailsModal;
