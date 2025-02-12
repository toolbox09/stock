import { RefreshControl, Alert } from 'react-native';
import { BackAction } from './_components/BackAction';
import { TopNavigation, ListItem, Divider } from '@ui-kitten/components';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { useNavi } from './_root._navi';
import { Content, goback, useRefresh, LoadingButton } from '@/components';
import { apis } from '@/api';
import { ProjectInfo } from '@/entities';
import { useState, useEffect } from 'react';
import { useWorkStore } from '@/stores';



interface ProjItemProps {
  info : ProjectInfo;
}

function ProjItem( { info } : ProjItemProps ) {

  const [ loading, setLoading ] = useState(false);
  const addWork = useWorkStore( state => state.addWork );

  async function handleWorkCreate() {
    try{
      setLoading(true);
      const work = await apis.work.get(info.name, info.masterUrl);
      work ? Alert.alert("등록 되었습니다.") : Alert.alert("등록에 실패 하였습니다.");
      if(work) {
        work.sections = {};
        addWork(work);
      }
    }finally {
      setLoading(false);
    }
  }

  return (
  <ListItem 
    title={info.name} 
    description={info.created}
    accessoryRight={()=><LoadingButton text='선택' loading={loading} loadingText='로딩중' onPress={handleWorkCreate} />}
  />
  )
}

export function NewWorkScreen() {

  const { navi } = useNavi();
  const { data, isLoading, refresh } = useRefresh({ fetch : async () => {
    const _data = await apis.project.infoList();
    return _data;
  }});

  useEffect(()=>{
    refresh();
  },[])

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }


  return (
  <Content>
    <TopNavigation
      alignment='center'
      title={'작업등록'}
      accessoryLeft={back}
    />
    <Divider/>
    <SwipeableFlatList
      refreshControl={<RefreshControl refreshing={isLoading} onRefresh={refresh} />}
      data={data}
      keyExtractor={item=>item.name}
      ItemSeparatorComponent={Divider}
      renderItem={({ item })=><ProjItem info={item} />
      }
    />
  </Content>
  )
}