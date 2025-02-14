import { useState } from 'react';
import { Alert } from 'react-native';
import { TopNavigation, Divider, Input, Layout, Button  } from '@ui-kitten/components';
import { goback, Content, LoadingButton } from '@/components';
import { BarcodeRaw, SheetOp } from '@/entities';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';
import { apis } from '@/api';
import { useAuthStore, useWorkStore } from '@/stores';


export function UploadScreen() {

  const { navi, route } = useNavi();
  const { workId, sectionIds } = route('Upload').params;
  const work = useWorkStore( state => state.works[workId] );
  const keyword = useAuthStore( state => state.auth?.keyword );
  const [ fileName, setFileName ] = useState(keyword);
  const [ uploading, setUploading ] = useState<boolean>(false);

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  const save = async () => {
    if(work) {
      try{
        setUploading(true);
        const targets = SheetOp.list(work.sections).filter(  _ => sectionIds.indexOf(_.id) !== -1 );
        const raws = new Array<BarcodeRaw>();
        targets.forEach( sec => {
          const _t = SheetOp.list(sec.barcodes).map( _ => {
            return {
              barcode : _.barcode,
              count : _.count,
            }
          });
          raws.push(..._t);
        })
        const result = await apis.work.update( {
          projectName : work.projectName,
          fileName : fileName ?? 'TMP',
          raws : raws
        });
        Alert.alert( result ? '업로드에 성공하였습니다.' : '업로드에 실패하였습니다.')
      }finally {
        setUploading(false);
      }
    }
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'업로드'}
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
        <Input label={'섹션명'} value={fileName} onChangeText={setFileName} />
        <LoadingButton text='업로드' loadingText={'업로딩'} loading={uploading} onPress={save} style={{ width : '100%', marginTop : 15 }} />
      </Layout>
    </Content>
  )

}