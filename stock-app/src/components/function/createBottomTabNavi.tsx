import { useNavigation as useNativeNavigation, NavigationProp,useRoute as useNativeRoute, RouteProp } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';


export function createBottomTabNavi<T extends {}>() {
  const useNavigation = () => useNativeNavigation<NavigationProp<T>>();
  const Tab = createBottomTabNavigator<T>();

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

  return { Tab, useNavi };
}

