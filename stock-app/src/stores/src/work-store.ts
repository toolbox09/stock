import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import { persist, createJSONStorage } from 'zustand/middleware';
import { Section, Barcode, Work, WorkSheet, SheetOp, Utils } from '@/entities';
import { apis } from '@/api';
import { Storage } from './storage';
import { useAuthStore } from './auth-store';


interface State {
  works : WorkSheet;
}

interface Action {
  addWork : ( work : Work ) => Work;
  deleteWork : ( workId : string ) => void;

  setWork : ( workId : string, action : ( work : Work ) => void ) => void;
  setSection : ( workId : string, sectionId : string, action : ( section : Section ) => void ) => void;
  setBarcode : ( workId : string, sectionId : string, barcodeId : string,  action : ( barcode : Barcode ) => void ) => void;

  autoAddSection : ( workId : string ) => Section|undefined;
  addBarcode : ( workId : string, sectionId : string, barcode : string, count : number, matched : boolean ) => void;
}

const init : State = {
  works : {},
}

export const useWorkStore = create(
  persist(
    immer<State & Action>(
    (set, get) => ({
      ...init,
      addWork : ( work : Work ) => {
        set( state => {
          SheetOp.set(state.works, work)
        });
        return work;
      },
      deleteWork : ( workId : string ) => {
        set( state => {
          delete state.works[workId];
        });
      },
      setWork : ( workId : string, action : ( work : Work ) => void ) => {
        set( state => {
          const work = state.works[workId];
          if(action) {
            action(work);
          }
        })
      },
      setSection : ( workId : string, sectionId : string, action : ( section : Section ) => void ) => {
        set( state => {
          const section = state.works[workId]?.sections[sectionId];
          if(action) {
            action(section);
          }
        })
      },
      setBarcode : ( workId : string, sectionId : string, barcodeId : string,  action : ( barcode : Barcode ) => void ) => {
        set( state => {
          const barcode = state.works[workId]?.sections[sectionId]?.barcodes[barcodeId];
          if(action) {
            action(barcode);
          }
        })
      },
      autoAddSection : ( workId : string ) => {

        const auth = useAuthStore.getState().auth;
        if(!auth)
          return;

        let result : Section | undefined;
        set( state => {
          const work = state.works[workId];
          if(work) {
            const newName = Utils.makeSectionName(auth.keyword, work.sections );
            const newSection = Utils.createSection(newName);
            SheetOp.set(work.sections, newSection);
            result = newSection;
          }
        })
        return result;
      },
  
      addBarcode : ( workId : string, sectionId : string, barcode : string, count : number, matched : boolean ) => {
        set( state => {
          const section = state.works[workId]?.sections[sectionId];
          if(section) {
            const b = Utils.createBarcode( barcode, count, matched);
            SheetOp.set(section.barcodes, b);
          }
        })
      }
    }),
  ),
  {
    name: 'sotck-DEV-work-storage',
    storage : createJSONStorage(()=>Storage), 
  }
  )
);
