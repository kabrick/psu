<?php

include 'dbconfig.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "images/news/";
$url = "images/news/".$originalImgName;
$id = $_POST['id'];
$name = $_POST['name'];

if(move_uploaded_file($tempName,$folder.$originalImgName)){
    
    $query = "UPDATE psu_jobs SET attachment_url='$url', attachment_name='$name' WHERE id = '$id'";

    if($conn->query($query) === TRUE){
        echo "";
    }else{
        echo "";
    }
}else{
    echo "";
}

?>