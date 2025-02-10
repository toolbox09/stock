import { createStackNavi } from '@/components';

type Stacks = {
  Main : undefined,
  NewProj : undefined;
  Ble : undefined;
}

const { Stack, useNavi } = createStackNavi<Stacks>();
export { Stack, useNavi };