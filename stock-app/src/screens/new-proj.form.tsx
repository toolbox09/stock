import { Content } from '@/components';
import { BackAction } from './_components/BackAction';
import { TopNavigation, Layout, Input, Button } from '@ui-kitten/components';
import { useNavi as useRootNavi } from './_root._navi';
import { useNavi } from './new-proj._navi';
import { useContext } from './new-proj._state';

export function FormScreen() {
  const rootNavi = useRootNavi();
  const { projectName, masterUrl, matchUrl, setter } = useContext();

  const back = () => {
    return <BackAction onPress={()=>rootNavi.navi.goBack()} />
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title='프로젝트 등록'
        accessoryLeft={back}
      />
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
        <Input label={'프로젝트명'} value={projectName} onChangeText={setter('projectName')} />
        <Input readOnly label={'마스터 파일'}  value={projectName} />
        <Input readOnly label={'전산현황 파일'}  value={projectName} />
        <Button style={{ width : '100%', marginTop : 15 }} >등록하기</Button>
      </Layout>
    </Content>
  )
}