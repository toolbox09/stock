import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { Auth } from '@/entities';
import { apis } from '@/api';
import { Storage } from './storage';

interface State {
  auth? : Auth;
}

interface Action {
  login : ( id : string, password : string ) => Promise<boolean>;
  logout : () => void;
}

const init : State = {
}

export const useAuthStore = create(
  persist<State & Action>(
    (set, get) => ({
      ...init,
      login :  async (id : string, password : string ) => {
        const res = await apis.auth.login(id, password);
        if(res) {
          set( state => ({ ...state, auth : res }) )
          return true;
        }else{
          return false;
        }
      },
      logout : () => {
        set( state => ({ ...state, auth : undefined }) )
      }
    }),
    {
      name: 'sotck-DEV-auth-storage',
      storage : createJSONStorage(()=>Storage), 
    }
  )
)
