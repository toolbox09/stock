
import { Stack } from './main.collect._navi';
import { WorkScreen } from './main.collect.work';
import { SectionScreen } from './main.collect.section';
import { BarcodeScreen } from './main.collect.barcode';
import { SectionFormScreen } from './main.collect.section-form';
import { BarcodeFormScreen } from './main.collect.barcode-form';
import { UploadScreen } from './main.collect.upload';

export function CollectScreen() {

  return (
    <Stack.Navigator
      screenOptions={{
        headerShown : false,
      }}
    >
      <Stack.Screen name='Work' component={WorkScreen} />
      <Stack.Screen name='Section' component={SectionScreen} />
      <Stack.Screen name='Barcode' component={BarcodeScreen} />
      <Stack.Screen name='SectionForm' component={SectionFormScreen} />
      <Stack.Screen name='BarcodeForm' component={BarcodeFormScreen} />
      <Stack.Screen name='Upload' component={UploadScreen} />
    </Stack.Navigator>
  )
}

