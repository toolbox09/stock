import { useNavigate } from 'react-router';
import { useAuthStore } from '@/stores';
import { Center, Card, TextInput, Button, useForm, Flex, Title, Group, useGoBack } from '@/components';


export function Signin() {

  const navigate = useNavigate();
  const { goBack } = useGoBack('/');
  const singin = useAuthStore( state => state.signin );
  const form = useForm({
    initialValues : {
      id : '',
      password : ''
    }
  })

  async function handleSignin() {
    const { id, password } = form.getValues();
    if( await singin( id,  password) ) {
      goBack();
    }
  }

  return (
    <Center w={'100%'} h={'100%'} >
      <Card shadow='sm' p={40} >
        <Card.Section>
          <Flex direction={'column'} gap='md' w={300} >
            <Title>Signin</Title>
            <TextInput label='Id' {...form.getInputProps('id')}/>
            <TextInput label='Password' {...form.getInputProps('password')} type='password' />
            <Button fullWidth onClick={handleSignin} >Signin</Button>
            <Group justify='space-between' >
              <Button variant='transparent' onClick={()=>navigate('/')}  >ID/Password find</Button>
              <Button variant='transparent' onClick={()=>navigate('/signup')}  >Signup</Button>
            </Group>
          </Flex>
        </Card.Section>
      </Card>
    </Center>
  )
}