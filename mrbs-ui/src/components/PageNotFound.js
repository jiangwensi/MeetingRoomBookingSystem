import React from 'react';
import requireAuth from 'hoc/requireAuth';
import layout1 from 'components/layout/layout1';
import { Link, Grid, Typography } from '@material-ui/core/';
import * as ROUTE_CONST from 'utils/const';

function PageNotFound(props) {
	return (
		<>
			<Grid container direction='column' align-items='center' spacing={3}>
				<Grid item>
					<Typography variant='h4'>Something went wrong...</Typography>
				</Grid>
				<Grid item align='center'>
					<Link href={ROUTE_CONST.ROUTE_HOME}>Back to Home</Link>
				</Grid>
			</Grid>
		</>
	);
}

export default requireAuth(layout1(PageNotFound));
