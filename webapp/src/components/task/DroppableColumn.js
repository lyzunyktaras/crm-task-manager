import React, {useEffect, useRef, useState} from "react";
import {useDroppable} from "@dnd-kit/core";
import {Box, Button, MenuItem, Paper, TextField, Typography} from "@mui/material";
import {createTask} from "../../api/task";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFns";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {searchContacts} from "../../api/contact";

const DroppableColumn = ({id, title, clientId, children, onTaskCreated}) => {
    const [showInput, setShowInput] = useState(false);
    const [newTaskDescription, setNewTaskDescription] = useState("");
    const [dueDate, setDueDate] = useState(null);
    const [selectedContact, setSelectedContact] = useState("");
    const [contacts, setContacts] = useState([]);
    const formRef = useRef(null);
    const {setNodeRef} = useDroppable({id});

    useEffect(() => {
        const loadContacts = async () => {
            try {
                if (clientId) {
                    const fetchedContacts = await searchContacts(clientId, "");
                    setContacts(fetchedContacts);
                }
            } catch (error) {
                console.error("Failed to load contacts:", error);
            }
        };

        loadContacts();
    }, [clientId]);

    const handleAddTask = async () => {
        if (!newTaskDescription.trim() || !clientId) {
            console.error("Description or Client ID is missing");
            return;
        }

        try {
            const newTask = await createTask({
                description: newTaskDescription,
                status: id,
                dueDate: dueDate ? dueDate.toISOString() : null,
                client: { id: clientId },
                contact: selectedContact ? { id: selectedContact } : null,
            });

            if (selectedContact) {
                const assignedContact = contacts.find(
                    (contact) => contact.id === parseInt(selectedContact, 10)
                );
                newTask.contact = assignedContact;
            }

            onTaskCreated(newTask);

            setNewTaskDescription("");
            setDueDate(null);
            setSelectedContact("");
            setShowInput(false);

            console.log("Task created successfully:", newTask);
        } catch (error) {
            console.error("Failed to create task:", error.response?.data || error.message || error);
        }
    };



    return (
        <Paper
            ref={setNodeRef}
            sx={{
                width: "300px",
                minHeight: "300px",
                padding: 2,
                backgroundColor: "#f9f9f9",
                position: "relative",
                overflow: "visible",
                display: "flex",
                flexDirection: "column",
            }}
        >
            <Typography
                variant="h6"
                sx={{borderBottom: "1px solid #ccc", paddingBottom: 1, marginBottom: 2}}
                gutterBottom
            >
                {title.replace("_", " ")}
            </Typography>
            <Box sx={{display: "flex", flexDirection: "column", gap: 1}}>
                {children}

                {showInput ? (
                    <Box ref={formRef} sx={{display: "flex", flexDirection: "column", gap: 1}}>
                        <TextField
                            multiline
                            rows={2}
                            size="small"
                            fullWidth
                            autoFocus
                            value={newTaskDescription}
                            onChange={(e) => setNewTaskDescription(e.target.value)}
                            placeholder="Task description"
                        />
                        <TextField
                            select
                            label="Assign Contact"
                            value={selectedContact}
                            onChange={(e) => setSelectedContact(e.target.value)}
                            size="small"
                            fullWidth
                        >
                            <MenuItem value="">No Contact</MenuItem>
                            {contacts.map((contact) => (
                                <MenuItem key={contact.id} value={contact.id}>
                                    {contact.firstName} {contact.lastName}
                                </MenuItem>
                            ))}
                        </TextField>
                        <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DatePicker
                                value={dueDate}
                                onChange={(newValue) => setDueDate(newValue)}
                                renderInput={(params) => (
                                    <Button variant="outlined" {...params} size="small">
                                        {dueDate ? dueDate.toLocaleDateString() : "Set Due Date"}
                                    </Button>
                                )}
                            />
                        </LocalizationProvider>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleAddTask}
                            sx={{marginTop: 1}}
                        >
                            Add Task
                        </Button>
                    </Box>
                ) : (
                    <Typography
                        variant="body1"
                        sx={{cursor: "pointer", color: "primary.main"}}
                        onClick={() => setShowInput(true)}
                    >
                        + Add Task
                    </Typography>
                )}
            </Box>
        </Paper>
    );
};

export default DroppableColumn;
