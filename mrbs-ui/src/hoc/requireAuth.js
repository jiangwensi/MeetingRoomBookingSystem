import React, { useLayoutEffect } from 'react';
import { connect } from 'react-redux';

import { ROUTE_SIGN_IN } from 'utils/const';

export default (ChildComponent) => {
	function ComposedComponent(props) {
		useLayoutEffect(() => {
			shouldNavigateAway();
		});

		function shouldNavigateAway() {
			// if (!sessionStorage.getItem('authToken')) {
			if (!props.authToken) {
				props.history.push(ROUTE_SIGN_IN);
			} else {
			}
		}
		return <ChildComponent {...props} />;
	}

	function mapStateToProps(state) {
		return { authToken: state.authReducer.authToken };
	}

	return connect(mapStateToProps)(ComposedComponent);
};
