import { Box, Stack } from "@mui/material";
import { Link } from "react-router-dom";

const NavBar = () => {
  return (
    <Box sx={{ paddingX: 2, width: "100%", position: "fixed", top: 0 }}>
      <Stack
        direction="row"
        sx={{
          justifyContent: "space-between",
          alignItems: "center",
          height: 60,
          backdropFilter: "blur(6px)",
        }}
      >
        <Stack direction="row" spacing={2}>
          <div>Eikona</div> <div>Onoma</div>
        </Stack>
        <Stack direction="row" spacing={2}>
          <Link to="/">Home</Link>
          <Link to="/about">About</Link>
        </Stack>
      </Stack>
    </Box>
  );
};

export default NavBar;
