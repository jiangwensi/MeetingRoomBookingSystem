import React from 'react';

import { connect } from 'react-redux';
import {
	ClickAwayListener,
	makeStyles,
	Link,
	Hidden,
	Typography,
	Drawer,
	List,
	ListItem,
	ListItemText,
	Divider
} from '@material-ui/core';

import * as ROUTE_CONST from 'utils/const';
import { signOut } from 'myRedux/actions/auth';

const useStyles = makeStyles((theme) => ({
	paper: {
		zIndex: '2900',
		background: theme.palette.primary.main
	},
	menuList: {
		marginTop: '4em'
	},
	listItem: {
		paddingTop: '0',
		paddingBottom: '0'
	},
	menuText: {
		// marginLeft: '1em',
		color: theme.palette.common.white
	},
	menuListItemSub: {
		marginLeft: '0.5em',
		color: theme.palette.common.white
	}
}));

const MyDrawer = (props) => {
	const classes = useStyles();
	const menus = [];
	menus.push({ main: 'Bookings', link: ROUTE_CONST.ROUTE_BOOKING_SEARCH });
	menus.push({ main: 'Rooms', link: ROUTE_CONST.ROUTE_ROOM_SEARCH });
	menus.push({ main: 'Organizations', link: ROUTE_CONST.ROUTE_ORG_SEARCH });
	if (props.roles && props.roles.includes('SYSADM')) {
		menus.push({ main: 'Users', link: ROUTE_CONST.ROUTE_USER_SEARCH });
		// menus.push({ main: 'Rooms', link: ROUTE_CONST.ROUTE_ROOM_SEARCH });
	} else {
		if (props.myOrgs && props.myOrgs.length > 0) {
			menus.push({ main: 'Organizations', link: ROUTE_CONST.ROUTE_ORG_SEARCH });
		}
		// if (props.myRooms && props.myRooms.length > 0) {
		// 	menus.push({ main: 'Rooms', link: ROUTE_CONST.ROUTE_ROOM_SEARCH });
		// }
	}

	menus.push({
		main: 'Sign Out',
		onClickHandler: () => {
			props.signOut(props.history.push(ROUTE_CONST.ROUTE_SIGN_IN));
			props.setOpenMenu(false);
		}
	});

	const drawer = (
		<ClickAwayListener
			onClickAway={() => {
				if (props.openMenu) {
					props.setOpenMenu(false);
					sessionStorage.setItem('openMenu', false);
				}
			}}>
			<List className={classes.menuList}>
				{menus.map((menu) => (
					<div key={menu.main}>
						<ListItem key={menu.main} button>
							<ListItemText>
								<Link href={menu.link} onClick={menu.onClickHandler}>
									<Typography variant='h6' className={classes.menuText}>
										{menu.main}
									</Typography>
								</Link>
							</ListItemText>
						</ListItem>
						{menu.sub !== undefined &&
							menu.sub.map((sub) => (
								<ListItem key={sub} button className={classes.listItem}>
									<ListItemText className={classes.menuListItemSub}>
										<Typography variant='subtitle2'>{sub}</Typography>
									</ListItemText>
								</ListItem>
							))}
						<Divider className={classes.divider} />
					</div>
				))}
			</List>
		</ClickAwayListener>
	);
	return (
		<>
			{/* <Hidden mdUp>
				<Drawer
					open={props.openMenu}
					// open={props.openMobileMenu}
					className={classes.drawer}
					variant='persistent'
					classes={{ paper: classes.paper }}>
					{drawer}
				</Drawer>
			</Hidden> */}
			{/* <Hidden smDown> */}
			<Drawer
				open={props.openMenu}
				className={classes.drawer}
				variant='persistent'
				classes={{ paper: classes.paper }}>
				{drawer}
			</Drawer>
			{/* </Hidden> */}
		</>
	);
};

const mapStateToProps = (state) => ({
	authToken: state.authReducer.authToken,
	roles: state.authReducer.roles,
	myRooms: state.authReducer.myRooms,
	myOrgs: state.authReducer.myOrgs
});

export default connect(mapStateToProps, { signOut })(MyDrawer);
