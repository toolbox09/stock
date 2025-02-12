import { apis } from '@/api';
import { FileSelectPanel } from './_components/FileSelectPanel';
import { useContext } from './new-proj._state';

export function MasterScreen() {

  const { setter } = useContext();

  return (
    <FileSelectPanel
      title='마스터'
      fetch={apis.master.fileList}
      onPress={( fileInfo )=>setter('masterUrl')(fileInfo.name)  }
    />
  )
}