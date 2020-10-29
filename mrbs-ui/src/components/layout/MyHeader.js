//react
import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import Link from '@material-ui/core/Link';

//@material-ui
import { makeStyles, useTheme, useMediaQuery, AppBar, Toolbar, Typography, IconButton } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/MenuRounded';

import { signOut } from 'myRedux/actions/auth';

//custom
import * as ROUTE_CONST from 'utils/const';

const useStyles = makeStyles((theme) => ({
	appBar: {
		zIndex: 3000
	},
	iconButton: {
		color: theme.palette.common.white,
		'&:hover': {
			background: 'inherit'
		}
	},
	header: {
		color: theme.palette.common.white,
		fontWeight: 'bold'
	},
	version: {
		color: theme.palette.common.white,
		paddingLeft: '1em'
	},
	avatar: {
		'&:hover': {
			background: 'inherit',
			textDecoration: 'underline'
		}
	},
	username: {
		color: theme.palette.common.white,
		// marginLeft: '1em',
		textDecoration: 'underline',
		'&:hover': {
			background: 'inherit',
			textDecoration: 'underline'
		}
	},
	signInButton: {
		color: theme.palette.common.white,
		// marginLeft: '1em',
		fontWeight: 'bold',
		textTransform: 'none',
		'&:hover': {
			background: 'inherit',
			textDecoration: 'underline'
		}
	},
	link: {
		color: theme.palette.common.white,
		// marginLeft: '1em',
		fontWeight: 'bold',
		textTransform: 'none',
		'&:hover': {
			textDecoration: 'underline'
		},
		margin: '1em'
	},
	button: {
		'&:hover': {
			background: 'inherit'
		}
	}
}));

const Header = (props) => {
	const classes = useStyles();
	const theme = useTheme();
	const matchesSM = useMediaQuery(theme.breakpoints.down('sm'));
	const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));

	const signOutHandler = () => {
		props.signOut(props.history.push(ROUTE_CONST.ROUTE_SIGN_IN));
		props.setOpenMenu(false);
	};

	useEffect(() => {
		props.setOpenMenu(sessionStorage.getItem('openMenu') === 'true' ? true : false);
		// props.setOpenMobileMenu(sessionStorage.getItem('openMobileMenu') === 'true' ? true : false);
	});

	return (
		<AppBar className={classes.appBar} color='primary'>
			<Toolbar>
				{props.authToken && (
					<IconButton
						className={classes.iconButton}
						disableFocusRipple={true}
						onClick={() => {
							// let openMenu = props.openMenu;
							// let openMobileMenu = props.openMobileMenu;
							sessionStorage.setItem('openMenu', !props.openMenu);
							// sessionStorage.setItem('openMobileMenu', !openMobileMenu);
							props.setOpenMenu(!props.openMenu);
							// props.setOpenMobileMenu(!openMobileMenu);
						}}>
						<MenuIcon fontSize='large' />
					</IconButton>
				)}

				<Typography className={classes.header} variant='h6'>
					{matchesSM ? 'MRBS' : 'Meeting Room Booking System'}
				</Typography>
				<Typography className={classes.version}>
					<small>{matchesSM ? '' : 'v1.0.0'}</small>
				</Typography>
				<div style={{ marginLeft: 'auto', textAlign: 'center' }}>
					{/* {matchesXS ? null : (
						<Button disableFocusRipple className={classes.avatar}>
							<Avatar alt='JWS' src='' />
						</Button>
					)} */}
					{props.authToken && (
						<>
							<Link href={ROUTE_CONST.ROUTE_PROFILE} color='textPrimary' classes={{ root: classes.link }}>
								<small style={{ whiteSpace: 'nowrap' }}>{props.username}</small>
							</Link>
							{matchesXS && <br />}
							<Link color='textPrimary' classes={{ root: classes.link }} onClick={signOutHandler}>
								<small style={{ whiteSpace: 'nowrap' }}>Sign Out</small>
							</Link>
						</>
					)}
				</div>
			</Toolbar>
		</AppBar>
	);
};

const mapStateToProps = (state) => ({ authToken: state.authReducer.authToken, username: state.authReducer.username });

export default connect(mapStateToProps, { signOut })(Header);
