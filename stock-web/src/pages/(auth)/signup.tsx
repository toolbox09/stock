import { useNavigate } from 'react-router';
import { Center, Card, Button, Flex, Title, Text } from '@/components';


export function Signup() {

  const navigate = useNavigate();

  return (
    <Center w={'100%'} h={'100%'} >
      <Flex direction={'column'} >
        <Title order={4} mb={40} >Signup</Title>
        <Flex gap={40} >
          <Card shadow='sm' p={40} >
            <Card.Section>
              <Flex direction={'column'} gap='md' w={200} >
                <Text fw='bold' >Owner</Text>
                <Text c='dimmed' >A prospective building owner starting architectural planning</Text>
                <Button fullWidth onClick={()=>navigate('./owner')} >Continue</Button>
              </Flex>
            </Card.Section>
          </Card>
          <Card shadow='sm' p={40} >
            <Card.Section>
              <Flex direction={'column'} gap='md' w={200} >
                <Text fw='bold' >Partner</Text>
                <Text c='dimmed' >Construction companies, and other building professionals</Text>
                <Button fullWidth onClick={()=>navigate('./partner')} >Continue</Button>
              </Flex>
            </Card.Section>
          </Card>
        </Flex>
      </Flex>

    </Center>
  )
}