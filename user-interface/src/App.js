import './App.css';
import FilesList from './components/FilesList/FilesList';
import PauseCircleOutlineOutlinedIcon from '@mui/icons-material/PauseCircleOutlineOutlined';
import CachedOutlinedIcon from '@mui/icons-material/CachedOutlined';
import { Tooltip } from '@mui/material';
import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import UploadIcon from '@mui/icons-material/Upload';
import axios from 'axios';
import syncicon from './resources/sync.png';

function App() {
  const [isSuspended,setisSuspended] = useState(false);
  const [refresh,setrefresh] = useState(true);
  let selectedFile = null
  function suspendorresume() {
    setisSuspended(!isSuspended)
    toast.dismiss()
    if(!isSuspended) {
      toast.error("All tasks and transfers are now suspended!");
    } else {
      toast.success("All tasks and transfers are now resumed");
    }
  }

  function fileUpload() {
    document.getElementById('fileUpload').click()
  }

  function onFileChange(e) {
    e.preventDefault();
    const info = e.target.files[0];
    const reader = new FileReader();
    reader.onload = (e) => {
      selectedFile = {fileName:info.name,newContent:e.target.result};
      axios.post('http://localhost:8080/file/create',selectedFile).then(({data})=>{
        if (data['code'] !== 200) {
          toast.error(data['msg'])
        } else {
          toast.dismiss()
        toast.success("File Uploaded Sucessfully")}
        setrefresh(!refresh);}
      )
    };
    reader.readAsText(e.target.files[0]);
  }
  return (
    <div className="App">
     <p className='header'>
      <span className='title'>
      <img src={syncicon} style={{height:'60px',width:'65px',paddingBottom:'5px'}}></img>
        SYNC-FE
      </span>

      <div>
        <input type="file" className='d-none' onChange={onFileChange} id='fileUpload' accept=".txt" />
        <span onClick={fileUpload}>
        <Tooltip
        title="Upload new file"
        placement="top">
          <UploadIcon fontSize='large'></UploadIcon></Tooltip></span>
        <span onClick={suspendorresume}>
          { !isSuspended ? 
        <Tooltip
        title="suspend"
        placement="top">
      <PauseCircleOutlineOutlinedIcon fontSize='large'/></Tooltip> : 
      <Tooltip
      title="resume"
      placement="top"><CachedOutlinedIcon fontSize='large'/>
        </Tooltip> }
        </span>
      </div>
     </p>

     <FilesList refresh={refresh}/>

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

export default App;
