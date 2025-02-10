import { Outlet } from 'react-router';
import { AppShell, Burger, Group, Skeleton, useDisclosure, StockLogo, Button } from '@/components';
import { useAuthStore } from '@/stores';


export function MainLayout() {
  const [opened, { toggle }] = useDisclosure();
  const logout = useAuthStore( state => state.logout );
  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{ width: 300, breakpoint: 'sm', collapsed: { mobile: !opened } }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md" justify='space-between' >
          <Group>
            <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
            <StockLogo/>
          </Group>
          <Group>
            <Button variant='default' onClick={logout} >Logout</Button>
          </Group>
        </Group>
      </AppShell.Header>
      <AppShell.Navbar p="md">
        Navbar
        {Array(15)
          .fill(0)
          .map((_, index) => (
            <Skeleton key={index} h={28} mt="sm" animate={false} />
          ))}
      </AppShell.Navbar>
      <AppShell.Main>
        <Outlet/>
      </AppShell.Main>
    </AppShell>
  );
}