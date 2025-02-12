import { TopNavigation, Divider } from '@ui-kitten/components';
import { goback, Content } from '@/components';
import { SheetOp } from '@/entities';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { useWorkStore } from '@/stores';
import { useMemo } from 'react';



export function BarcodeScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionId } = route('Barcode').params;
  const barcodes = useWorkStore( state => state.works[workId]?.sections[sectionId]?.barcodes );

  const data = useMemo( () => {
    return barcodes ? SheetOp.list(barcodes) : [];
  },[barcodes])

  const back = () => {
    return <BackAction onPress={()=>navi.navigate('Section',{ workId : workId })} />
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'바코드'}
        accessoryLeft={back}
      />
      <Divider/>
    </Content>
  )

}