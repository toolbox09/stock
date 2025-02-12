import { ImageProps, StyleSheet, View } from 'react-native';
import { Button, ButtonProps, Icon, IconElement, Layout, Spinner, Text } from '@ui-kitten/components';


interface LoadingButtonProps extends Omit<ButtonProps,'accessoryLeft'> {
  text : string;
  loading : boolean;
  loadingText : string;
}

const LoadingIndicator = (): React.ReactElement => {
  return (
    <View style={styles.indicator}>
    
  </View>
  )
}

export function LoadingButton( { text, loading, loadingText, ...props } : LoadingButtonProps ) {

  return (
    <Button
      accessoryLeft={ () => loading ? <Spinner status='info' size='small' /> : <Text></Text> }
      appearance='outline'
      {...props}
    >
      { loading ? loadingText : text }
    </Button>
  )
}

const styles = StyleSheet.create({
  indicator: {
    justifyContent: 'center',
    alignItems: 'center',
  },
});