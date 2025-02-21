import { useEffect, useState } from 'react';
import { Content, LoadingButton, goback } from '@/components';
import { TopNavigation, ListItem, Divider, Text, Layout } from '@ui-kitten/components';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { BackAction } from './_components/BackAction';
import { ProjectState } from '@/entities';
import { apis } from '@/api';
import { useNavi } from './main.proj._navi';


export function DetailScreen() {
  const { navi, route } = useNavi();
  const { projectName } = route('Detail').params;
  const [ state, setState ] = useState<ProjectState>();
  const [ uploading, setUploading ] = useState<boolean>(false);

    const back = () => {
      return <BackAction onPress={()=>goback(navi)} />
    }


  const refresh = async () => {
    const target = await apis.project.state(projectName);
    if(target) {
      setState(target);
    }
  }

  useEffect(()=>{
    refresh();
  },[])


  const merge = async () => {
    try{
      setUploading(true);
      if(projectName) {
        await apis.work.collect(projectName);
        await refresh();
      }

    }finally {
      setUploading(false);
    }
  }


  //       <MergeFab onPress={merge} />
  return state ?
    <Content>
      <LoadingButton
        text='병합'
        loadingText='병합중'
        loading={uploading}
        style={{
          bottom: 16,
          right: 16,
          position: 'absolute',
          zIndex : 20000,
        }}
        onPress={merge}
      />
      <TopNavigation
        alignment='center'
        title={'프로젝트 상세'}
        accessoryLeft={back}
      />
      <Divider/>
      <Layout style={{ padding : 10, flexDirection : 'column', gap : 10 }}>
        <Text category='s1' >{state.name}</Text>
        <Text category='s2' >{`Master : ${ state.masterUrl ?? "없음" }`}</Text>
        <Text category='s2' >{`Match  : ${ state.matchUrl ?? "없음" }`}</Text>
        <Text category='s2' >{`Total  : ${ state.originFile?.name ?? "없음" }`}</Text>
        <Text category='s2' >{`Merge  : ${ state.mergeFile?.name ?? "없음" }`}</Text>
      </Layout>
      <Divider/>
      <SwipeableFlatList
        style={{ padding : 10 }}
        data={state.workFiles}
        keyExtractor={item=>item.name}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=> {
          return (
              <ListItem 
                title={item.name} 
                description={item.created}
            />
          )
        }} />
    </Content> : 
    <Content>
      <Text>Error</Text>
    </Content>
}


/*

*/