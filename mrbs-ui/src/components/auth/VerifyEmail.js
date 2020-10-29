import React, { useEffect } from 'react';
import { connect } from 'react-redux';

import { makeStyles, useTheme } from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import {MyButton, StatusMessage} from 'components/element';

import { verifyEmail } from 'myRedux/actions/auth';

import { ROUTE_SIGN_IN } from 'utils/const';


const VerifyEmail = (props) => {

	const theme = useTheme();
	const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));
	const goToLoginPage = () => {
		props.history.push(ROUTE_SIGN_IN);
	};

	useEffect(() => {
		const token = new URLSearchParams(props.location.search).get('token');
		// const token = props.match.params.token;
		props.verifyEmail(token);
	}, []);

	return (
		<Grid container justify='center' alignItems='center' style={{ zIndex: 2000,marginTop: matchesXS ? '5em' : '10em' }}>
			<Grid item>
				<Grid container direction='column' >
					{props.message && (
						<Grid item component={Box}>
							<StatusMessage type="success" message={props.message}/>
						</Grid>
					)}
					{props.errorMessage && (
						<Grid item component={Box}>
							<StatusMessage type="failed" errorMessage={props.errorMessage}/>
						</Grid>
					)}
					<br />
					<Grid item width='85%'>
						<MyButton
							label="Back to SignIn"
							onClick={goToLoginPage}>

						</MyButton>
					</Grid>
				</Grid>
			</Grid>

		</Grid>
	);
};

const mapStateToProps = (state) => ({
	errorMessage: state.verifyEmailReducer.errorMessage,
	message: state.verifyEmailReducer.message
});

export default connect(mapStateToProps, { verifyEmail })(VerifyEmail);
