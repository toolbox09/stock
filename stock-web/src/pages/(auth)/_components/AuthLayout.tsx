import { Outlet } from 'react-router';
import { Group, Flex, Box, Title } from '@/components';


export function AuthLayout() {

  return (
    <Flex w={'100vw'} h={'100vh'} direction={'column'} >
      <Group p='xs' justify='center' style={{ 
        // boxShadow : 'var(--mantine-shadow-xs)' 
        }} >
        <Group w='100%' maw={1026}  >
          <Group align='center' gap='xs' >
            <i className="ri-github-fill" style={{ fontSize : '30px' }} />
            <Title order={4} >ConHub</Title>
          </Group>
        </Group>
      </Group>
      <Box flex={1} >
        <Outlet/>
      </Box>
    </Flex>
  );
}