import { Button, Flex, Title, useForm, PasswordInput, TextInput } from '@/components';



export function SignupOwner() {

  const form = useForm({
    initialValues : {
      id : "",
      password : "",
    }
  })


  return (
    <Flex w={'100%'} h={'100%'} justify={'center'}>
      <Flex direction={'column'} gap='md' w={400} mt={50} >
        <Title order={3} >Owner Signup</Title>
        <TextInput label={'아이디'}  {...form.getInputProps('id')} />
        <PasswordInput label={'비밀번호'}  {...form.getInputProps('password')} />
        <Button mt='xl' >Signup</Button>
      </Flex>
    </Flex>
  )
}

