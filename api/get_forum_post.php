<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_forums WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$id = $result_assoc['author'];

//get the author full name
$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

$result1 = $conn->query($sql1);
$result_assoc1 = $result1->fetch_assoc();

//get the default info
$responce['author'] = $result_assoc1['name'];
$responce['title'] = $result_assoc['title'];
$responce['picture_url'] = $result_assoc['picture_url'];
$responce['document_url'] = $result_assoc['document_url'];
$responce['document_name'] = $result_assoc['document_name'];
$responce['moderator'] = $result_assoc['moderator'];
$responce['timestamp'] = $result_assoc['created_at'];

echo json_encode($responce);

?>