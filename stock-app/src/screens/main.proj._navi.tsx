import { createStackNavi } from '@/components';

type Stacks = {
  List : undefined;
  Detail : { projectName : string };
}

const { Stack, useNavi } = createStackNavi<Stacks>();
export { Stack, useNavi }