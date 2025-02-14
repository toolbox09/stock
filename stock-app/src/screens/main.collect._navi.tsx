import { createStackNavi } from '@/components';

type Stacks = {
  Work : undefined;
  Section : { workId : string };
  SectionForm : { workId : string, sectionId : string };
  Barcode : { workId : string, sectionId : string };
  BarcodeForm : { workId : string, sectionId : string, barcodeId : string };
  BarcodeNew : { workId : string, sectionId : string };
  Upload : { workId : string, sectionIds : string[] };
}

const { Stack, useNavi } = createStackNavi<Stacks>();
export { Stack, useNavi }