import { useState, ChangeEventHandler, useMemo } from 'react';
import { useDebouncedCallback } from '@mantine/hooks';



interface CheckResult {
  error? : string;
  valid? : string;
}

interface UseValidValueProps {
  init : string;
  checker : ( value : string ) => Promise<CheckResult | undefined>;
  delay? : number;
}

export function useValidValue( { init , checker, delay = 500 } : UseValidValueProps ) {
  const [ value, setValue ] = useState(init);
  const [ result, setResult ] = useState<CheckResult>();

  const callback = useDebouncedCallback( async ( query ) =>{
    setResult( await checker(query) );
  }, delay);

  const change : ChangeEventHandler<HTMLInputElement> = ( e ) => {
    setValue(e.target.value);
    callback(e.target.value);
  }

  const valid = useMemo(()=>{
    return result?.valid ? true : false;
  },[result])

 
  function getProps() {
    return {
      value : value,
      onChange : change,
      error : result?.error,
      description : result?.valid,
    }
  }
  

  return {
    value,
    valid,
    getProps,
  }
}