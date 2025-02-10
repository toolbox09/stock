import { PropsWithChildren } from 'react';
import { Navigate,  } from 'react-router';
import { useAuthStore } from '@/stores';


export function SigninProvider( { children } : PropsWithChildren ) {
  const auth = useAuthStore( state => state.auth );
  if(auth) {
    return children;
  }else{
    return <Navigate to={"/signin"} />
  }
}