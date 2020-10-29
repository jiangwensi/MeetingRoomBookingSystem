import React from 'react';

import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import withMediaQuery from 'hoc/withMediaQuery';

const styles = {
	header: {
		marginTop: '5em',
		marginBottom: '0.5em'
		// color: theme.palette.text.primary
	},
	colorPrimary: {
		// backgroundColor: theme.palette.success.main
	},
	tab: {
		fontWeight: 'bold',
		width: '50%',
		maxWidth: '300px'
	},
	textColorInherit: {
		opacity: 1
	},
	inputLabelGrid: {
		marginRight: '1em'
	}
};

const layout1 = (ChildrenComponent) => {
	class ComposedComponent extends React.Component {
		render() {

			return (
				<Grid container justify='center' style={{ zIndex: 2000 }}>
					<Grid
						// direction='column'
						// alignItems='center'
						style={{ marginTop: this.props.matchesXS ? '5em' : '10em' }}
						width={
							this.props.matchesXS
								? '100%'
								: this.props.matchesSM
								? '75%'
								: this.props.matchesMD
								? '50%'
								: '30%'
						}>
						<ChildrenComponent {...this.props} />
					</Grid>

				</Grid>
			);
		}
	}
	//https://stackoverflow.com/a/61560866
	return withStyles(styles)(
		withMediaQuery([
			[
				'matchesXS',
				(theme) => theme.breakpoints.down('xs')
				// ,
				// {
				// 	defaultMatches: true
				// }
			]
		])(ComposedComponent)
	);
};

export default layout1;
