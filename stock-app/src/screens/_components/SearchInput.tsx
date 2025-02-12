import { Input, Layout } from '@ui-kitten/components';
import { useDebounce } from '@/components';


interface SearchInputProps {
  onChange : ( keyword? : string ) => void;
}

export function SearchInput( { onChange } : SearchInputProps ) {
  const { value, setValue } = useDebounce({
    init : '',
    callback : ( value? : string ) => {
      onChange(value);
    }
  });

  return (
  <Layout style={{ padding : 5 }} >
    <Input  value={value} onChangeText={setValue} />
  </Layout>
  )
}