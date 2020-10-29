import { createMuiTheme } from '@material-ui/core/styles';

export default createMuiTheme({
	props: {
		MuiButtonBase: {
			disableRipple: true
		}
	},
	palette: {
		primary: {
			light: '#ccffcc',
			main: '#7BC342',
			dark: '#2C5700'
			// dark: '#ffffff'
		},
		secondary: {
			light: '#FFEC94',
			main: '#FFC605',
			dark: '#FF9900'
		}
	}
});
