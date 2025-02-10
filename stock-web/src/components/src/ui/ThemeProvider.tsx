import { PropsWithChildren } from 'react';
import { MantineProvider, createTheme } from '@mantine/core';
import './ThemeProvider.css';

const fontFamily = `"Pretendard Variable", Pretendard, sans-serif;`;

const theme = createTheme({
  colors : {
    primary: [
      '#000', // Lightest pink
      '#000',
      '#000',
      '#000',
      '#000',
      '#000', // Primary pink
      '#000',
      '#000',
      '#000',
      '#000', // Darkest pink
    ],
    gray: [
      '#f8f9fa', // 가장 연한 회색
      '#f1f3f5', // 연한 회색
      '#e9ecef',
      '#dee2e6',
      '#ced4da',
      '#adb5bd', // 기본 회색
      '#868e96',
      '#495057',
      '#343a40',
      '#212529', // 가장 진한 회색
    ],
  },
  primaryColor: 'primary',
  fontSizes : {
    xs: '10px',
    sm: '12px',
    md: '14px',
    lg: '16px',
    xl: '18px',
    xxs : '3px',
  },
  radius : {
    xs: '0.2em',
    sm: '0.4em',
    md: '0.6em',
    lg: '0.8em',
    xl: '1.0em',
  },
  fontFamily: fontFamily,
  shadows : {
    xs: '0px 1px 2px rgba(0, 0, 0, 0.15)',
    sm: '0px 2px 4px rgba(0, 0, 0, 0.2)',
    md: '0px 4px 8px rgba(0, 0, 0, 0.25)',
    lg: '0px 8px 16px rgba(0, 0, 0, 0.3)',
    xl: '0px 16px 32px rgba(0, 0, 0, 0.35)',
  },
});


export function ThemeProvider( { children } : PropsWithChildren ) {
  return (
    <MantineProvider theme={theme} >
      {children}
    </MantineProvider>
  )
}
