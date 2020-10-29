import React, { useState } from 'react';
import { TextField, Grid } from '@material-ui/core';
import { MyButton } from 'components/element';
import { MyCheckbox } from 'components/element';

function SearchBar(props) {
	const [name, setName] = useState('');
	const [active, setActive] = useState(['Yes', 'No']);

	return (
		<Grid container justify='center'>
			<Grid item xs={12} md={8}>
				<TextField
					variant='outlined'
					label='Org Name'
					value={name}
					fullWidth
					onChange={(event) => setName(event.target.value)}></TextField>
			</Grid>
			<Grid item xs={12} md={3}>
				<MyCheckbox
					options={['Yes', 'No']}
					value={active}
					setValue={setActive}
					grouplabel='Select Active'></MyCheckbox>
			</Grid>
			<Grid item xs={12} md={1}>
				<Grid container alignItems='center' justify='center' style={{ height: '100%' }}>
					<Grid item>
						<MyButton label='Search' onClick={() => props.searchHandler(name, active)}></MyButton>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
}

export default SearchBar;
