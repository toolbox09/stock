import { Routes, Route } from 'react-router';

import { SigninProvider } from './(auth)/_components/SigninProvider';
import { authRoutes } from './(auth)/_routes';
// import { AuthLayout } from './(auth)/_components/AuthLayout';
// import { Signin } from './(auth)/signin';
// import { Signup } from './(auth)/signup';
// import { SignupOwner } from './(auth)/signup.owner';
// import { SignupPartner } from './(auth)/signup.partner';
// import { Find } from './(auth)/find';

import { MainLayout } from './(main)/_components/MainLayout';
import { Dashboard } from './(main)/dashboard';


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
      {authRoutes()}
    </Routes>
  )
}

/*

*/