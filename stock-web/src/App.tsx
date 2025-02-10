import { ThemeProvider } from '@/components';
import { BrowserRouter } from "react-router";
import RootRoutes from './pages/routes';

     
export function App() {
  return (
    <BrowserRouter>
      <ThemeProvider>
        <RootRoutes/>
      </ThemeProvider>
    </BrowserRouter>
  )
}
