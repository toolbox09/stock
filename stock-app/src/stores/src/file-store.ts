import { create } from 'zustand';
import { FileInfo } from '@/entities';
import { apis } from '@/api';

interface State {
  masters? : FileInfo[];
  matchs? : FileInfo[];
}

interface Action {
  fetchMasters : () => Promise<boolean>;
  fetchMatchs : () => Promise<boolean>;
}

const init : State = {
}

export const useAuthStore = create<State & Action >(
    (set, get) => ({
      ...init,
      fetchMasters :  async () => {
        return new Promise(( resolve )=> resolve(true) )
      },
      fetchMatchs : async () => {
        return new Promise(( resolve )=> resolve(true) )
      }
    }),
  )
