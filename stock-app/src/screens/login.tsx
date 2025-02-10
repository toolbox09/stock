import { useState } from 'react';
import { Alert } from 'react-native';
import { Content } from '@/components';
import { Text, Layout, Input, Button } from '@ui-kitten/components';
import { useAuthStore } from '@/stores';

export function LoginScreen() {
  const login = useAuthStore( state => state.login );
  const [ id, setId ] = useState<string>();
  const [ password, setPassword ] = useState<string>();

  async function handleLogin() {
    if( id && id.length > 0 && password && password.length > 0 ) {
      await login(id, password);
    }else{
      Alert.alert('잘못된 입력입니다.')
    }
  }

  return (
    <Content>
      <Layout style={{ 
        flex : 1,
        padding : 15,
        justifyContent : 'center', 
        alignItems : 'center',
        display : 'flex',
        flexDirection : 'column',
        gap : 15,
      }} >
        <Input label={'아이디'} value={id} onChangeText={nextValue => setId(nextValue)} />
        <Input label={'비밀번호'} value={password} secureTextEntry={true} onChangeText={nextValue => setPassword(nextValue)} />
        <Button style={{ width : '100%' }} onPress={handleLogin} >로그인</Button>
      </Layout>
    </Content>
  )
}