import React, { useState } from 'react';
import { TextField, Grid } from '@material-ui/core';
import { MyButton } from 'components/element';
import { MyCheckbox } from 'components/element';

function SearchBar(props) {
	const [name, setName] = useState('');
	const [email, setEmail] = useState('');
	const [active, setActive] = useState(['Yes', 'No']);
	const [verified, setVerified] = useState(['Yes', 'No']);
	const [roles, setRoles] = useState(props.options);

	return (
		<Grid container justify='space-evenly'>
			<Grid item xs={12} md={2}>
				<TextField
					variant='outlined'
					label='Name'
					fullWidth
					onChange={(event) => setName(event.target.value)}></TextField>
			</Grid>
			<Grid item xs={12} md={2}>
				<TextField
					variant='outlined'
					label='Email'
					fullWidth
					onChange={(event) => setEmail(event.target.value)}></TextField>
			</Grid>
			<Grid item xs={12} md={2}>
				<MyCheckbox
					options={props.options}
					value={roles}
					setValue={setRoles}
					grouplabel='Select Role'></MyCheckbox>

				{/* <TextField
					variant='outlined'
					label='Role'
					fullWidth
					onChange={(event) => setRoles(event.target.value)}></TextField> */}
			</Grid>
			<Grid item xs={12} md={2}>
				<MyCheckbox
					options={['Yes', 'No']}
					value={verified}
					setValue={setVerified}
					grouplabel='Select Verified'></MyCheckbox>
				{/* <TextField
					variant='outlined'
					label='Active'
					fullWidth
					onChange={(event) => setRoles(event.target.value)}></TextField> */}
			</Grid>
			<Grid item xs={12} md={2}>
				<MyCheckbox
					options={['Yes', 'No']}
					value={active}
					setValue={setActive}
					grouplabel='Select Active'></MyCheckbox>
				{/* <TextField
					variant='outlined'
					label='Active'
					fullWidth
					onChange={(event) => setRoles(event.target.value)}></TextField> */}
			</Grid>
			<Grid item xs={12} md={1}>
				<Grid container alignItems='center' justify='center' style={{ height: '100%' }}>
					<Grid item>
						<MyButton
							label='Search'
							onClick={() => props.searchHandler(name, email, roles, active, verified)}></MyButton>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
}

export default SearchBar;
