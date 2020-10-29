import React, { useState, Component } from 'react';
import { TextField, Grid } from '@material-ui/core';
import { MyButton } from 'components/element';

class SearchBar extends Component {
	state = {
		roomName: '',
		fromTime: '',
		toTime: ''
	};

	render = () => (
		<Grid container justify='space-evenly'>
			<Grid item xs={12} md={3}>
				<TextField
					variant='outlined'
					label='Room Name'
					value={this.state.roomName}
					fullWidth
					onChange={(event) => this.setState({ roomName: event.target.value })}></TextField>
			</Grid>
			<Grid item xs={12} md={3}>
				<TextField
					variant='outlined'
					label='From Time'
					value={this.state.fromTime}
					fullWidth
					onChange={(event) => this.setState({ fromTime: event.target.value })}></TextField>
			</Grid>
			<Grid item xs={12} md={3}>
				<TextField
					variant='outlined'
					label='To Time'
					value={this.state.toTime}
					fullWidth
					onChange={(event) => this.setState({ toTime: event.target.value })}></TextField>
			</Grid>
			<Grid item xs={12} md={2}>
				<Grid container alignItems='center' justify='center' style={{ height: '100%' }}>
					<Grid item>
						<MyButton
							label='Search'
							onClick={() =>
								this.props.searchHandler(this.state.roomName, this.state.fromTime, this.state.toTime)
							}></MyButton>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
}

export default SearchBar;
