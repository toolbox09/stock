import { TopNavigation, Divider, Input, Layout, Button } from '@ui-kitten/components';
import { goback, Content } from '@/components';
import { useWorkStore } from '@/stores';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { useState } from 'react';



export function BarcodeNewScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionId } = route('BarcodeNew').params;
  const addBarcode = useWorkStore( state => state.addBarcode );
  const [ barcode, setBarcode ] = useState("");
  const [ count, setCount ] = useState("");

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  const save = () => {
    addBarcode( workId, sectionId, barcode, Number(count), true);
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
        <Input label={'수량'} value={count} onChangeText={setCount} keyboardType='numeric'  />
        <Button  onPress={save} style={{ width : '100%', marginTop : 15 }} >저장</Button>
      </Layout>
    </Content>
  )

}