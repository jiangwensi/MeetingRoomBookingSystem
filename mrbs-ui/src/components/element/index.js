import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { EditOutlined, DeleteOutlined, VisibilityOutlined } from '@material-ui/icons';
import {
	TextField,
	FormLabel,
	Checkbox,
	FormGroup,
	Grid,
	Box,
	Radio,
	RadioGroup,
	FormControl,
	FormControlLabel,
	Typography,
	Button,
	Card,
	CardActions,
	CardContent,
	Modal,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
} from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
	errorBox: {
		backgroundColor: theme.palette.secondary.light,
		marginTop: '1em'
	},
	successBox: {
		backgroundColor: theme.palette.primary.light
	},
	formcontrollabel: {
		marginLeft: 0,
		marginRight: '5px'
	},
	radioGroup: {
		marginLeft: '5px'
	},
	radio: {
		paddingRight: '1px'
	},
	checkbox: {
		paddingRight: '1px'
	},
	button: {
		background: theme.palette.primary.main,
		color: theme.palette.background.paper,
		'&:hover': {
			background: theme.palette.primary.main
		}
	},
	buttonCancel: {
		background: theme.palette.grey,
		color: theme.palette.background.paper,
		'&:hover': {
			backgroundColor: theme.palette.grey
		}
	},
	buttonDelete: {
		background: theme.palette.warning.main,
		color: theme.palette.background.paper,
		'&:hover': {
			backgroundColor: theme.palette.warning.main
		}
	},
	tableCellNoBorder: {
		borderBottom: 'none'
	},
	hover: {
		'&:hover': {
			color: 'black',
			cursor: 'pointer'
		}
	},
	TableRowColumn:{
		whiteSpace: 'normal',
		wordWrap: 'break-word'
	}
}));

export const OutputText = (props) => {
	return (
		<Grid container justify='center' alignItems='center' style={{ height: '100%' }}>
			<Grid item>
				<Typography align='center'>{props.value}</Typography>
			</Grid>
		</Grid>
	);
};

export const LabeledOutputText = (props) => {
	return (
		<Grid container justify='center' alignItems='center' style={{ height: '90%', margin: '1em' }}>
			<Grid item xs={6}>
				<Typography align='center' variant='h6'>
					{props.label}
				</Typography>
			</Grid>
			<Grid item xs={6}>
				<Typography align='center' variant='h6'>
					{props.value}
				</Typography>
			</Grid>
		</Grid>
	);
};

export const MyDataList = (props) => {
	const classes = useStyles();
	const data = props.data;
	const ignoreData = props.ignoreData ? props.ignoreData : [];

	return (
		<TableContainer>
			<Table>
				<TableHead>
					<TableRow>
						{data[0] &&
							Object.keys(data[0])
								.filter((x) => !ignoreData.includes(x))
								.map((e) => (
									<TableCell key={e}>
										<Typography variant='h6'>{e}</Typography>
									</TableCell>
								))}
					</TableRow>
				</TableHead>
				<TableBody>
					{data.map((
						element //e is orgData
					) => (
						<TableRow key={element[Object.keys(element)[0]]}>
							{Object.keys(element)
								.filter((x) => x !== 'Actions' && !ignoreData.includes(x))
								// .filter((x) => x.indexOf('_') !== 0)
								.map((f) => {
									return <TableCell key={f} align="center">{element[f]}</TableCell>;
								})}
							<TableCell align="center" >
								{element['Actions'] &&
									element['Actions'].length > 0 &&
									element['Actions'].map((e) => {
										if (e.icon === 'view') {
											return (
												<VisibilityOutlined
													key={e.icon}
													onClick={e.clickHandler}
													fontSize='default'
													className={classes.hover}></VisibilityOutlined>
											);
										} else if (e.icon === 'edit') {
											return (
												<EditOutlined
													key={e.icon}
													onClick={e.clickHandler}
													fontSize='default'
													className={classes.hover}
													style={{ marginLeft: '0.5em' }}></EditOutlined>
											);
										} else if (e.icon === 'delete') {
											return (
												<DeleteOutlined
													key={e.icon}
													onClick={e.clickHandler}
													fontSize='default'
													className={classes.hover}
													style={{ marginLeft: '0.5em' }}></DeleteOutlined>
											);
										} else {
											return '';
										}
									})}
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</TableContainer>
	);
};

export const MyTable = (props) => {
	const classes = useStyles();
	const data = props.data;
	const editableFields = props.editableFields;
	useEffect(() => {
		if (editableFields && editableFields.length > 0) {
			editableFields.map((a) => a.setValue(data[a.key]));
		}
	}, []);
	return (
		<TableContainer>
			<Table>
				<TableBody>
					{Object.keys(data).map((key) => {
						return (
							<TableRow key={key}>
								<TableCell
									component='th'
									scope='row'
									align='right'
									// {...props}
									classes={{ root: classes.tableCellNoBorder }}>
									<Typography variant='h6'>{key}</Typography>
								</TableCell>
								{(!editableFields || !editableFields.map((a) => a.key).includes(key)) && (
									<TableCell
										component='td'
										align='left'
										classes={{ root: classes.tableCellNoBorder }}>
										<Typography variant='h6'>{data[key]}</Typography>
									</TableCell>
								)}
								{editableFields && editableFields.map((a) => a.key).includes(key) && (
									<TableCell
										component='td'
										align='left'
										classes={{ root: classes.tableCellNoBorder }}>
										<MyInputField
											/*value={data[key]}*/
											value={editableFields.find((e) => e.key === key).value}
											onChange={(event) =>
												editableFields.find((e) => e.key === key).setValue(event.target.value)
											}
											variant='outlined'
										/>
										{/* <Typography variant='h6'>{data[key]}</Typography> */}
									</TableCell>
								)}
							</TableRow>
						);
					})}
				</TableBody>
			</Table>
		</TableContainer>
	);
};

export const MyRadio = (props) => {
	const classes = useStyles();
	return <Radio disableRipple classes={{ root: classes.radio }} {...props} color='primary' />;
};

export const MyInputField = (props) => {
	const updateValue = (val) => {
		props.setvalue(val);
	};
	return (
		<>
			<Grid container justify='center' alignItems='center' style={{ height: '100%' }}>
				<Grid item xs={12}>
					<TextField
						variant='outlined'
						inputProps={{ style: { textAlign: 'center' } }}
						value={props.value}
						onChange={(event) => updateValue(event.target.value)}
						fullWidth
						{...props}></TextField>
				</Grid>
			</Grid>
		</>
	);
};

export const MyCheckbox = (props) => {
	const { options, value, setValue } = props;
	const handleChange = (event) => {
		if (value.includes(event.target.name)) {
			const index = value.indexOf(event.target.name);
			value.splice(index, 1);
		} else {
			value.push(event.target.name);
		}
		setValue([...value]);
	};
	const classes = useStyles();

	return (
		<Grid
			container
			direction='column'
			justify='center'
			alignItems='center'
			style={{ height: '100%', marginLeft: '15px' }}>
			{props.grouplabel && (
				<Grid item>
					<FormLabel component='legend'>{props.grouplabel}</FormLabel>
				</Grid>
			)}
			<Grid item>
				<Grid container direction='row'>
					<FormGroup row>
						{options.map((e) => {
							return (
								<Grid item key={e}>
									<FormControlLabel
										size='small'
										label={e}
										control={
											<Checkbox
												classes={{ root: classes.checkbox }}
												checked={value.includes(e)}
												onChange={(event) => handleChange(event)}
												name={e}
												color='primary'
											/>
										}
									/>
								</Grid>
							);
						})}
					</FormGroup>
				</Grid>
			</Grid>
		</Grid>
	);
};

export const MyModal = (props) => {
	const title = props.title;
	const message = props.message;
	const okHandler = props.okHandler ? props.okHandler : console.log('okHandler');
	const okLabel = props.okLabel;
	const noLabel = props.noLabel;
	const open = props.open;
	const setOpen = props.setOpen;
	const displayContent = props.displayContent;

	const handleClose = () => {
		setOpen(false);
	};
	const handleOK = () => {
		if (okHandler && typeof okHandler === 'function') {
			okHandler();
			setOpen(false)
		} else {
			setOpen(false);
		}
	};

	const body = (
		<Card
			style={{
				position: 'absolute',
				top: '50%',
				left: '50%',
				// marginRight: '-50%'
				transform: 'translate(-50%,-50%)'
			}}>
			<CardContent>
				<Typography align='center' variant='h5'>
					{title}
				</Typography>
				{message && (
					<Typography align='center' variant='h6'>
						{message}
					</Typography>
				)}
				{displayContent}
			</CardContent>
			<CardActions>
				{noLabel && <MyButton label={noLabel} onClick={handleClose}></MyButton>}
				{okLabel && <MyButton label={okLabel} onClick={handleOK}></MyButton>}
			</CardActions>
		</Card>
	);

	return (
		<div>
			<Modal open={open} onClose={handleClose}>
				{body}
			</Modal>
		</div>
	);
};

export const InputRadioButton = (props) => {
	const { value, setValue } = props;
	const changeHandler = (value) => {
		setValue(value === 'true' ? true : false);
	};
	const classes = useStyles();
	return (
		<Grid
			container
			justify={props.justify ? props.justify : 'center'}
			alignItems='center'
			style={{ height: '100%' }}>
			<Grid item>
				<FormControl component='fieldset' style={{ width: '100%', textAlign: 'center' }}>
					<RadioGroup
						row
						style={{ width: '100%', textAlign: 'center' }}
						classes={{ root: classes.radioGroup }}
						value={value}
						onChange={(event) => changeHandler(event.target.value)}>
						<Grid item>
							<FormControlLabel
								label={props.label1}
								value={props.value1}
								control={
									<Radio
										size='small'
										disableRipple
										classes={{ root: classes.radio }}
										color='primary'
									/>
								}
								classes={{ root: classes.formcontrollabel }}
							/>
						</Grid>
						<Grid item>
							<FormControlLabel
								label={props.label2}
								value={props.value2}
								control={
									<Radio
										size='small'
										disableRipple
										classes={{ root: classes.radio }}
										color='primary'
									/>
								}
								classes={{ root: classes.formcontrollabel }}
							/>
						</Grid>
					</RadioGroup>
				</FormControl>
			</Grid>
		</Grid>
	);
};

export const MyTextField = (props) => {
	return (
		<TextField
			{...props}
			variant='outlined'
			InputLabelProps={{
				shrink: true
			}}
		/>
	);
};

export const MyButton = (props) => {
	const classes = useStyles();

	let className = 'button';
	if (props.label === 'Cancel') {
		className = 'buttonCancel';
	} else if (props.label === 'Delete') {
		className = 'buttonDelete';
	}
	return (
		<Button
			variant='contained'
			// color={buttonColor}
			// color={props.color ? props.color : 'primary'}
			// fullWidth
			className={classes[className]}
			onClick={props.onClick}
			style={{ width: props.width ? props.width : '100%' }}
			disabled={props.disabled ? true : false}
			// {...props}
		>
			{props.label}
		</Button>
	);
};

export const StatusMessage = (props) => {
	const width = props.width ? props.width : '95%';
	const textAlign = props.textAlign ? props.textAlign : 'center';
	const textColor = props.textColor ? props.textColor : 'text.primary';
	const type = props.type;
	const message = type && type === 'failed' ? props.errorMessage : props.message;
	const classes = useStyles();
	const rootClass = type && type === 'failed' ? classes.errorBox : classes.successBox;
	return (
		<Grid
			container
			direction='column'
			justify='center'
			alignItems='center'
			component={Box}
			width={width}
			textAlign={textAlign}
			color={textColor}
			classes={{ root: rootClass }}
			style={{ height: '3em' }}>
			<Grid item>{message}</Grid>
		</Grid>
	);
};

export const ErrorBox = (props) => {
	const classes = useStyles();
	return (
		<Grid
			item
			component={Box}
			width='95%'
			textAlign='center'
			color='text.primary'
			classes={{ root: classes.errorBox }}>
			{props.errorMessage}
		</Grid>
	);
};
