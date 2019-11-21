<?php
include 'dbconfig.php';
include 'functions.php';

$picturesOriginalImgName = $_FILES['picture_filename']['name'];
$picturesTempName = $_FILES['picture_filename']['tmp_name'];
$picturesFolder = "news/pictures/";
$picturesUrl = "news/pictures/".$picturesOriginalImgName;
$documentsOriginalImgName = $_FILES['document_filename']['name'];
$documentsTempName = $_FILES['document_filename']['tmp_name'];
$documentsFolder = "news/documents/";
$documentsUrl = "news/documents/".$documentsOriginalImgName;

$title = mysqli_real_escape_string($conn, $_POST['title']);
$text = mysqli_real_escape_string($conn, $_POST['text']);
$source = mysqli_real_escape_string($conn, $_POST['source']);
$author_id = mysqli_real_escape_string($conn, $_POST['author_id']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

if(!move_uploaded_file($picturesTempName,$picturesFolder.$picturesOriginalImgName)){
    $picturesUrl = 0;
    $picturesOriginalImgName = 0;
}

if(!move_uploaded_file($documentsTempName,$documentsFolder.$documentsOriginalImgName)){
    $documentsUrl = 0;
    $documentsOriginalImgName = 0;
}

$sql = "INSERT INTO psu_news (title, text, source, author, timestamp, attachment_url, attachment_name, photo) VALUES ('$title','$text','$source','$author_id','$timestamp','$documentsUrl','$documentsOriginalImgName','$picturesUrl')";

if ($conn->query($sql) === TRUE) {
	send_admin_notifications_news($title);

	send_push_notification('PSU Approval Needed', 'News post has been submitted that needs your approval.', '1');

	echo "1";
} else {
    echo "0";
}
?>