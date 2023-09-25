import { useEffect, useState } from "react";
import React from 'react';
import axios from "axios";
import { Button } from "@mui/material";
import Pagination from '@mui/material/Pagination';
import './TextEditor.css'
function TextEditor({fileName}) {
const [initial,setinitial] = useState(false)
const [page,setpage]=useState(1)
const [totalpages,setTotalPages]=useState(0);
const [data,setData] = useState('');
const [originaldata,setoriginaldata]=useState('');
useEffect(()=> {
if(!initial){
setinitial(true)
axios.get(`http://localhost:8080/file/read/${fileName}`).then((result)=>{
setpage(result.data.page);
setData(result.data[fileName]);
setTotalPages(result.data.totalPages);
setoriginaldata(JSON.parse(JSON.stringify(result.data[fileName])))
})}
})
const [changed, setchanged] = useState(false);
function changeContent(event) {
setchanged(true);
setData(event.target.value);

}

function updateContent() {
axios.post('http://localhost:8080/file/update', {
fileName: fileName,
newContent:data,
original: originaldata,
pageIndex: page
})
}

function updatePage(event) {
axios.get(`http://localhost:8080/file/read/${fileName}/${Number(event.target.innerText)}/${totalpages}`).then((result) => {
debugger
setpage(result.data.page);
setData(result.data[fileName]);
setTotalPages(result.data.totalPages);
})
}

return (<div className="editor-field"><textarea value={data} onChange={changeContent}></textarea>
<div className="footer">
<span></span>
<Pagination count={totalpages} page={page} onClick={updatePage} variant="outlined" color="secondary" />
<Button variant="outlined" color="success" onClick={updateContent} disabled={!changed}>
Update
</Button></div></div>)
}



export default TextEditor;