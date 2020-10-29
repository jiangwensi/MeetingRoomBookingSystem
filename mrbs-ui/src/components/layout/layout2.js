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

const layout2 = (ChildrenComponent) => {
	class ComposedComponent extends React.Component {
		render() {

			return (
				<Grid
					container
					justify='center'
					style={{
						zIndex: 2000,
						width: '100%',
						paddingTop: '10em'
					}}>
						<ChildrenComponent {...this.props} />
				</Grid>
			);
		}
	}

	//https://stackoverflow.com/a/61560866
	return withStyles(styles)(
		withMediaQuery([['matchesXS', (theme) => theme.breakpoints.down('xs')]])(ComposedComponent)
	);
};

export default layout2;
