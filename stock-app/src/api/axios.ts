type FetchMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

interface FetchOptions {
  method?: FetchMethod;
  headers?: Record<string, string>;
  body?: any;
  params?: Record<string, string | number | boolean>;
}

export const axios = async <T = any>(
  url: string,
  options: FetchOptions = {}
): Promise<T | undefined> => {
  const { method = 'GET', headers = {}, body, params } = options;
  url = "http://121.133.57.68:50000/" + url;

  // Construct query string if params are provided
  const queryString = params
      ? `?${new URLSearchParams(
            Object.entries(params).reduce<Record<string, string>>(
                (acc, [key, value]) => {
                    acc[key] = String(value);
                    return acc;
                },
                {}
            )
        ).toString()}`
      : '';

  const requestUrl = `${url}${queryString}`;

  const defaultHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
      Accept: 'application/json',
  };

  try {
      const response = await fetch(requestUrl, {
          method,
          headers: { ...defaultHeaders, ...headers },
          body: body ? JSON.stringify(body) : undefined,
      });

      const contentType = response.headers.get('Content-Type');
      let responseBody: T;

      // Parse JSON or text based on content type
      if (contentType && contentType.includes('application/json')) {
          responseBody = await response.json();
      } else {
          responseBody = (await response.text()) as unknown as T;
      }

      if (response.ok) {
        return responseBody;
      }


  } catch {
  }
};


