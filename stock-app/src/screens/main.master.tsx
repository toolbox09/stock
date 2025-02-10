// import { useEffect, useState } from 'react';
import { StyleSheet, Text, View, Button, FlatList } from 'react-native';
// import { PermissionsAndroid } from 'react-native';
// import RNBluetoothClassic, {
//   BluetoothDevice,
// } from 'react-native-bluetooth-classic';

/*
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


const BluetoothDeviceList = () => {
  const [devices, setDevices] = useState<BluetoothDevice[]>([]);
  const [isScanning, setIsScanning] = useState(false);

  function onStateChange(event: any) {
    console.log(event);
  }

  useEffect(() => {

    RNBluetoothClassic.onStateChanged(onStateChange);
    async function gets() {
      const paired = await RNBluetoothClassic.getBondedDevices();
      setDevices(paired);
    }
    requestAccessFineLocationPermission();
    gets();

  }, []);

  function on( device : BluetoothDevice) {

    RNBluetoothClassic.getConnectedDevices()
    .then((e)=>{
      console.log(e);
    })
    
    device.isConnected().then( e => console.log(e) );
    
    
    device.connect()
     .then(( a )=>{
      device.onDataReceived((data) => {
        device.read().then((e)=>{
          console.log(e);
        })
      });

      console.log(a);
     }).catch( (b)=>{
      console.log(b);
     })
    
    

  }


  return (
    <View style={{ padding: 20 }}>
      <Button title={isScanning ? "검색 중..." : "장치 검색"} disabled={isScanning} />
      <FlatList
        data={devices}
        keyExtractor={item=>item.id}
        renderItem={(item)=> <DeviceItem device={item.item}  /> }
      >
      </FlatList>
    </View>
  );
};

function DeviceItem( { device } : { device : BluetoothDevice} ) {

  const [ connecting, setConnecting ] = useState<boolean>();
  const [ isConnect, setConnect ] = useState<boolean>();

  

  async function connect() {
    try{
      let connection = await device.isConnected();
      if(!connection) {
        setConnecting(true);
        connection = await device.connect({
          // connectionType : 'binary',
        });
        setConnect(connection);
      }
    }finally {
      setConnecting(false);
    }

  }

  // function onReceivedData() {
  // }

  async function performRead() {

    let available = await device.available();
    if(available > 0) {
      for( let i = 0; i < available; i++ ){
        const data = await device.read();
        console.log(data);
      }

    }
  }

  useEffect(()=>{
    setInterval(async () => await performRead(), 5000);

  },[device])

  return (
    <Button color={ isConnect ? '#000' : '#ff0' } title={ connecting ? '연결중' : `DEV-${device.name}` } onPress={connect}  />
  )
}
*/

export function MasterScreen() {

  return (
    <View style={styles.container}>
      <Text>asd</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
