import { Title, Group } from '@mantine/core';

export function StockLogo() {
  return (
  <Group w='100%' align='center' gap='xs' >
    <i className="ri-github-fill" style={{ fontSize : '30px' }} />
    <Title order={4} >ConHub</Title>
  </Group>
  )
}