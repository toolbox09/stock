import { createBottomTabNavi } from '@/components';

type Tabs = {
  Collect : undefined;
  Project : undefined;
  Master : undefined;
  Config : undefined;
}

const { Tab, useNavi } = createBottomTabNavi<Tabs>();
export { Tab, useNavi }
