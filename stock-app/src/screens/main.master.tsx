import { useEffect, useMemo, useState } from 'react';
import { RefreshControl, PermissionsAndroid  } from 'react-native';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { Content, useRefresh } from '@/components';
import { TopNavigation, ListItem, Divider, Text, Layout } from '@ui-kitten/components';
import RNBluetoothClassic, {
  BluetoothDevice,
} from 'react-native-bluetooth-classic';
import { useBleStore } from '@/stores';


interface BleItemProps {
  device : BluetoothDevice;
}

function BleItem( { device } : BleItemProps ) {

  const deviceAddress = useBleStore( state => state.deviceAddress );
  const reConnect = useBleStore( state => state.connect );
  const setDeviceAddress = useBleStore( state => state.setDeviceAddress );
  const [ connecting, setConnecting ] = useState<boolean>(false);
  const [ connected, setConnected ] = useState<boolean>(false);

  async function init() {
    const result = await device.isConnected();
    console.log(result);
    setConnected(result);
  }

  async function connect() {
    try {
      setConnecting(true);
      await reConnect(device.address);
    }finally {
      setConnecting(false);
    }
  }

  useEffect(()=>{
    init();
  },[]) 

  return (
  <ListItem 
    title={ device.name}
    description={device.address}
    accessoryRight={ <Text>{connecting ? '로딩중' : ''}</Text> }
    style={{ backgroundColor : connected ? 'rgba(18, 184, 134, .12)' : undefined }}
    onPress={connect} 
  />
  )
}

export function MasterScreen() {

  const devices = useBleStore( state => state.devices );
  const refreshList = useBleStore( state => state.refreshList );
  const [isScanning, setIsScanning] = useState(false);

  async function refresh() {
    try{
      setIsScanning(true);
      await refreshList();
    }finally {
      setIsScanning(false);
    }
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'스캐너'}
      />
      <Divider/>
      <SwipeableFlatList
        refreshControl={<RefreshControl refreshing={isScanning} onRefresh={refresh} />}
        data={devices}
        keyExtractor={item=>item.address}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=><BleItem device={item} />}
      />
    </Content>
  );
};