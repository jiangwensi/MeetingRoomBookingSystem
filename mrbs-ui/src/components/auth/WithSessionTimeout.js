import React, { useState, useRef } from 'react';
import { Switch, Route } from 'react-router-dom';
import { useIdleTimer } from 'react-idle-timer';
import { connect } from 'react-redux';

import * as ROUTE_CONST from 'utils/const';
import { signOut } from 'myRedux/actions/auth';
import SessionTimeoutModel from 'components/auth/SessionTimeoutModel';
import Home from 'components/Home';
import SearchUser from 'components/user/SearchUser';
import Profile from 'components/user/Profile';
import SearchOrg from 'components/org/SearchOrg';
import CreateOrg from 'components/org/CreateOrg';
import ViewOrg from 'components/org/ViewOrg';
import EditOrg from 'components/org/EditOrg';
import EditRoom from 'components/room/EditRoom';
import CreateRoom from 'components/room/CreateRoom';
import ViewRoom from 'components/room/ViewRoom';
import SearchRoom from 'components/room/SearchRoom';
import CreateBooking from 'components/booking/CreateBooking';
import SearchBooking from 'components/booking/SearchBooking';

const WithSessionTimeout = (props) => {
	const delay = 1000 * 60 * 1;
	const idleLimit = 1000 * 60 * 10;
	const [openModel, setOpenModel] = useState(false);
	const logoutHandler = () => {
		setOpenModel(false);
		props.signOut(props.history.push(ROUTE_CONST.ROUTE_SIGN_IN));
	};

	const handleCloseModel = () => {
		setOpenModel(false);
	};

	const timerRef = useRef(null);
	const handleOnIdle = () => {
		setOpenModel(true);
		timerRef.current = setTimeout(() => {
			logoutHandler();
			timerRef.current = null;
		}, delay);
	};

	const handleOnActive = () => {};

	const handleOnAction = () => {};

	useIdleTimer({
		timeout: idleLimit,
		onIdle: handleOnIdle,
		onActive: handleOnActive,
		onAction: handleOnAction,
		debounce: 0
	});

	return (
		<>
			<Switch>
				<Route path={ROUTE_CONST.ROUTE_HOME} exact render={(props) => <Home {...props} />} />
				<Route path={ROUTE_CONST.ROUTE_USER_SEARCH} exact component={SearchUser} />
				<Route path={ROUTE_CONST.ROUTE_PROFILE} exact component={Profile} />
				<Route path={ROUTE_CONST.ROUTE_ORG_SEARCH} exact component={SearchOrg} />
				<Route path={ROUTE_CONST.ROUTE_ORG_CREATE} exact component={CreateOrg} />
				<Route path={ROUTE_CONST.ROUTE_ORG_VIEW} exact component={ViewOrg} />
				<Route path={ROUTE_CONST.ROUTE_ORG_EDIT} exact component={EditOrg} />

				<Route path={ROUTE_CONST.ROUTE_ROOM_CREATE} exact component={CreateRoom} />
				<Route path={ROUTE_CONST.ROUTE_ROOM_SEARCH} exact component={SearchRoom} />
				<Route path={ROUTE_CONST.ROUTE_ROOM_VIEW} exact component={ViewRoom} />
				<Route path={ROUTE_CONST.ROUTE_ROOM_EDIT} exact component={EditRoom} />

				<Route path={ROUTE_CONST.ROUTE_BOOKING_CREATE} exact component={CreateBooking} />
				<Route path={ROUTE_CONST.ROUTE_BOOKING_SEARCH} exact component={SearchBooking} />
			</Switch>
			<SessionTimeoutModel
				open={openModel}
				handleLogout={logoutHandler}
				handleClose={handleCloseModel}
				delay={delay}
			/>
		</>
	);
};

export default connect(null, { signOut })(WithSessionTimeout);
