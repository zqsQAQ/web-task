import { MessageBox } from "element-ui";
export default function myconfirm(text){
    return new Promise((resolve,reject) => {
        MessageBox.confirm(text,'system hint',{
            confirmButtonText:'yes',
            cancelButtonText:'no',
            type:'warning'
        }).then(()=>{
            resolve(true);
        }).catch(()=>{
            Message.warning('Cancelled')
            reject(false)
        })
    }).catch(()=>{
        return false;
    })
}