<?php

include 'dbconfig.php';

$picturesOriginalImgName = $_FILES['picture_filename']['name'];
$picturesTempName = $_FILES['picture_filename']['tmp_name'];
$picturesFolder = "discussion_forum/pictures/";
$picturesUrl = "discussion_forum/pictures/".$picturesOriginalImgName;
$documentsOriginalImgName = $_FILES['document_filename']['name'];
$documentsTempName = $_FILES['document_filename']['tmp_name'];
$documentsFolder = "discussion_forum/documents/";
$documentsUrl = "discussion_forum/documents/".$documentsOriginalImgName;
$title = $_POST['title'];
$author_id = $_POST['author_id'];
$timestamp = $_POST['timestamp'];

if(!move_uploaded_file($picturesTempName,$picturesFolder.$picturesOriginalImgName)){
    $picturesUrl = 0;
    $picturesOriginalImgName = 0;
}

if(!move_uploaded_file($documentsTempName,$documentsFolder.$documentsOriginalImgName)){
    $documentsUrl = 0;
    $documentsOriginalImgName = 0;
}

$sql = "INSERT INTO psu_forums (title, picture_url, picture_name, document_url, document_name, author, created_at) VALUES ('$title','$picturesUrl','$picturesOriginalImgName','$documentsUrl','$documentsOriginalImgName','$author_id','$timestamp')";

if($conn->query($sql) === TRUE){
    echo "1";
}else{
    echo "0";
}

?>