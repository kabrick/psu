<?php

include 'dbconfig.php';
include 'functions.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "resources/";
$url = "resources/".$originalImgName;
$name = $_POST['name'];
$timestamp = $_POST['timestamp'];
$category = $_POST['category'];
$author = $_POST['author'];
$title = $_POST['title'];

if(move_uploaded_file($tempName,$folder.$originalImgName)){
    
    $query = "INSERT INTO psu_eresources (title, url, category, author, timestamp) VALUES ('$title','$url','$category','$author','$timestamp')";

    if($conn->query($query) === TRUE){
	send_push_notification("PSU Notification - New Resource", $title, 0);
        echo "";
    }else{
        echo "";
    }
}else{
    echo "";
}

?>