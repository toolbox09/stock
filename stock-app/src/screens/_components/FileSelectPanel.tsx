import { useNavi } from '../new-proj._navi';
import { RefreshControl } from 'react-native';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { TopNavigation, Text, ListItem, Divider, Input, Layout } from '@ui-kitten/components';
import { Content, goback, useRefresh, useDebounce } from '@/components';
import { FileInfo } from '@/entities';
import { BackAction } from './BackAction';
import { SearchInput } from './SearchInput';
import { useEffect, useMemo, useState } from 'react';

// search-outline
interface FileSelectPanelProps {
  title : string;
  fetch : () => Promise<FileInfo[] | undefined>;
  onPress? : ( fileInfo : FileInfo ) => void;
}

function searchKeyword( files : FileInfo[], keyword? : string ) {
  if( keyword && keyword.length >= 2) {
    const lowerKeyword = keyword.toLowerCase();
    return files.filter( item => item.name.toLocaleLowerCase().includes(lowerKeyword) );
  }
  return files;
}

export function FileSelectPanel( { title, fetch, onPress } : FileSelectPanelProps ) {

  const { navi } = useNavi();
  const { data, isLoading, refresh } = useRefresh({ fetch : fetch });
  const [ keyword, setKeyword ] = useState<string>();

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  const searchResults = useMemo( ()=>{
    return data && searchKeyword(data, keyword);
  },[data, keyword ])

  useEffect(()=>{
    refresh();
  },[])

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={title}
        accessoryLeft={back}
      />
      <SearchInput onChange={setKeyword} />
      <SwipeableFlatList
          refreshControl={<RefreshControl refreshing={isLoading} onRefresh={refresh} />}
          data={searchResults}
          keyExtractor={item=>item.name}
          ItemSeparatorComponent={Divider}
          renderItem={({ item })=>
          <ListItem 
            title={item.name} 
            description={item.modified} 
            onPress={()=> {
              if(onPress) {
                onPress(item);
              }
              goback(navi);
            } } 
            />
          }
        enableOpenMultipleRows={false}
      />
    </Content>
  )

}