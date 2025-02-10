import { createStackNavi } from '@/components';

type Stacks = {
  Form : undefined;
  Master : undefined;
  Match : undefined;
}

const { Stack, useNavi } = createStackNavi<Stacks>();
export { Stack, useNavi }
