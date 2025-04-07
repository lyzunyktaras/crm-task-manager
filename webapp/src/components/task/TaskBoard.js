import React, { useEffect, useState } from "react";
import { closestCenter, DndContext } from "@dnd-kit/core";
import { Box, Grid, Typography, TextField, MenuItem } from "@mui/material";
import { fetchTasksByClient, updateTaskStatus } from "../../api/task";
import { searchContacts } from "../../api/contact";
import DroppableColumn from "./DroppableColumn";
import DraggableTask from "./DraggableTask";

const TaskBoard = ({ clientId }) => {
    const [tasks, setTasks] = useState({ TODO: [], IN_PROGRESS: [], DONE: [] });
    const [contacts, setContacts] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filterContact, setFilterContact] = useState("");
    const [filterDeadline, setFilterDeadline] = useState("");

    useEffect(() => {
        const loadTasks = async () => {
            try {
                const fetchedTasks = await fetchTasksByClient(clientId);
                setTasks(transformTasksToBoard(fetchedTasks));
            } catch (error) {
                console.error("Failed to fetch tasks:", error);
            }
        };

        const loadContacts = async () => {
            try {
                const fetchedContacts = await searchContacts(clientId, "");
                setContacts(fetchedContacts);
            } catch (error) {
                console.error("Failed to fetch contacts:", error);
            }
        };

        if (clientId) {
            loadTasks();
            loadContacts();
        }
    }, [clientId]);

    const transformTasksToBoard = (tasks) => {
        const board = { TODO: [], IN_PROGRESS: [], DONE: [] };
        tasks.forEach((task) => {
            board[task.status].push(task);
        });
        return board;
    };

    const handleDragEnd = async (event) => {
        const { active, over } = event;

        if (!over) return;

        const sourceColumn = Object.keys(tasks).find((key) =>
            tasks[key].some((task) => task.id === active.id)
        );

        const destinationColumn = over.id;

        if (sourceColumn !== destinationColumn) {
            const task = tasks[sourceColumn].find((t) => t.id === active.id);

            const updatedSource = tasks[sourceColumn].filter((t) => t.id !== active.id);
            const updatedDestination = [...tasks[destinationColumn], task];

            setTasks({
                ...tasks,
                [sourceColumn]: updatedSource,
                [destinationColumn]: updatedDestination,
            });

            await updateTaskStatus(active.id, destinationColumn);
        }
    };

    const filteredTasks = (tasks) => {
        return Object.entries(tasks).reduce((result, [status, taskList]) => {
            const filtered = taskList.filter((task) => {
                const matchesSearchTerm =
                    !searchTerm || task.description.toLowerCase().includes(searchTerm.toLowerCase());
                const matchesContact =
                    !filterContact || (task.contact && task.contact.id === parseInt(filterContact, 10));
                const isOverdue =
                    filterDeadline === "overdue" &&
                    new Date(task.dueDate) < new Date() &&
                    task.status !== "DONE";
                const isNotOverdue =
                    filterDeadline === "notOverdue" &&
                    (new Date(task.dueDate) >= new Date() || task.status === "DONE");

                const matchesDeadline =
                    !filterDeadline || (filterDeadline === "overdue" ? isOverdue : isNotOverdue);

                return matchesSearchTerm && matchesContact && matchesDeadline;
            });

            return { ...result, [status]: filtered };
        }, {});
    };

    const displayedTasks = filteredTasks(tasks);

    return (
        <Box>
            <Typography variant="h5" gutterBottom>
                Tasks for Client ID: {clientId}
            </Typography>

            <Box sx={{ display: "flex", gap: 2, marginBottom: 5 }}>
                <TextField
                    label="Search tasks"
                    variant="outlined"
                    size="medium"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    sx={{ minWidth: 300 }}
                />
                <TextField
                    select
                    label="Filter by Contact"
                    variant="outlined"
                    size="medium"
                    value={filterContact}
                    onChange={(e) => setFilterContact(e.target.value)}
                    sx={{ minWidth: 250 }}
                >
                    <MenuItem value="">All Contacts</MenuItem>
                    {contacts.map((contact) => (
                        <MenuItem key={contact.id} value={contact.id}>
                            {contact.firstName} {contact.lastName}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    select
                    label="Filter by Deadline"
                    variant="outlined"
                    size="medium"
                    value={filterDeadline}
                    onChange={(e) => setFilterDeadline(e.target.value)}
                    sx={{ minWidth: 250 }}
                >
                    <MenuItem value="">All Tasks</MenuItem>
                    <MenuItem value="overdue">Overdue</MenuItem>
                    <MenuItem value="notOverdue">Not Overdue</MenuItem>
                </TextField>
            </Box>

            <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
                <Grid container spacing={2}>
                    {Object.entries(displayedTasks).map(([status, taskList]) => (
                        <DroppableColumn
                            key={status}
                            id={status}
                            title={status}
                            clientId={clientId}
                            contacts={contacts}
                            onTaskCreated={(newTask) => {
                                setTasks((prev) => ({
                                    ...prev,
                                    [newTask.status]: [...prev[newTask.status], newTask],
                                }));
                            }}
                        >
                            {taskList.map((task) => (
                                <DraggableTask
                                    key={task.id}
                                    id={task.id}
                                    task={task}
                                    contacts={contacts}
                                    onUpdateTask={(updatedTask) => {
                                        setTasks((prev) => {
                                            const updatedTasks = { ...prev };
                                            const sourceStatus = Object.keys(prev).find((key) =>
                                                prev[key].some((t) => t.id === updatedTask.id)
                                            );
                                            updatedTasks[sourceStatus] = updatedTasks[sourceStatus].map((t) =>
                                                t.id === updatedTask.id ? updatedTask : t
                                            );
                                            return updatedTasks;
                                        });
                                    }}
                                    onUpdateBoard={() => {
                                        const loadTasks = async () => {
                                            try {
                                                const fetchedTasks = await fetchTasksByClient(clientId);
                                                setTasks(transformTasksToBoard(fetchedTasks));
                                            } catch (error) {
                                                console.error("Failed to fetch tasks:", error);
                                            }
                                        };
                                        loadTasks();
                                    }}
                                />

                            ))}
                        </DroppableColumn>

                    ))}
                </Grid>
            </DndContext>
        </Box>
    );
};

export default TaskBoard;
