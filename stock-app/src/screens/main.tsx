import { Tab } from './main._navi';
import { ScanBarcodeIcon, SettingsIcon, PackageCheckIcon, ListCheckIcon } from 'lucide-react-native';
import { CollectScreen } from './main.collect';
import { ProjScreen } from './main.proj';
import { ConfigScreen } from './main.config';
import { MasterScreen } from './main.master';

export function MainScreen() {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarActiveTintColor : '#000',
        tabBarInactiveTintColor : '#999',
        tabBarShowLabel : false,
        headerShown : false,
      }}
    >
      <Tab.Screen 
        name='Collect' 
        component={CollectScreen}
        options={{
          title : '재고조사',
    
          tabBarIcon : ({ size, color })=>{
            return <ScanBarcodeIcon color={color} size={size} />
          }
        }} 
      />
      <Tab.Screen 
        name='Project' 
        component={ProjScreen}
        options={{
          title : '프로젝트',
          tabBarIcon : ({ size, color })=>{
            return <PackageCheckIcon color={color} size={size} />
          }
        }} 
      />
      <Tab.Screen 
        name='Master' 
        component={MasterScreen}
        options={{
          title : '마스터',
          tabBarIcon : ({ size, color })=>{
            return <ListCheckIcon color={color} size={size} />
          }
        }} 
      />
      <Tab.Screen 
        name='Config' 
        component={ConfigScreen} 
        options={{
          title : '설정',
          tabBarIcon : ({ size, color })=>{
            return <SettingsIcon color={color} size={size} />
          }
        }} 
      />
    </Tab.Navigator>
  )
}