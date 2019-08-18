<?php
include 'dbconfig.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "documents/";
$url = "documents/".$originalImgName;
$id = $_POST['id'];
$name = $_POST['name'];

if(move_uploaded_file($tempName,$folder.$originalImgName)){
    
    $query = "UPDATE psu_ecpd SET resource_url = '$url' WHERE id = '$id'";

    if($conn->query($query) === TRUE){
        echo "";
    }else{
        echo "";
    }
}else{
    echo "";
}

?>