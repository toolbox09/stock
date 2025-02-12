import { Stack } from './_root._navi';
import { MainScreen } from './main';
import { NewProjScreen } from './new-proj';
import { NewWorkScreen } from './new-work';
import { LoginScreen } from './login';
import { useAuthStore } from '@/stores';



export function RootScreen() {

  const auth = useAuthStore( state => state.auth );

  return auth ?
    <Stack.Navigator>
      <Stack.Screen name='Main' component={MainScreen} options={{ headerShown : false }} />
      <Stack.Screen name='NewProj' component={NewProjScreen}  options={{ headerShown : false }}/>
      <Stack.Screen name='NewWork' component={NewWorkScreen}  options={{ headerShown : false }}/>
    </Stack.Navigator>
    : <LoginScreen/>
}
