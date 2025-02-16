import { create } from 'zustand';
import RNBluetoothClassic, {
  BluetoothDevice,
} from 'react-native-bluetooth-classic';
import { immer } from 'zustand/middleware/immer';
import { persist, createJSONStorage } from 'zustand/middleware';
import { Storage } from './storage';


interface State {
  deviceAddress? : string;
  devices : BluetoothDevice[];
}

interface Action {
  refreshList : () => Promise<void>;
  setDeviceAddress : ( deviceAddress :  string ) => void;
  reConnect : () => Promise<boolean>;
  connect : ( address : string ) => Promise<boolean>;
}

const init : State = {
  devices : [],
}

type ReceivedFun = ( data : string ) => void;
let received : ReceivedFun | undefined;
export function setReceived( fun? : ReceivedFun ) {
  received = fun;
}

export const useBleStore = create(
  persist(
    immer<State & Action>(
    (set, get) => ({
      ...init,
      refreshList : async () => {
        const paired = await RNBluetoothClassic.getBondedDevices();
        if(paired) {
          set( state => {
            state.devices = paired;
          })
        }
      },
      setDeviceAddress : ( deviceAddress :  string ) => {
        set( state => state.deviceAddress = deviceAddress )
      },
      reConnect : async () => {
        const { deviceAddress, connect } =  get();
        return deviceAddress ? await connect(deviceAddress) : false;
      },
      connect : async ( address : string ) => {
        const device = await RNBluetoothClassic.connectToDevice(address);
        if(device) {
          device.onDataReceived((e)=>{
            device.read().then(() => {
              if(received) {
                received(e.data);
              }
            })
          })
        }
        set( state => {
          state.deviceAddress = device ? device.address : undefined;
        })
        return device ? true : false;
      }
    }),
  ),
  {
    name: 'sotck-DEV-ble-storage',
    storage : createJSONStorage(()=>Storage), 
  } 
)
);

/*
      setDevice : async ( device :  BluetoothDevice ) => {
        const result = await device.connect();
        if(result) {
          set( state=> {
            state.selectDevice = device;
          })
        }
      }
*/