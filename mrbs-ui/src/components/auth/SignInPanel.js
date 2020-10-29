import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';

import { makeStyles } from '@material-ui/core/styles';
import { Button, Grid, Box, TextField, Checkbox, FormControlLabel, Link } from '@material-ui/core';

import { signIn } from 'myRedux/actions/auth';
import { validateEmail } from 'utils/validation';
import { ROUTE_BOOKING_SEARCH, ROUTE_REQUEST_RESET_FORGOTTEN_PASSWORD, ROUTE_RESET_PASSWORD } from 'utils/const';

// import TabPanel from '@material-ui/lab/TabPanel';

const useStyles = makeStyles((theme) => ({
	link: {
		display: 'inline-block',
		marginTop: '1em'
	},
	button: {
		color: 'white',
		'&:hover': {
			backgroundColor: theme.palette.primary.main
		}
	},
	errorBox: {
		backgroundColor: theme.palette.secondary.light
	}
}));

function SignInPanel(props) {
	//useState()
	const [email, setEmail] = useState(localStorage.getItem('signInEmail') ? localStorage.getItem('signInEmail') : '');
	const [emailHelper, setEmailHelper] = useState('');
	const [password, setPassword] = useState(
		localStorage.getItem('signInPassword') ? localStorage.getItem('signInPassword') : ''
	);
	const [passwordHelper, setPasswordHelper] = useState('');
	const [rememberPassword, setRememberPassword] = useState(localStorage.getItem('rememberPassword') === 'true');

	//useEffect()
	useEffect(() => {
		if (props.signUpState.email) {
			setEmail(props.signUpState.email);
		}
	}, [props.signUpState.email]);

	useEffect(() => {
		if (email || password) {
			checkEmail();
		}
	}, [email]);

	useEffect(() => {
		if (email || password) {
			checkPassword();
		}
	}, [password]);

	function checkPassword() {
		if (!password) {
			setPasswordHelper('Password cannot be empty');
			return false;
		} else {
			setPasswordHelper('');
			return true;
		}
	}

	function checkEmail() {
		if (validateEmail(email)) {
			setEmailHelper('');
			return true;
		} else {
			setEmailHelper('Email is not valid.');
			return false;
		}
	}

	const signInHandler = () => {
		const validEmailFormat = checkEmail();
		const validPasswordFormat = checkPassword();
		if (validEmailFormat && validPasswordFormat) {
			localStorage.setItem('rememberPassword', rememberPassword);
			if (rememberPassword) {
				localStorage.setItem('signInEmail', email);
				localStorage.setItem('signInPassword', password);
			} else {
				localStorage.removeItem('signInEmail');
				localStorage.removeItem('signInPassword');
			}
			props.signIn(email, password, () => {
				if (!props.signInError) {
					props.history.push(ROUTE_BOOKING_SEARCH);
				}
			});
		}
	};

	const classes = useStyles();

	return (
		<Grid container justify="center" alignItems='center' spacing={3}>
			<Grid item xs={12} sm={10} md={6} lg={5}>
				<Grid container direction="column">

					{props.errorMessage && (
						<Grid
							item
							component={Box}
							width='95%'
							textAlign='left'
							color='text.primary'
							classes={{ root: classes.errorBox }}>
							{props.errorMessage}
						</Grid>
					)}
					<Grid item component={Box} width='100%' textAlign='right'>
						<TextField
							id='email'
							error={emailHelper !== ''}
							helperText={emailHelper}
							variant='outlined'
							fullWidth
							margin='normal'
							label='Email'
							value={email}
							onChange={(event) => {
								setEmail(event.target.value);
							}}
						/>
						<TextField
							type='password'
							error={passwordHelper != ''}
							helperText={passwordHelper}
							id='password'
							variant='outlined'
							fullWidth
							margin='normal'
							label='Password'
							value={password}
							onChange={(event) => {
								setPassword(event.target.value);
							}}
						/>
						<FormControlLabel
							control={
								<Checkbox checked={rememberPassword} onChange={() => setRememberPassword(!rememberPassword)} />
							}
							label='Remember Password'
							size='small'
						/>
						<Button
							variant='contained'
							color='primary'
							fullWidth
							className={classes.button}
							onClick={signInHandler}
							size='large'>
							Sign In
						</Button>
						<Link
							color='textPrimary'
							underline='always'
							href={ROUTE_REQUEST_RESET_FORGOTTEN_PASSWORD}
							size='small'
							classes={{ root: classes.link }}>
							Forget Password
						</Link>
						<br />
						<Link
							color='textPrimary'
							underline='always'
							href={ROUTE_RESET_PASSWORD}
							size='small'
							classes={{ root: classes.link }}>
							Reset Password
						</Link>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
}

const mapStateToProps = (state) => {
	return { signUpState: state.signUpReducer, errorMessage: state.authReducer.errorMessage };
};

export default connect(mapStateToProps, { signIn })(SignInPanel);
