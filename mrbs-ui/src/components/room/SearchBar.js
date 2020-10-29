import React, { useState } from 'react';
import { TextField, Grid } from '@material-ui/core';
import { MyButton } from 'components/element';
import { MyCheckbox } from 'components/element';

function SearchBar(props) {
	const [roomName, setRoomName] = useState('');
	const [orgName, setOrgName] = useState('');
	const [active, setActive] = useState(['Yes', 'No']);

	return (
		<Grid container justify='space-evenly'>
			<Grid item xs={12} md={4}>
				<TextField
					variant='outlined'
					label='Room Name'
					value={roomName}
					fullWidth
					onChange={(event) => setRoomName(event.target.value)}></TextField>
			</Grid>
			<Grid item xs={12} md={4}>
				<TextField
					variant='outlined'
					label='Org Name'
					value={orgName}
					fullWidth
					onChange={(event) => setOrgName(event.target.value)}></TextField>
			</Grid>
			<Grid item xs={12} md={2}>
				<MyCheckbox
					options={['Yes', 'No']}
					value={active}
					setValue={setActive}
					grouplabel='Select Active'></MyCheckbox>
			</Grid>
			<Grid item xs={12} md={1}>
				<Grid container alignItems='center' justify='center' style={{ height: '100%' }}>
					<Grid item>
						<MyButton
							label='Search'
							onClick={() => props.searchHandler(roomName, orgName, active)}></MyButton>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
}

export default SearchBar;
