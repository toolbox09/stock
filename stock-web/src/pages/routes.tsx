import { Routes, Route } from "react-router-dom";

export default function RootRoutes() {
  return (
    <Routes>
      <Route element={
        <SigninProvider>
          <MainLayout/>
        </SigninProvider>
        } >
        <Route index element={<Dashboard />} />
        <Route path="about" element={<>about</>} />
      </Route>
    </Routes>
  )
}

/*

*/