import { Route } from 'react-router';
import { AuthLayout } from './_components/AuthLayout';
import { Signin } from './signin';
import { Signup } from './signup';
import { SignupOwner } from './signup.owner';
import { SignupPartner } from './signup.partner';
import { Find } from './find';


export function authRoutes() {
  return (
    <Route element={<AuthLayout />}>
      <Route path="signin" element={<Signin />} />
      
      <Route path="signup">
        <Route index element={<Signup />} />
        <Route path="owner" element={<SignupOwner />} />
        <Route path="partner" element={<SignupPartner />} />
      </Route>

      <Route path="find" element={<Find />} />
    </Route>
  )
}      