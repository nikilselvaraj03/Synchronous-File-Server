import React from 'react';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import './FilesList.css'
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Tooltip } from '@mui/material';
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Slide from '@mui/material/Slide';
import { useEffect } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import TextEditor from '../TextEditor/TextEditor'

function FilesList({refresh}) {
  const [connected,setConnected] = React.useState(false);
  const [prevrefresh,setprevrefresh] = React.useState(false);
  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '60%',
    height: '68%',
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };
  const [deleteFileName,setDeleteName] = React.useState('');
  const [Deleteopen, setDeleteOpen] = React.useState(false);
  const [EditOpen, setEditOpen] = React.useState(false);
  const [FileData,setFileData] = React.useState(''); 
  const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
  });
  

  const handleDeleteOpen = (name) => {
    setDeleteName(name);
    setDeleteOpen(true);
  };

  const handleDeleteClose = () => {
    setDeleteOpen(false);
  };

  const handleDelete = () => {

    axios.post(`http://localhost:8080/file/delete/${deleteFileName}`).then(({data}) => {
      debugger
      if (data['code'] !== 200) {
        toast.error(data['msg'])
      } else {
      toast.success('File deleted Sucessfully'); }
      getFilesList();
  })
    setDeleteOpen(false)
  }

  const handleEditOpen = (row) => {
        setFileData(row.fileName)
        debugger
        console.log(FileData)
        setEditOpen(true);
  };

  const handleEditClose = () => {
    setEditOpen(false);
    getFilesList();
  };

  const [files,setFiles] = React.useState([]);


  function getFilesList() {
    axios.get('http://localhost:8080/file/list').then(data => {
      setFiles(data.data);
    })
  }


  useEffect(() => {
    if(refresh !== prevrefresh) {
      setprevrefresh(!prevrefresh);
      getFilesList()
    }
    if(!connected) {
    var socket = new WebSocket("ws://localhost:9987").onopen= (data) => {
      getFilesList()
      setConnected(true);
    }
    socket.onmessage = (message) => {
      debugger;
      getFilesList();
    }
    }
  });


  return (
    <div className="FilesList">
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell align="right">File Name</TableCell>
            <TableCell align="right">File Size&nbsp;(mb)</TableCell>
            <TableCell align="right">Created Date</TableCell>
            <TableCell align="right">Created By</TableCell>
            <TableCell align="right">Last Modified Date</TableCell>
            <TableCell align="right">Last Modified By</TableCell>
            <TableCell align="right">Sync Status</TableCell>
            <TableCell align="right"> &nbsp; &nbsp; &nbsp;</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {files.map((row) => (
            <TableRow
              key={row.name}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row" align="right">
                {row.fileName}
              </TableCell>
              <TableCell align="right">{row.size}</TableCell>
              <TableCell align="right">{row.createddate}</TableCell>
              <TableCell align="right">{row.createdby}</TableCell>
              <TableCell align="right">{row.lastMod}</TableCell>
              <TableCell align="right">{row.modifiedby}</TableCell>
              <TableCell align="right">{row.syncstatus}</TableCell>
              <TableCell align="right">{row.fileName.split('.')[row.fileName.split('.').length -1] === 'txt' ?  <span className='EditIcon'><Tooltip title="Edit"
              placement="top"><EditIcon onClick={()=>handleEditOpen(row)}></EditIcon></Tooltip></span> : ''} 
              <span className='DeleteIcon'><Tooltip title="Delete"
              placement="top"><DeleteIcon onClick={() =>handleDeleteOpen(row.fileName)}></DeleteIcon></Tooltip></span></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    {(files.length === 0) ? <div className='nofiles'>There are no files. Please upload a new file to view it here.</div> : ''}
    <Dialog
        open={Deleteopen}
        TransitionComponent={Transition}
        keepMounted
        onClose={handleDeleteClose}
        aria-describedby="alert-dialog-slide-description"
      >
        <DialogTitle>{"Do you want to delete this file?"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-slide-description">
            This action will delete the file for all clients and is <b>irreversible</b>.
            do you want to delete it anyway?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteClose}>Cancel</Button>
          <Button onClick={handleDelete}><span style={{color:'red'}}>Delete</span></Button>
        </DialogActions>
      </Dialog>

    <Modal
      open={EditOpen}
      onClose={handleEditClose}
    >
      <Box sx={style}>
        <TextEditor  fileName={FileData}></TextEditor>
      </Box>
    </Modal>
    <ToastContainer
        position="bottom-center"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
 }

FilesList.propTypes = {};

FilesList.defaultProps = {};

export default FilesList;
