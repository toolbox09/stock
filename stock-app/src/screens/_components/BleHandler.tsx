import { useEffect } from 'react';
import { PermissionsAndroid } from 'react-native';
import { useBleStore } from '@/stores';
import RNBluetoothClassic, {
  BluetoothDevice,
} from 'react-native-bluetooth-classic';



const requestAccessFineLocationPermission = async () => {
  const granted = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    {
      title: 'Access fine location required for discovery',
      message:
        'In order to perform discovery, you must enable/allow ' +
        'fine location access.',
      buttonNeutral: 'Ask Me Later',
      buttonNegative: 'Cancel',
      buttonPositive: 'OK',
    }
  );
  return granted === PermissionsAndroid.RESULTS.GRANTED;
};



export function BleHandler() {
  const reConnect = useBleStore( state => state.reConnect );

  function onStateChange( e : any ) {
    console.log(e);
  }

  async function init() {
    await reConnect();
  }

  useEffect(()=>{
    RNBluetoothClassic.onStateChanged(onStateChange);
    requestAccessFineLocationPermission();
    init();
  },[])
  

  return null;
}