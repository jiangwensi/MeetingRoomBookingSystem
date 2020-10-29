import * as ACTION_CONST from 'utils/const';

export const drawerClickAway = () => {
	return {
		type: ACTION_CONST.DRAWER_CLICK_AWAY,
		payload: { openMenu: false }
	};
};

export const toggleMobile = (current) => {
	return {
		type: ACTION_CONST.TOGGLE_DRAWER,
		payload: { openMenu: !current }
	};
};
