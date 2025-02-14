import { useEffect } from 'react';
import { RefreshControl  } from 'react-native';
import { Content, useRefresh } from '@/components';
import { TopNavigation, ListItem, Divider, Text, Layout } from '@ui-kitten/components';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { NewProjFab } from './_components/NewProjFab';
import { ProjectState } from '@/entities';
import { apis } from '@/api';
import { useNavi } from './main.proj._navi';



interface ProjItemProps {
  state : ProjectState;
  onPress : ( projectName : string )=>void;
}

function ProjItem( { state, onPress } : ProjItemProps ) {
  return (
  <ListItem 
    title={state.name} 
    description={state.created}
    accessoryRight={()=>
      <Layout>
        <Text>{`상태 : ${state.originFile ? '취합' : '진행'}`}</Text>
        <Text>{`작업 : ${state.workFiles.length}개`}</Text>
      </Layout>
    }
    onPress={()=>onPress(state.name)}
  />
  )
}

export function ListScreen() {
  const { navi } = useNavi();


  const { data, isLoading, refresh } = useRefresh({ fetch : async () => {
    const _data = await apis.project.stateList();
    return _data;
  }});

  useEffect(()=>{
    refresh();
  },[])



  return (
    <Content>
      <NewProjFab/>
      <TopNavigation
        alignment='center'
        title={'프로젝트'}
      />
      <Divider/>
      <SwipeableFlatList
        refreshControl={<RefreshControl refreshing={isLoading} onRefresh={refresh} />}
        data={data}
        keyExtractor={item=>item.name}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=><ProjItem state={item}  onPress={(name)=> navi.navigate('Detail',{ projectName : name }) } />
        }
      />
    </Content>
  )
}