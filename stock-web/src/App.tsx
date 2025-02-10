import { ThemeProvider } from '@/components';
import { BrowserRouter } from "react-router";
import Routes from './pages/routes';



export function App() {
  return (
    <BrowserRouter>
      <ThemeProvider>
        <Routes/>
      </ThemeProvider>
    </BrowserRouter>
  )
}
