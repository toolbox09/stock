import { createContext } from '@/components';
import { BackAction } from './_components/BackAction';
import { TopNavigation, Layout, Input, Button } from '@ui-kitten/components';
import { Stack } from './new-proj._navi';
import { Provider } from './new-proj._state';
import { FormScreen } from './new-proj.form';
import { MasterScreen } from './new-proj.master';
import { MatchScreen } from './new-proj.match';




export function NewProjScreen() {

  return (
  <Provider>
    <Stack.Navigator
      screenOptions={{
        headerShown : false,
      }}
    >
      <Stack.Screen name='Form' component={FormScreen} />
      <Stack.Screen name='Master' component={MasterScreen} />
      <Stack.Screen name='Match' component={MatchScreen} />
    </Stack.Navigator>
  </Provider>
  )
}