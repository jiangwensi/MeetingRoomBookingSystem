import React, { useState, useRef } from 'react';
import Modal from '@material-ui/core/Modal';
import { Typography, Button } from '@material-ui/core';
import { useEffect } from 'react';

//need to study this code
//https://overreacted.io/zh-hans/making-setinterval-declarative-with-react-hooks/
const useInterval = (callback, delay) => {
	const savedCallback = useRef();

	// save callback
	useEffect(() => {
		savedCallback.current = callback;
	});

	// create interval
	useEffect(() => {
		function tick() {
			savedCallback.current();
		}
		if (delay !== null) {
			let id = setInterval(tick, delay);
			return () => clearInterval(id);
		}
	}, [delay]);
};

const SessionTimeoutModel = ({ open, handleLogout, handleClose, delay }) => {
	const [remaining, setRemaining] = useState(delay / 1000);
	useInterval(() => {
		setRemaining(remaining - 1);
	}, 1000);

	const closeHandler = () => {
		handleClose();
		setRemaining(delay / 1000);
	};

	const body = (
		<div
			style={{
				position: 'absolute',
				top: '50%',
				left: '50%',
				transform: 'translate(-50%, -50%)',
				backgroundColor: 'white'
			}}>
			<Typography variant='h5'>Your session is inactive.</Typography>
			<Typography variant='h6'>You will be signed out in {remaining} seconds.</Typography>
			<Button onClick={handleLogout}>Logout</Button>
			<Button onClick={closeHandler}>Continue</Button>
		</div>
	);

	return <Modal open={open}>{body}</Modal>;
};

export default SessionTimeoutModel;
