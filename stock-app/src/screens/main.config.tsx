import { Content } from '@/components';
import { Text, Button } from '@ui-kitten/components';
import { ScrollView } from 'react-native';
import { useAuthStore } from '@/stores';

export function ConfigScreen() {
  const logout = useAuthStore( state => state.logout );

  return (
    <Content>
      <ScrollView style={{ flex : 1, padding : 15 }} >
        <Button onPress={logout} appearance='outline' >로그아웃</Button>
        <Text>asdsd</Text>
      </ScrollView>
    </Content>
  )
}