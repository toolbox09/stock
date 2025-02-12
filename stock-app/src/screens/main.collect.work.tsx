import { useMemo } from 'react';
import { Alert } from 'react-native';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { Text, TopNavigation, Divider, ListItem, Layout } from '@ui-kitten/components';
import { Content, SwipeableButton } from '@/components';
import { useWorkStore } from '@/stores';
import { SheetOp } from '@/entities';
import { NewWorkFab } from './_components/NewWorkFab';
import { useNavi } from './main.collect._navi';



export function WorkScreen() {

  const { navi } = useNavi();
  const works = useWorkStore( state => state.works );
  const deleteWork = useWorkStore( state => state.deleteWork );

  const data = useMemo( ()=>{
    return SheetOp.list(works);
  },[works])

  function handleDeleteSection( id : string ) {
    Alert.alert('삭제', '정말 삭제하시 겠습니까?', [
      { text : '취소', style: 'cancel' },
      {
        text : '확인',
        onPress : () => {
          deleteWork(id);
        }
      }
    ])
  }

  return (
    <Content>
      <NewWorkFab/>
      <TopNavigation
        alignment='center'
        title={'작업'}
      />
      <Divider/>
      <SwipeableFlatList
        data={data}
        keyExtractor={item=>item.id}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=>
        <ListItem 
          title={item.projectName} 
          description={  <Text  >{ item.master ? "있음!!" : "없음" }</Text> }
          onPress={()=>navi.navigate('Section',{ workId : item.id }) } 
          />
        }
        renderRightActions={( item )=>{
          return (
          <Layout style={{flexDirection : 'row'}} >
            <SwipeableButton color='#d2302b' text='삭제' onPress={()=>handleDeleteSection(item.id)} />
          </Layout>
          )
        }}
      />


    </Content>
  )
}

/*

*/