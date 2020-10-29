import React, { Component } from 'react';
import DateFnsUtils from '@date-io/date-fns';
// import { MuiPickersUtilsProvider, KeyboardTimePicker, KeyboardDatePicker } from '@material-ui/pickers';
import { DeleteOutline, AddCircleOutlineOutlined } from '@material-ui/icons';
import { MyTextField } from 'components/element';
import {
	Grid,
	Typography,
	TextField,
	FormControl,
	FormLabel,
	RadioGroup,
	FormControlLabel,
	List,
	ListItem,
	ListItemText,
	MenuItem,
	Select,
	InputLabel
} from '@material-ui/core';
import { isEqualObject } from 'utils';

export class BlockTimeslots extends Component {
	render() {
		console.log(this.props.blockedTimeslots);
		const display = this.props.blockedTimeslots.map((e, i) => (
			<Grid key={e.type + e.day + e.from + e.to}>
				<Timeslot
					timeslot={e}
					setBlockedTimeslots={this.props.setBlockedTimeslots}
					blockedTimeslots={this.props.blockedTimeslots}
					index={i}
					deleteTimeslotHandler={this.props.deleteTimeslotHandler}></Timeslot>
			</Grid>
		));
		return display;
	}
}

class Timeslot extends Component {
	state = {
		timeslot: this.props.timeslot
	};

	options = ['Once', 'Daily', 'Weekly', 'Monthly'];

	componentDidUpdate = (prevProps, prevState) => {
		console.log(prevState.timeslot, this.state.timeslot);
		if (!isEqualObject(prevState.timeslot, this.state.timeslot)) {
			console.log('not equal', this.state.timeslot);
			// this.props.setTimeslots([...this.props.timeslots, this.state.timeslot]);
			this.props.setBlockedTimeslots(
				this.props.blockedTimeslots.map((e, i) => {
					console.log(e, i);
					if (i === this.props.index) {
						console.log(this.state.timeslot);
						return this.state.timeslot;
					} else {
						return e;
					}
				})
			);
		}
	};

	render() {
		return (
			<form>
				<Grid container justify='space-between' style={{ marginTop: '1em' }}>
					<Grid item xs={12} md={2}>
						<FormControl variant='outlined' style={{ width: '100%' }}>
							<InputLabel id={`typeLabel ${this.props.index}`}>Type</InputLabel>
							<Select
								label='Type'
								value={this.state.timeslot.type}
								onChange={(e) =>
									this.setState({
										timeslot: {
											...this.state.timeslot,
											type: e.target.value
										}
									})
								}>
								{this.options.map((e) => (
									<MenuItem value={e} key={e}>
										{e}
									</MenuItem>
								))}
							</Select>
						</FormControl>
					</Grid>
					<Grid item xs={12} md={2}>
						<MyTextField
							label='From'
							type='time'
							value={this.state.timeslot.timeFrom}
							onChange={(e) =>
								this.setState({
									timeslot: {
										...this.state.timeslot,
										timeFrom: e.target.value
									}
								})
							}
						/>
					</Grid>
					<Grid item xs={12} md={2}>
						<MyTextField
							label='To'
							value={this.state.timeslot.timeTo}
							type='time'
							onChange={(e) =>
								this.setState({
									timeslot: {
										...this.state.timeslot,
										timeTo: e.target.value
									}
								})
							}
						/>
					</Grid>
					<Grid item xs={12} md={3}>
						{this.state.timeslot.type.toUpperCase() !== 'DAILY' && (
							<MyTextField
								label='Day'
								type='date'
								value={this.state.timeslot.day}
								onChange={(e) =>
									this.setState({
										timeslot: {
											...this.state.timeslot,
											day: e.target.value
										}
									})
								}
							/>
						)}
					</Grid>
					<Grid item xs={12} md={1}>
						<Grid container direction='column' justify='center'>
							<Grid item>
								<DeleteOutline
									fontSize='large'
									onClick={() => this.props.deleteTimeslotHandler(this.props.index)}
								/>
							</Grid>
						</Grid>
					</Grid>
				</Grid>
			</form>
		);
	}
}
