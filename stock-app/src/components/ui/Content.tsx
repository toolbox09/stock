import { SafeAreaView, SafeAreaViewProps } from 'react-native-safe-area-context';


export function Content( { children, style, ...props } : SafeAreaViewProps ) {

	return (
		<SafeAreaView style={[{ flex : 1 }, style ]} {...props} >
      {children}
		</SafeAreaView>
  )
}

