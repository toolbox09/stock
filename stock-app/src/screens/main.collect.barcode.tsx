import { useState } from 'react';
import { Alert } from 'react-native';
import { TopNavigation, Divider, ListItem, Layout, Button, Icon, Text, Toggle, CheckBox } from '@ui-kitten/components';
import { Content, SwipeableButton } from '@/components';
import { SheetOp, BarcodeOp } from '@/entities';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { useWorkStore } from '@/stores';
import { useMemo } from 'react';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';



export function BarcodeScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionId } = route('Barcode').params;
  const work = useWorkStore( state => state.works[workId] );
  const barcodes = useWorkStore( state => state.works[workId]?.sections[sectionId]?.barcodes );
  const setSection = useWorkStore( state => state.setSection );
  const addBarcode = useWorkStore( state => state.addBarcode );
  const autoAddSection = useWorkStore( state => state.autoAddSection );
  const [ merge, setMerge ] = useState(false);

  const data = useMemo( () => {
    return barcodes ? SheetOp.list(barcodes) : [];
  },[barcodes])

  const back = () => {
    return <BackAction onPress={()=>navi.navigate('Section',{ workId : workId })} />
  }

  const deleteBarcode = ( barcodeId : string ) => {
    Alert.alert('삭제', '정말 삭제하시 겠습니까?', [
      { text : '취소', style: 'cancel' },
      {
        text : '확인',
        onPress : () => {
          setSection( workId, sectionId, (_) => {
            SheetOp.delete(_.barcodes, barcodeId);
          })
        }
      }
    ])
  }

  function handleBarcode( barcode : string, count : number ) {

    let matched = true;
    if(work && barcodes ) {
      if(work.master) {
        matched = work.master[barcode] ? true : false;
      }
    }
    addBarcode(workId, sectionId, barcode, count, matched);
  }

  const handleNew = () => {
    const barcode = BarcodeOp.randomBarcode();
    handleBarcode(barcode, 1);
  }

  const handleNextSection = () => {
    const target = autoAddSection( workId );
    if(target) {
      navi.navigate("Barcode", { workId : workId, sectionId : target.id })
    } 
  }

  return (
    <Content>
      <NewFab onPress={handleNew} />
      <TopNavigation
        alignment='center'
        title={work?.sections[sectionId]?.name}
        accessoryLeft={back}
        accessoryRight={()=><Button onPress={handleNextSection} >다음섹션</Button>}
      />
      <Divider/>
      <Layout style={{  padding : 10, flexDirection : 'row', justifyContent : 'space-between' }} >
        <CheckBox
          checked={merge}
          onChange={nextChecked => setMerge(nextChecked)}
        >
          {`병합`}
        </CheckBox>
        <Button appearance='outline' onPress={()=>navi.navigate('BarcodeNew',{ workId : workId, sectionId : sectionId })} >추가</Button>
      </Layout>
      <SwipeableFlatList
        data={data}
        keyExtractor={item=>item.id}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=>
        <ListItem 
          title={item.barcode}
          description={item.created}
          accessoryRight={()=><Text>{item.count}</Text>}
          style={{ backgroundColor : item.matched ? undefined : '#ffc9c9' }}
          // onPress={()=>navi.navigate('Barcode', { workId :  work.id, sectionId : item.id })}
          />
        }
        renderRightActions={( item )=>{
          return (
          <Layout style={{flexDirection : 'row'}} >
            <SwipeableButton color='rgb(5,150,129)' text='편집' onPress={()=>{ navi.navigate("BarcodeForm", { workId : workId, sectionId : sectionId,  barcodeId : item.id  }) }} />
            <SwipeableButton color='#d2302b' text='삭제' onPress={()=>deleteBarcode(item.id)} />
          </Layout>
          )
        }}
        enableOpenMultipleRows={false}
      />
    </Content>
  )
}

function NewFab( { onPress } : {  onPress : () => void; } ) {
  const { navi } = useNavi();
  return (
  <Button
    accessoryLeft={<Icon
      name='plus-outline'
    />}
    style={{
      bottom: 16,
      right: 16,
      position: 'absolute',
      zIndex : 20000,
    }}
    onPress={()=>onPress()}
  >
    TEST
  </Button>
  )
}