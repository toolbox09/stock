import { TopNavigation, Divider, Input, Layout, Button } from '@ui-kitten/components';
import { goback, Content } from '@/components';
import { useWorkStore } from '@/stores';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { useState } from 'react';


export function SectionFormScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionId } = route('BarcodeForm').params;
  const section =  useWorkStore( state => state.works[workId]?.sections[sectionId] );
  const setSection =  useWorkStore( state => state.setSection );
  const [ name , setName ] = useState(section?.name);

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  const save = () => {
    setSection( workId, sectionId, ( s )=>{
      s.name = name;
    })
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
        <Input label={'섹션명'} value={name} onChangeText={(e)=>setName(e)} />
        <Button  onPress={save} style={{ width : '100%', marginTop : 15 }} >저장</Button>
      </Layout>
      
    </Content>
  )

}