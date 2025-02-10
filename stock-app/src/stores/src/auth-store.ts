import { create } from 'zustand';
import { Auth } from '@/entities';
import { apis } from '@/api';

interface State {
  auth? : Auth;
}

interface Action {
  login : ( id : string, password : string ) => Promise<boolean>;
  logout : () => void;
}

const init : State = {
  auth : {
    id : "",
    keyword : "A",
  }
}

export const useAuthStore = create<State & Action >(
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
  )
