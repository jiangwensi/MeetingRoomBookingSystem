import React from 'react';
import { Grid, Typography } from '@material-ui/core';

function SearchResultTitleBar(props) {
	return (
		<Grid container style={{ marginTop: '3em' }}>
			{/* <Grid item md={2}>
				<Typography variant='h6' align='center'>
					ID
				</Typography>
			</Grid> */}
			<Grid item md={3}>
				<Typography variant='h6' align='center'>
					Email
				</Typography>
			</Grid>
			<Grid item md={1}>
				<Typography variant='h6' align='center'>
					Verified
				</Typography>
			</Grid>
			<Grid item md={3}>
				<Typography variant='h6' align='center'>
					Name
				</Typography>
			</Grid>
			<Grid item md={2}>
				<Typography variant='h6' align='center'>
					Roles
				</Typography>
			</Grid>
			<Grid item md={2}>
				<Typography variant='h6' align='center'>
					Active
				</Typography>
			</Grid>
			<Grid item md={1}>
				<Typography variant='h6' align='center'>
					Action
				</Typography>
			</Grid>
		</Grid>
	);
}

export default SearchResultTitleBar;
