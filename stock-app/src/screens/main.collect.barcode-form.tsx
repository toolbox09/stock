import { TopNavigation, Divider } from '@ui-kitten/components';
import { goback, Content } from '@/components';
import { BackAction } from './_components/BackAction';
import { useNavi } from './main.collect._navi';



export function BarcodeFormScreen() {

  const { navi } = useNavi();

  const back = () => {
    return <BackAction onPress={()=>goback(navi)} />
  }

  return (
    <Content>
      <TopNavigation
        alignment='center'
        title={'작업'}
        accessoryLeft={back}
      />
      <Divider/>
    </Content>
  )

}