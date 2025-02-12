import { Text, Pressable  } from 'react-native';

type Id = string | number;

interface SwipeableButtonProps {
  id? : Id;
  text : string;
  color : string;
  onPress? : ( id? : Id ) => void;
}

export function SwipeableButton( { id, text, color, onPress } : SwipeableButtonProps ) {

  return (
    <Pressable
      onPress={()=>{
        if(onPress) {
          onPress(id)
        }
      }}
      style={{ 
        width : 70,
        backgroundColor : color,
        display : 'flex',
        flexDirection : 'column',
        justifyContent : 'center',
        alignItems : 'center',
      }}
    >
      <Text style={{ color : '#fff' }} >
        {text}
      </Text>
   </Pressable>
  )
}