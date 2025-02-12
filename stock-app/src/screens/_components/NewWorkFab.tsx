import { StyleSheet } from 'react-native';
import { Button, Icon } from '@ui-kitten/components'
import {  useNavi } from '../_root._navi';


export function NewWorkFab() {
  const { navi } = useNavi();
  return (
  <Button
    accessoryLeft={<Icon
      name='plus-outline'
    />}
    style={styles.fabStyle}
    onPress={()=> navi.navigate('NewWork') }
  >
    작업등록
  </Button>
  )
}

const styles = StyleSheet.create({
  fabStyle: {
    bottom: 16,
    right: 16,
    position: 'absolute',
    zIndex : 20000,
  },
});