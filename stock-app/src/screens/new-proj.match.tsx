import { apis } from '@/api';
import { FileSelectPanel } from './_components/FileSelectPanel';
import { useContext } from './new-proj._state';

export function MatchScreen() {

  const { setter } = useContext();

  return (
    <FileSelectPanel
      title='전산현황'
      fetch={apis.master.matchFileList}
      onPress={( fileInfo )=>setter('matchUrl')(fileInfo.name)  }
    />
  )
}