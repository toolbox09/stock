import { IconElement, Icon, IconProps, TopNavigationAction } from '@ui-kitten/components';

interface BackActionProps {
  onPress? : () => void;
}

export function BackAction( { onPress } : BackActionProps ) {

  const backIcon = ( props : IconProps ): IconElement => (
    <Icon
      {...props}
      name='arrow-back'
    />
  );

  return (
    <TopNavigationAction onPress={ ()=> onPress && onPress() } icon={backIcon} />
  );

}