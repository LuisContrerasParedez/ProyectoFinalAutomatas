import { extendTheme } from '@chakra-ui/react'
import type { ThemeConfig } from '@chakra-ui/react'

const config: ThemeConfig = { initialColorMode: 'light', useSystemColorMode: false }

export const theme = extendTheme({
  config,
  fonts: {
    heading: `'Inter', system-ui, sans-serif`,
    body: `'Inter', system-ui, sans-serif`,
  },
  colors: {
    brand: {
      50:  '#f0fff4',
      100: '#c6f6d5',
      200: '#9ae6b4',
      300: '#68d391',
      400: '#48bb78',
      500: '#38a169', // verde principal
      600: '#2f855a',
      700: '#276749',
      800: '#22543d',
      900: '#1c4532',
    },
    wood: {
      50:'#F8F5F2', 200:'#E8DED3', 400:'#C8A98B', 600:'#9B7351', 800:'#704C2E'
    }
  },
  components: {
    Card: {
      baseStyle: { container: { borderRadius: '2xl', shadow: 'md' } }
    },
    Button: {
      defaultProps: { colorScheme: 'brand' }
    },
    Badge: { baseStyle: { borderRadius: 'full' } }
  }
})
