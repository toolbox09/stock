import { TopNavigation, Divider, Input, Layout, Button } from '@ui-kitten/components';
import { goback, Content } from '@/components';
import { useWorkStore } from '@/stores';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { useState } from 'react';



export function BarcodeFormScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionId, barcodeId } = route('BarcodeForm').params;
  const target =  useWorkStore( state => state.works[workId]?.sections[sectionId]?.barcodes[barcodeId] );
  const setTarget = useWorkStore( state => state.setBarcode );
  const [ barcode, setBarcode ] = useState(target.barcode);
  const [ count, setCount ] = useState(String(target.count));

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  const save = () => {
    setTarget( workId, sectionId, barcodeId, (b)=>{
      b.barcode = barcode;
      b.count = Number(count);
    });
    goback(navi);
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'작업'}
        accessoryLeft={back}
      />
      <Divider/>
      <Layout 
        level={'1'}
        style={{ 
          flex : 1,
          padding : 15,
          display : 'flex',
          flexDirection : 'column',
          gap : 15,
        }} 
      >
        <Input label={'바코드'} value={barcode} onChangeText={setBarcode} />
        <Input label={'수량'} value={count} onChangeText={setCount}  keyboardType='numeric' />
        <Button  onPress={save} style={{ width : '100%', marginTop : 15 }} >저장</Button>
      </Layout>
    </Content>
  )

}