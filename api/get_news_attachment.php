<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT attachment_url, attachment_name FROM psu_news WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

// check if attachment exists
if(!is_null($result_assoc['attachment_url'])){
	$responce['attachment_url'] = $result_assoc['attachment_url'];
	$responce['attachment_name'] = $result_assoc['attachment_name'];
} else {
	$responce['attachment_url'] = 0;
}

echo json_encode($responce);

?>