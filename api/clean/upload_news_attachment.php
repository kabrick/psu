<?php
include 'dbconfig.php';

//this is our upload folder
$upload_path = 'images/news/';

//creating the upload url
$upload_url = $upload_path;
$name = $_POST['name'];

//response array
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    //checking the required parameters from the request
    if(isset($_POST['id']) and isset($_FILES['pdf']['name'])){

        //getting name from the request
        $id = $_POST['id'];

        //getting file info from the request
        $fileinfo = pathinfo($_FILES['pdf']['name']);

        //getting the file extension
        $extension = $fileinfo['extension'];

        //file url to store in the database
        $file_url = $upload_url . $id . '.' . $extension;

        //file path to upload in the server
        $file_path = $upload_path . $id . '.'. $extension;

        //trying to save the file in the directory
        try{
            //saving the file
            move_uploaded_file($_FILES['pdf']['tmp_name'],$file_path);

            // update the news here

            $query = "UPDATE psu_news SET attachment_url='$file_url', attachment_name='$name' WHERE id = '$id'";

            //adding the path and name to database
            if($conn->query($query) === TRUE){
                //filling response array with values
                $response['error'] = false;
                $response['url'] = $file_url;
                $response['name'] = $name;
            }
            //if some error occurred
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        } 
    }else{
        $response['error']=true;
        $response['message']='Please choose a file';
    }
    
    //displaying the response
    echo json_encode($response);
}

?>