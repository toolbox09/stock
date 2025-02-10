import { Stack } from './main.proj._navi';
import { ListScreen } from './main.proj.list'
import { DetailScreen } from './main.proj.detail';

export function ProjScreen() {

  return (
    <Stack.Navigator
      screenOptions={{
        headerShown : false,
      }}
    >
      <Stack.Screen name='List' component={ListScreen} />
      <Stack.Screen name='Detail' component={DetailScreen} />
    </Stack.Navigator>
  )
}

