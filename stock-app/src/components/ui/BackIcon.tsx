import React from 'react';
import {
  Icon,
  IconElement,
  IconProps,
} from '@ui-kitten/components';

export const BackIcon = (props : IconProps): IconElement => (
  <Icon
    {...props}
    name='arrow-back'
  />
);