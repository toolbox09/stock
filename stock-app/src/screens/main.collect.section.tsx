import { useMemo, useState, useEffect } from 'react';
import { produce } from 'immer';
import { Alert } from 'react-native';

import { TopNavigation, Divider, ListItem, CheckBox, Button, Layout, ButtonGroup } from '@ui-kitten/components';
import SwipeableFlatList from 'rn-gesture-swipeable-flatlist';
import { goback, Content, SwipeableButton } from '@/components';
import { SheetOp } from '@/entities';
import { useWorkStore } from '@/stores';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';



type Selected = Record<string, any>;


export function SectionScreen() {

  const { navi, route } = useNavi();
  const { workId } = route('Section').params;
  const work = useWorkStore( state => state.works[workId] );
  const sections = useWorkStore( state => state.works[workId]?.sections );
  const setWork = useWorkStore( state => state.setWork );
  const addSection = useWorkStore( state => state.autoAddSection );

  const [ selected, setSelected ] = useState<Selected>({});
  const [checked, setChecked] = useState(false);


  const data = useMemo(()=>{
    return sections ? SheetOp.list(sections) : [];
  },[sections])

  const back = () => {
    return <BackAction onPress={()=>navi.navigate('Work')} />
  }

  const toggle = ( sectionId : string ) => {
    setSelected( produce( state =>{
      if(state[sectionId]) {
        delete state[sectionId];
      }else{
        state[sectionId] = 1;
      }
    }))
  }

  const allSelect = ( select : boolean ) => {
    if(select) {
      const select : Selected = {};
      data.forEach( target =>{
        select[target.id] = 1;
      })
      setSelected(select);
    }else{
      setSelected({});
    }
  }

  function deleteSection( id : string ) {
    Alert.alert('삭제', '정말 삭제하시 겠습니까?', [
      { text : '취소', style: 'cancel' },
      {
        text : '확인',
        onPress : () => {
          setWork( workId, (_)=>{
            SheetOp.delete(_.sections, id);
          })
        }
      }
    ])
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'섹션'}
        accessoryLeft={back}
        accessoryRight={()=><Button size='small' onPress={()=>addSection(workId)} >추가</Button>}
      />
      <Divider/>
      <Layout style={{ padding : 15, flexDirection : 'row', justifyContent : 'space-between' }}  >
        <CheckBox 
          checked={checked}  
          onChange={(nextChecked)=>{  
            setChecked(nextChecked);
            allSelect(nextChecked);
          }}  
        >
          전체선택
        </CheckBox>
        <ButtonGroup size='small' appearance='ghost' >
          <Button appearance='outline' >선택 내보내기</Button>
          <Button>전체 내보내기</Button>
        </ButtonGroup>
      </Layout>
      <Divider/>
      <SwipeableFlatList
        data={data}
        keyExtractor={item=>item.id}
        ItemSeparatorComponent={Divider}
        renderItem={({ item })=>
        <ListItem 
          title={item.name}
          accessoryLeft={<CheckBox checked={selected[item.id] ? true : false }  onChange={()=>toggle(item.id)}  />}
          onPress={()=>navi.navigate('Barcode', { workId :  work.id, sectionId : item.id })}
          />
        }
        renderRightActions={( item )=>{
          return (
          <Layout style={{flexDirection : 'row'}} >
            <SwipeableButton color='rgb(5,150,129)' text='편집' onPress={()=>{ navi.navigate("SectionForm", { workId : work.id, sectionId : item.id  }) }} />
            <SwipeableButton color='#d2302b' text='삭제' onPress={()=>deleteSection(item.id)} />
          </Layout>
          )
        }}
        enableOpenMultipleRows={false}
      />
    </Content>
  )

}