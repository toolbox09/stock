import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as eva from '@eva-design/eva';
import { ApplicationProvider, IconRegistry } from '@ui-kitten/components';
import { EvaIconsPack } from '@ui-kitten/eva-icons';
import { RootScreen } from './src/screens/_root';


export default () => (
  <>
    <IconRegistry icons={EvaIconsPack} />
    <ApplicationProvider {...eva} theme={eva.light}>
    <NavigationContainer>
      <GestureHandlerRootView>
        <SafeAreaProvider>
          <RootScreen />
        </SafeAreaProvider>
      </GestureHandlerRootView>
    </NavigationContainer>
  </ApplicationProvider>
  </>

);