import {useCallback, useEffect, useState } from 'react';


interface UseDebounceProps<T> {
  init? : T;
  delay? : number;
  callback : ( valeu? : T ) => void;
}

export function useDebounce<T>({ init, callback, delay = 500 } : UseDebounceProps<T> ) {

  const [ value, setValue ] = useState<T | undefined>(init);

  useEffect(() => {
    const handler = setTimeout(() => {
      callback(value);
    }, delay);
 
    return () => {
      clearTimeout(handler);
    };
  }, [value,  callback, delay ]);


  return {
    value,
    setValue,
  }
};