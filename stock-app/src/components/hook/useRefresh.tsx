import { useCallback, useState } from 'react';


interface UseRefreshProps<T> {
  fetch : () => Promise<T | undefined>;
}

export function useRefresh<T>( { fetch } : UseRefreshProps<T> ) {
  const [ data, setData ] = useState<T>();
  const [ isLoading, setLoading ] = useState<boolean>(false);

  const refresh = useCallback( async ()=>{
    try {
      setLoading(true);
      const _date = await fetch();
      setData(_date);
    }finally{
      setLoading(false);
    }

    },[fetch]);

    return {
      data,
      refresh,
      isLoading,
    }
}
