import { useNavigation as useNativeNavigation, NavigationProp,useRoute as useNativeRoute, RouteProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';


export function createStackNavi<T extends {}>() {
  const useNavigation = () => useNativeNavigation<NavigationProp<T>>();
  const Stack = createNativeStackNavigator<T>();

  function useNavi() {
    const navi= useNavigation();
    const route = <K extends keyof T>( key : K ): RouteProp<T, K> => {
      return useNativeRoute<RouteProp<T, K>>();
    };
  
    return {
      navi,
      route,
    }
  }

  return { Stack, useNavi };
}
