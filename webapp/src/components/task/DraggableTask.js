import React, { useState } from "react";
import { useDraggable } from "@dnd-kit/core";
import { Avatar, Box, Paper, Typography } from "@mui/material";
import WarningIcon from "@mui/icons-material/Warning";
import TaskDetailsModal from "./TaskDetailsModal";

const DraggableTask = ({ id, task, contacts, onUpdateTask, onUpdateBoard }) => {
    const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({ id });
    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleOpenModal = (e) => {
        e.stopPropagation();
        setIsModalOpen(true);
    };

    const getInitials = (name) => {
        const parts = name.split(" ");
        return parts.map((part) => part.charAt(0)).join("").toUpperCase();
    };

    const style = {
        transform: transform
            ? `translate3d(${transform.x}px, ${transform.y}px, 0)`
            : undefined,
        zIndex: isDragging ? 1000 : "auto",
        backgroundColor: "#fff",
        cursor: "grab",
        padding: "8px",
        borderRadius: "4px",
        border: "1px solid #ddd",
    };

    return (
        <>
            <Paper
                ref={setNodeRef}
                {...listeners}
                {...attributes}
                sx={{
                    ...style,
                    marginBottom: "8px",
                }}
            >
                <Box>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <Typography
                            variant="body1"
                            sx={{
                                cursor: "pointer",
                                textDecoration: "underline",
                                transition: "color 0.3s ease",
                                "&:hover": {
                                    color: "#1976d2",
                                },
                            }}
                            onClick={handleOpenModal}
                            onPointerDown={(e) => e.stopPropagation()}
                        >
                            {task.description}
                        </Typography>
                        {new Date(task.dueDate) < new Date() && (
                            <WarningIcon sx={{ color: "#f44336", fontSize: "1.2rem" }} />
                        )}
                    </Box>
                    <Typography variant="caption" sx={{ color: "textSecondary" }}>
                        Due: {new Date(task.dueDate).toLocaleDateString()}
                    </Typography>
                    <Box sx={{ display: "flex", alignItems: "center", marginTop: 1 }}>
                        {task.contact ? (
                            <>
                                <Avatar
                                    sx={{
                                        width: 26,
                                        height: 26,
                                        bgcolor: "#1976d2",
                                        color: "#fff",
                                        marginRight: "6px",
                                        fontSize: "0.75rem",
                                    }}
                                >
                                    {task.contact
                                        ? getInitials(`${task.contact.firstName} ${task.contact.lastName}`)
                                        : "?"}
                                </Avatar>
                                <Typography variant="body2">
                                    {task.contact.firstName} {task.contact.lastName}
                                </Typography>
                            </>
                        ) : (
                            <Typography variant="body2" sx={{ color: "#1976d2" }}>
                                Assign Contact
                            </Typography>
                        )}
                    </Box>
                </Box>
            </Paper>

            <TaskDetailsModal
                open={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    onUpdateBoard();
                }}
                task={task}
                contacts={contacts}
                onUpdateTask={onUpdateTask}
            />
        </>
    );
};

export default DraggableTask;
