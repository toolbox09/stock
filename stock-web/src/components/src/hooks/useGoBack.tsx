import { useCallback } from 'react';
import { useNavigate } from 'react-router';

export function useGoBack( defaultPath : string ) {
  const navigate = useNavigate();

  const goBack = useCallback(()=>{
    try {
      navigate(-1);
    }catch {
      navigate(defaultPath);
    }
  },[defaultPath])

  return {
    goBack
  }
}