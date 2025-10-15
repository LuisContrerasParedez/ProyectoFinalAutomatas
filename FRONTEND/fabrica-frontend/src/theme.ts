// theme.ts
import { extendTheme } from '@chakra-ui/react'
import type { ThemeConfig } from '@chakra-ui/react'

const config: ThemeConfig = { initialColorMode: 'light', useSystemColorMode: false }

export const theme = extendTheme({
  config,
  fonts: {
    heading: `-apple-system, BlinkMacSystemFont, 'SF Pro Display', 'SF Pro Text', 'Inter', system-ui, sans-serif`,
    body:    `-apple-system, BlinkMacSystemFont, 'SF Pro Text', 'Inter', system-ui, sans-serif`,
  },
  colors: {
    accent: {
      500: '#0A84FF', // Cupertino blue
      600: '#0060DF',
    },
    gray: {
      50:'#FAFAFA',100:'#F5F5F7',200:'#E5E5EA',300:'#D1D1D6',
      400:'#C7C7CC',500:'#8E8E93',600:'#636366',700:'#48484A',
      800:'#3A3A3C',900:'#1C1C1E'
    },
    // si quieres, conserva tu palette 'wood' para acentos secundarios
    wood: { 50:'#F8F5F2',200:'#E8DED3',400:'#C8A98B',600:'#9B7351',800:'#704C2E' }
  },
  styles: {
    global: {
      body: { bg: 'white', color: 'gray.800' }
    }
  },
  components: {
    Button: {
      baseStyle: { borderRadius: 'full' },
      defaultProps: { colorScheme: 'accent' }
    },
    Badge: { baseStyle: { borderRadius: 'full' } },
    Card:  { baseStyle: { container: { borderRadius: '2xl', shadow: 'sm', borderWidth: '1px', borderColor: 'gray.200' } } }
  }
})
