import { createContext as CreateContext, PropsWithChildren, useContext as UseContext , useState } from 'react';


interface CreateContextProps<T extends object> {
  init : T,
}

export function createContext<T extends object>( { init } : CreateContextProps<T> ) {

  type ContextType = T & { setter : <K extends keyof T>(key: K) => (next: T[K]) => void };

  const Context = CreateContext<ContextType | undefined>(undefined);
  function Provider( { children } : PropsWithChildren  ) {
    const [ value, setValue ] = useState<T>(init);

    function setter<K extends keyof T>(key: K) {
      return function (next: T[K]) {
        setValue((prev) => {
          if (!prev) return prev;

          return {
            ...prev,
            [key]: next, 
          };
        });
      };
    }

    return (
    <Context.Provider value={{ ...value, setter }} >
      {children}
    </Context.Provider>
    )
  }

  function useContext() {
    return UseContext(Context) as ContextType;
  }

  return {
    Provider,
    useContext,
  }
}