import { createTheme } from "@mui/material/styles";

const theme = createTheme({
    palette: {
        primary: {
            main: "#1976d2",
        },
        secondary: {
            main: "#ff4081",
        },
        background: {
            default: "#f4f6f8",
        },
    },
    typography: {
        fontFamily: "Roboto, Arial, sans-serif"
    },
});

export default theme;
