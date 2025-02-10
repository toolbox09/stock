import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import { Auth } from '@/entities';

interface State {
  auth? : Auth;
}

interface Action {
  signin : ( id : string, password : string ) => boolean;
  logout : () => boolean;
}

const init : State = {
}

export const useAuthStore = create(
  immer<State & Action>((set)=>({
    ...init,
    signin : ( id : string, password : string ) => {
        set(state => {
          state.auth = {
            id : id,
            password : password,
          }
        })
        return true;
    },
    logout : () => {
      set( state => {
        state.auth = undefined;
      })
      return true;
    },
  }))
)