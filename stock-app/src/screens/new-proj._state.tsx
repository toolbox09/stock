import { createContext } from '@/components';

interface NewProjData {
  projectName : string;
  masterUrl? : string;
  matchUrl? : string;
}

const { Provider, useContext } = createContext<NewProjData>({
  init : { projectName : '' }
})

export { Provider, useContext }; 